package com.example.android.alphap.playgames.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class GameManager extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        RealTimeMessageReceivedListener, RoomStatusUpdateListener,
        RoomUpdateListener, OnInvitationReceivedListener {

    // Request codes for the UIs that we show with startActivityForResult:
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;
    final static int GAME_DURATION = 20; // game duration, seconds.
    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;
    // Room ID where the currently active game is taking place; null if we're not playing.
    String mRoomId = null;
    // Are we playing in multiplayer mode?
    boolean mMultiplayer = false;
    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;
    // My participant ID in the currently active game
    String mMyId = null;
    private List<String> currentPlayers = new ArrayList<>();
    // If non-null, this is the id of the invitation we received via the
    // invitation listener
    String mIncomingInvitationId = null;
    // Message buffer for sending messages
    byte[] mMsgBuf = new byte[2];
    // Current state of the game:
    int mSecondsLeft = -1; // how long until the game ends (seconds)
    // Client used to interact with Google APIs.
    private GoogleApiClient mGoogleApiClient;
    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;
    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;
    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;
    // Score of other participants. We update this as we receive their scores
    // from the network.
    private Map<String, Integer> mParticipantScore = new HashMap<String, Integer>();
    // Participants who sent us their final score.
    private Set<String> mFinishedParticipants = new HashSet<String>();
    private int mScore;

    // Activity listening to our callback methods
    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Listener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Create the Google Api Client with access to Games
        mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
    }

    /**
     * Activity just got to the foreground. We switch to the wait screen because we will now
     * go through the sign-in flow (remember that, yes, every time the Activity comes back to the
     * foreground we go through the sign-in flow -- but if the user is already authenticated,
     * this flow simply succeeds and is imperceptible).
     */
    @Override
    public void onStart() {
        if (mGoogleApiClient == null) {
            listener.showSignIn();
        } else if (!mGoogleApiClient.isConnected()) {
            Log.d(TAG, "Connecting client.");
            listener.showProgress(true);
            mGoogleApiClient.connect();
        } else {
            Log.w(TAG, "GameHelper: client was already connected on onStart()");
        }

        super.onStart();
    }

    public void signIn() {
        // Start the sign-in flow
        Log.d(TAG, "Sign-in button clicked");
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    public void signOut() {
        // Sign the user out
        Log.d(TAG, "Sign-out button clicked");
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
        mGoogleApiClient.disconnect();
        listener.showSignIn();
    }

    public void createGame() {
        // Show list of invitable players
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 3);
        listener.showProgress(true);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    public void joinGame() {
        // Show list of pending invitations
        Intent intent = Games.Invitations.getInvitationInboxIntent(mGoogleApiClient);
        listener.showProgress(true);
        startActivityForResult(intent, RC_INVITATION_INBOX);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");

        // Activity is going to the background. We have to leave the current room.
        // If we're in a room, leave it.
        leaveRoom();

        // Stop trying to keep the screen on
        listener.stopKeepingScreenOn();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            listener.showMenu();
        } else {
            listener.showSignIn();
        }

        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        Log.d(TAG, "onConnected() called. Sign in successful!");

        // Once we're signed in, go to the menu
        listener.showMenu();

        // Register listener so we are notified if we receive an invitation to play
        // while we are in the game
        Games.Invitations.registerInvitationListener(mGoogleApiClient, this);

        if (connectionHint != null) {
            Log.d(TAG, "onConnected: connection hint provided. Checking for invite.");
            Invitation inv = connectionHint
                    .getParcelable(Multiplayer.EXTRA_INVITATION);
            if (inv != null && inv.getInvitationId() != null) {
                // retrieve and cache the invitation ID
                Log.d(TAG, "onConnected: connection hint has a room invite!");
                acceptInviteToRoom(inv.getInvitationId());
                return;
            }
        }


    }

    private void switchToMainScreen() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            listener.showMenu();
        } else {
            listener.showSignIn();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = listener.resolveConnectionFailure(connectionResult, mGoogleApiClient);
        }

        listener.showSignIn();
    }

    // Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
    // is connected yet).
    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom.");

        //get participants and my ID:
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mGoogleApiClient));

        // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
        if (mRoomId == null)
            mRoomId = room.getRoomId();

        // print out the list of participants (for debug purposes)
        Log.d(TAG, "Room ID: " + mRoomId);
        Log.d(TAG, "My ID " + mMyId);
        Log.d(TAG, "<< CONNECTED TO ROOM>>");
    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        mIncomingInvitationId = invitation.getInvitationId();
        listener.showInvitationPopup(invitation.getInviter().getDisplayName());
    }

    @Override
    public void onInvitationRemoved(String invitationId) {
        if (mIncomingInvitationId.equals(invitationId) && mIncomingInvitationId != null) {
            mIncomingInvitationId = null;
            listener.hideInvitationPopup();
        }
    }


    // We treat most of the room update callbacks in the same way: we update our list of
    // participants and update the display. In a real game we would also have to check if that
    // change requires some action like removing the corresponding player avatar from the screen,
    // etc.
    @Override
    public void onPeerDeclined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onP2PDisconnected(String participant) {
    }

    @Override
    public void onP2PConnected(String participant) {
    }

    @Override
    public void onPeerJoined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerLeft(Room room, List<String> peersWhoLeft) {
        updateRoom(room);
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        updateRoom(room);
    }

    @Override
    public void onRoomConnecting(Room room) {
        updateRoom(room);
    }

    @Override
    public void onPeersConnected(Room room, List<String> peers) {
        updateRoom(room);
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
        updateRoom(room);
    }

    /**
     * Called when we get disconnected from the room. We return to the main screen.
     */
    @Override
    public void onDisconnectedFromRoom(Room room) {
        mRoomId = null;
        listener.showGameError();
        switchToMainScreen();
    }


    /**
     * Called when room has been created
     */
    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            listener.showGameError();
            switchToMainScreen();
            return;
        }

        // save room ID so we can leave cleanly before the game starts.
        mRoomId = room.getRoomId();

        // show the waiting room UI
        listener.showWaitingRoom(mGoogleApiClient, room, Integer.MAX_VALUE);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            listener.showGameError();
            switchToMainScreen();
            return;
        }

        // show the waiting room UI
        listener.showWaitingRoom(mGoogleApiClient, room, Integer.MAX_VALUE);
    }

    /**
     * Called when we've successfully left the room (this happens a result of voluntarily leaving
     * via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
     */
    @Override
    public void onLeftRoom(int statusCode, String roomId) {
        // we have left the room; return to main screen.
        Log.d(TAG, "onLeftRoom, code " + statusCode);
        switchToMainScreen();
    }

    /**
     * Called when room is fully connected.
     */
    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            listener.showGameError();
            switchToMainScreen();
            return;
        }
        updateRoom(room);
    }

    /**
     * Handle the result of the invitation inbox UI, where the player can pick an invitation
     * to accept. We react by accepting the selected invitation, if any.
     */
    public void handleInvitationInboxResult(int response, Intent data) {
        if (response != RESULT_OK) {
            Log.w(TAG, "*** invitation inbox UI cancelled, " + response);
            listener.showMenu();
            return;
        }

        Log.d(TAG, "Invitation inbox UI succeeded.");
        Invitation inv = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);

        acceptInviteToRoom(inv.getInvitationId());
    }

    private void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
            for (Participant participant : mParticipants) {
                if (!currentPlayers.contains(participant.getParticipantId())) {
                    currentPlayers.add(participant.getParticipantId());
                }
            }
        }
        if (mParticipants != null) {
            listener.updatePeerScoresDisplay(mParticipants, mParticipantScore, mRoomId, mMyId, mScore);
        }
    }

    /**
     * Accept the given invitation.
     */
    private void acceptInviteToRoom(String invId) {
        // Accept the invitation
        Log.d(TAG, "Accepting invitation: " + invId);
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setInvitationIdToAccept(invId)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
        listener.showProgress(true);
        listener.keepScreenOn();
        resetGameVars();
        Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());
    }

    public void handleSelectPlayersResult(int response, Intent data) {
        if (response != RESULT_OK) {
            Log.w(TAG, "*** select players UI cancelled, " + response);
            listener.showMenu();
            return;
        }

        Log.d(TAG, "Select players UI succeeded.");

        // Get the invitee list
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.d(TAG, "Invitee count: " + invitees.size());

        // Get the automatch criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
        }

        // Create the room
        Log.d(TAG, "Creating room...");
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.addPlayersToInvite(invitees);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        if (autoMatchCriteria != null) {
            rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        }
        listener.showProgress(true);
        listener.keepScreenOn();
        resetGameVars();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());
        Log.d(TAG, "Room created, waiting for it to be ready...");
    }

    public void handleSignInResult(Intent intent, int requestCode, int responseCode) {
        Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                + responseCode + ", intent=" + intent);
        mSignInClicked = false;
        mResolvingConnectionFailure = false;
        if (responseCode == RESULT_OK) {
            mGoogleApiClient.connect();
            listener.showMenu();
        } else {
            listener.showSignInError(requestCode, responseCode);
        }
    }

    /***********************************************************************************************************
     * GAME LOGIC SECTION. Methods that implement the game's rules.
     */

    /**
     * Reset game variables in preparation for a new game.
     */
    private void resetGameVars() {
        mSecondsLeft = GAME_DURATION;
        mScore = 0;
        mParticipantScore.clear();
        mFinishedParticipants.clear();
    }

    /**
     * Start the gameplay phase of the game.
     */
    public void startGame(boolean multiplayer) {
        Log.d(TAG, "Starting game (waiting room returned OK).");
        mMultiplayer = multiplayer;
        listener.updateScoreDisplay(mScore);
        Random random = new Random();
        int randomPlayerToStart = random.nextInt(currentPlayers.size() - 1);
        sendPotato(false, randomPlayerToStart);

        listener.showGameUI();
        listener.showClickMeButton(true);

        // run the gameTick() method every second to update the game.
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSecondsLeft <= 0)
                    return;
                gameTick();
                h.postDelayed(this, 1000);
            }
        }, 1000);
    }

    /**
     * Game tick -- update countdown, and check if game ended.
     */
    private void gameTick() {
        if (mSecondsLeft > 0)
            --mSecondsLeft;

        // Update countdown
        listener.updateCountDown("0:" + (mSecondsLeft < 10 ? "0" : "") + String.valueOf(mSecondsLeft));

        if (mSecondsLeft <= 0) {
            // Time's up, Finish game
            finishGame();
        }
    }

    /**
     * Indicates the player scored one point.
     */

    //TODO keeping score? maybe?
//    public void scoreOnePoint() {
//        if (mSecondsLeft <= 0) {
//            // too late, game is over!
//
//        } else {
//            // Score a point
//            mScore++;
//
//            // Update the scoreboard
//            listener.updateScoreDisplay(mScore);
//            listener.updatePeerScoresDisplay(mParticipants, mParticipantScore, mRoomId, mMyId, mScore);
//
//            // Broadcast our new score to our peers
//            sendPotato(false);
//        }
//    }

    /**
     * Time's up; finish the game.
     */
    //TODO finish game logic
    private void finishGame() {
        listener.showClickMeButton(false);
        //sendPotato(true);
        //display LOST or WON message on screens
    }

    // Leave the room.
    public void leaveRoom() {
        Log.d(TAG, "Leaving room.");
        mSecondsLeft = 0;
        listener.stopKeepingScreenOn();
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
            listener.showProgress(true);
        } else {
            listener.showMenu();
        }
    }

    /***********************************************************************************************************
     * COMMUNICATIONS SECTION. Methods that implement the game's network
     * protocol.
     */

    /**
     * Called when we receive a real-time message from the network.
     * Messages in our game are made up of 2 bytes: the first one is 'F' or 'U'
     * indicating whether it's a final or interim score. The second byte is the score.
     * There is also the 'S' message, which indicates that the game should start.
     */
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
        byte[] buf = rtm.getMessageData();
        String sender = rtm.getSenderParticipantId();
        int indexOfPlayer = buf[1];
        if (mMyId.equals(currentPlayers.get(indexOfPlayer))){
            //TODO display potato on screen
        }
            Log.d(TAG, "Message received: " + (char) buf[0] + "/" + (int) buf[1]);


        if (buf[0] == 'T' || buf[0] == 'F') {
            // score update.
            int existingScore = mParticipantScore.containsKey(sender) ?
                    mParticipantScore.get(sender) : 0;
            int thisScore = (int) buf[1];
            if (thisScore > existingScore) {
                // this check is necessary because packets may arrive out of
                // order, so we
                // should only ever consider the highest score we received, as
                // we know in our
                // game there is no way to lose points. If there was a way to
                // lose points,
                // we'd have to add a "serial number" to the packet.
                mParticipantScore.put(sender, thisScore);
            }

            // update the scores on the screen
            listener.updatePeerScoresDisplay(mParticipants, mParticipantScore, mRoomId, mMyId, mScore);

            // if it's a final score, mark this participant as having finished
            // the game
            if ((char) buf[0] == 'F') {
                mFinishedParticipants.add(rtm.getSenderParticipantId());
            }
        }
    }

    /**
     * Broadcast my score to everybody else.
     */
    private void sendPotato(boolean hasPotato, int indexOfPlayerWithPotato) {
        if (!mMultiplayer)
            return; // playing single-player mode

        // First byte in message indicates whether it's a final score or not
        mMsgBuf[0] = (byte) (hasPotato ? 'T' : 'F');

        // Second byte is the player with the potato.
        mMsgBuf[1] = (byte) indexOfPlayerWithPotato;

        // Send to every other participant.
        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus() != Participant.STATUS_JOINED)
                continue;
            if (isPotatoPopped()) {
                // final score notification must be sent via reliable message
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, mMsgBuf,
                        mRoomId, p.getParticipantId());
            } else {
                // it's an interim score notification, so we can use unreliable
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, mMsgBuf, mRoomId,
                        p.getParticipantId());
            }
        }
    }

    private boolean isPotatoPopped() {
        //TODO create logic to check if timer ran out
        return false;
    }

    public void acceptInvite() {
        // User wants to accept the invitation shown on the invitation popup
        // (the one we got through the OnInvitationReceivedListener).
        acceptInviteToRoom(mIncomingInvitationId);
        mIncomingInvitationId = null;
    }

    public interface Listener {

        void showSignIn();

        void showMenu();

        void showGameUI();

        void showProgress(boolean show);

        void updateScoreDisplay(int score);

        void updatePeerScoresDisplay(List<Participant> participants, Map<String, Integer> participantScore, String roomId, String myId, int myScore);

        void keepScreenOn();

        void stopKeepingScreenOn();

        void showInvitationPopup(String invitePlayerName);

        void hideInvitationPopup();

        void showClickMeButton(boolean show);

        void updateCountDown(String countdownText);

        void showSignInError(int requestCode, int responseCode);

        void showWaitingRoom(GoogleApiClient apiClient, Room room, int minPlayers);

        void showGameError();

        boolean resolveConnectionFailure(ConnectionResult connectionResult, GoogleApiClient apiClient);
    }
}

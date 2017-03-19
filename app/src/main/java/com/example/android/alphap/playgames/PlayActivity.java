package com.example.android.alphap.playgames;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.android.alphap.R;
import com.example.android.alphap.playgames.logic.GameManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayActivity extends AppCompatActivity implements
        SignInFragment.Listener,
        MenuFragment.Listener, GameFragment.Listener,
        GameManager.Listener {

    //Played game first time- GamesPlayed variable gamesPlayed
    //Lost 3 times in a row- check how many times lost gamesLost
    //Reached 500 points
    //TODO: Make sure to set a limit to how long a user holds the potato
    //Played and won 10 games without the clock?

    public static final String GAME_MANAGER = "GAME_MANAGER";

    // Request codes for the UIs that we show with startActivityForResult:
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;
    private static final String TAG = "play activity";

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    private SignInFragment signInFragment;
    private MenuFragment menuFragment;
    private GameFragment gameFragment;
     ProgressBar progressBar;
    private LinearLayout invitationPopup;

    private GameManager gameManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        invitationPopup = (LinearLayout) findViewById(R.id.invitation_popup);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        signInFragment = new SignInFragment();
        menuFragment = new MenuFragment();
        gameFragment = new GameFragment();

        loadConnectionFragment();

        Button acceptInvite = (Button) findViewById(R.id.button_accept_popup_invitation);
        acceptInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.acceptInvite();
            }
        });
    }

    private void loadConnectionFragment() {
        gameManager = (GameManager) getSupportFragmentManager().findFragmentByTag(GAME_MANAGER);
        if (gameManager == null) {
            gameManager = new GameManager();
            getSupportFragmentManager().beginTransaction()
                    .add(gameManager, GAME_MANAGER)
                    .commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        switch (requestCode) {
            case RC_SELECT_PLAYERS:
                // We got the result from the "select players" UI -- ready to create the room
                gameManager.handleSelectPlayersResult(responseCode, intent);
                break;
            case RC_INVITATION_INBOX:
                // We got the result from the "select invitation" UI (invitation inbox). We're
                // ready to accept the selected invitation:
                gameManager.handleInvitationInboxResult(responseCode, intent);
                break;
            case RC_WAITING_ROOM:
                // We got the result from the "waiting room" UI.
                if (responseCode == Activity.RESULT_OK) {
                    // Ready to start playing
                    gameManager.startGame(true);
                } else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // Player indicated that they want to leave the room
                    gameManager.leaveRoom();
                } else if (responseCode == Activity.RESULT_CANCELED) {
                    // Dialog was cancelled (user pressed back key, for instance). In our game,
                    // this means leaving the room too. In more elaborate games, this could mean
                    // something else (like minimizing the waiting room UI).
                    gameManager.leaveRoom();
                }
                break;
            case RC_SIGN_IN:
                gameManager.handleSignInResult(intent, requestCode, responseCode);
                break;
        }
        super.onActivityResult(requestCode, responseCode, intent);
    }

    /**
     * Something went wrong. Show the user that there was an error signing in.
     */
    @Override
    public void showSignInError(int requestCode, int responseCode) {
        BaseGameUtils.showActivityResultError(this, requestCode, responseCode, R.string.signin_other_error);
    }

    /**
     * Handle back key to make sure we cleanly leave a game if we are in the middle of one
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_BACK && getSupportFragmentManager().findFragmentById(R.id.container) instanceof GameFragment) {
            gameManager.leaveRoom();
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

    /**
     * Show the waiting room UI to track the progress of other players as they enter the room and get connected.
     */
    @Override
    public void showWaitingRoom(GoogleApiClient apiClient, Room room, int minPlayers) {
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(apiClient, room, minPlayers);
        startActivityForResult(i, RC_WAITING_ROOM);
    }

    /**
     * Show error message about game being cancelled and return to main screen.
     */
    @Override
    public void showGameError() {
        BaseGameUtils.makeSimpleDialog(this, getString(R.string.game_problem));
    }

    @Override
    public boolean resolveConnectionFailure(ConnectionResult connectionResult, GoogleApiClient apiClient) {
        return BaseGameUtils.resolveConnectionFailure(this, apiClient, connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
    }


    private void showInvitationPopup(boolean show) {
        findViewById(R.id.invitation_popup).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Updates the label that shows the current user's score
     */
    //TODO update time?
    @Override
    public void updateScoreDisplay(int score) {
        gameFragment.updateScoreDisplay(score);
    }

    /**
     * Updates the screen with the scores from other players.
     */
    //TODO come back to update player screens
    @Override
    public void updatePeerScoresDisplay(List<Participant> participants, Map<String, Integer> participantScore, String roomId, String myId, int myScore) {
       // gameFragment.updatePeerScoresDisplay(participants, participantScore, roomId, myId, myScore);
    }

    /**
     * Sets the flag to keep this screen on. It's recommended to do that during the
     * handshake when setting up a game, because if the screen turns off, the game will be
     * cancelled.
     */
    @Override
    public void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Clears the flag that keeps the screen on.
     */
    @Override
    public void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void showSignIn() {
        Log.d(TAG, "showSignIn: ");
        showProgress(false);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, signInFragment);
        ft.commit();
    }

    @Override
    public void showMenu() {
        Log.d(TAG, "showMenu: ");
        showProgress(false);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, menuFragment);
        ft.commit();
    }

    @Override
    public void showGameUI() {
        Log.d(TAG, "showGameUI: ");
        showProgress(false);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, gameFragment);
        ft.commit();
    }

    @Override
    public void showProgress(boolean show) {
        // progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showInvitationPopup(String invitePlayerName) {
        invitationPopup.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInvitationPopup() {
        invitationPopup.setVisibility(View.INVISIBLE);
    }

    //TODO show potato

    @Override
    public void showClickMeButton(boolean show) {
      //  gameFragment.showClickMeButton(show);
    }

    @Override
    public void updateCountDown(String countdownText) {
       // gameFragment.updateCountDown(countdownText);
    }

    /**
     * SignInFragment.Listener methods
     */
    @Override
    public void onSignInClicked() {
        gameManager.signIn();
    }

    /**
     * MenuFragment.Listener methods
     */
    @Override
    public void onCreateGameClicked() {
        gameManager.createGame();
    }

    @Override
    public void onJoinGameClicked() {
        gameManager.joinGame();
    }

    @Override
    public void onSignOutClicked() {
        gameManager.signOut();
    }

    @Override
    public void scoreOnePoint() {
    //    gameManager.scoreOnePoint();
    }
}


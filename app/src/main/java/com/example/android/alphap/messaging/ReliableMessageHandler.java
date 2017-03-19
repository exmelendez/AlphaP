package com.example.android.alphap.messaging;

import android.os.Handler;
import android.util.SparseArray;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMultiplayer;

/**
 * Simple try-again handler to keep track of the status of messages sent via Games.RealTimeMultiplayer.sendReliableMessage.
 * The handler will keep a record of realtime reliable message attempts + their tokens in the messageCache. If a failure
 * status code arrives in the onRealTimeMessageSent callback, the handler will try again (to the attempt limit) or report
 * a network failure to the user.
 * If onRealTimeMessageSent fails to receive a status code any message within SEND_ATTEMPT_TIMEOUT, the handler will timeout
 * and report a network failure to the user.
 */

public class ReliableMessageHandler implements RealTimeMultiplayer.ReliableMessageSentCallback {
    private static final int SEND_ATTEMPT_TIMEOUT = 10000;
    private static final int RETRY_DELAY = 5000;

    private GoogleApiClient apiClient;
    private ReliableMessageResultListener resultListener;
    private SparseArray<MessageAttempt> messageCache;

    public ReliableMessageHandler(GoogleApiClient apiClient, ReliableMessageResultListener resultListener) {
        this.apiClient = apiClient;
        this.resultListener = resultListener;
        this.messageCache = new SparseArray<>();
    }

    public void send(MessageAttempt message) {
        final int token = Games.RealTimeMultiplayer.sendReliableMessage(apiClient,
                this,
                message.getMessageData(),
                message.getRoomId(),
                message.getRecipientParticipantId());

        message.incrementAttempts();
        messageCache.put(token, message);

        if (token == RealTimeMultiplayer.REAL_TIME_MESSAGE_FAILED) {
            // The message failed to send immediately, stop trying and let the listener know.
            message.stopAttempting();
            messageCache.put(token, message);
            onRealTimeMessageSent(token, token, message.getRecipientParticipantId());
        } else {
            // The message is sending, start a timeout in case we dont hear back
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkStatusOnTimeout(token);
                }
            }, SEND_ATTEMPT_TIMEOUT);
        }
    }

    private void checkStatusOnTimeout(int token) {
        // Check if the message is still in the cache. If it is, we still haven't received an onRealTimeMessageSent callback. Lets consider it a failure and bail
        MessageAttempt message = messageCache.get(token);
        messageCache.remove(token);
        if (message != null) {
            message.stopAttempting();
            messageCache.put(token, message);
            onRealTimeMessageSent(GamesStatusCodes.STATUS_TIMEOUT, token, message.getRecipientParticipantId());
        }
    }

    public void onSuccess(int token) {
        // The message was successfully sent, clear it from the cache.
        messageCache.remove(token);
    }

    public int onFailure(int token, int statusCode) {
        // The message failed to send, remove it from the cache.
        final MessageAttempt message = messageCache.get(token);
        messageCache.remove(token);

        // If attempts remain, retry. Otherwise, return the last status code.
        if (message != null && message.shouldTry()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (apiClient != null) {
                        send(message);
                    }
                }
            }, RETRY_DELAY);

            return GamesStatusCodes.STATUS_OK;
        } else {
            return statusCode;
        }
    }

    public void disconnect() {
        // Make the listener null so any lingering async handlers will leave it alone
        apiClient = null;
        resultListener = null;
    }


    @Override
    public void onRealTimeMessageSent(int statusCode, int token, String recipientParticipantId) {
        if (statusCode == GamesStatusCodes.STATUS_OK) {
            // The message sent successfully
            onSuccess(token);
        } else if (resultListener != null) {
            // Try again
            int retryStatusCode = onFailure(token, statusCode);

            if (retryStatusCode != GamesStatusCodes.STATUS_OK) {
                // Max send attempts hit, time to handle the error
                resultListener.handleReliableMessageFailure(retryStatusCode);
            } else {
                // Show loading fragment to let the user know its taking awhile
                resultListener.showLoadingOnDelay();
            }
        }
    }

    public interface ReliableMessageResultListener {
        void handleReliableMessageFailure(int retryStatusCode);

        void showLoadingOnDelay();
    }
}


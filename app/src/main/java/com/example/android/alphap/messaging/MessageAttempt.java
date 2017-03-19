package com.example.android.alphap.messaging;

public class MessageAttempt {
    private byte[] messageData;
    private String roomId;
    private String recipientParticipantId;
    private int maxAttempts;
    private int attempts;

    public MessageAttempt(byte[] messageData, String roomId, String recipientParticipantId, int maxAttempts) {
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("maxAttempts must be initialized to a positive integer");
        }

        this.messageData = messageData;
        this.roomId = roomId;
        this.recipientParticipantId = recipientParticipantId;
        this.maxAttempts = maxAttempts;
        this.attempts = 0;
    }

    public byte[] getMessageData() {
        return messageData;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRecipientParticipantId() {
        return recipientParticipantId;
    }

    public boolean shouldTry() {
        return attempts < maxAttempts;
    }

    public void stopAttempting() {
        this.attempts = maxAttempts + 1;
    }

    public void incrementAttempts() {
        attempts++;
    }
}
package com.android.BloodBank.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String timestamp;
    private String messageSeenStatus;

    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String timestamp, String seenStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
        this.messageSeenStatus = seenStatus;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageSeenStatus() {
        return messageSeenStatus;
    }

    public void setMessageSeenStatus(String messageSeenStatus) {
        this.messageSeenStatus = messageSeenStatus;
    }
}

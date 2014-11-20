package com.github.subtitles.view;

public class ChatMessageModel {
    private String message = "";
    private boolean userMessage = true;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUserMessage() {
        return userMessage;
    }

    public void setUserMessage(boolean userMessage) {
        this.userMessage = userMessage;
    }
}

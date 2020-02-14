package com.bignerdranch.android.suicidepreventionapp.Models;

public class ChatMessage {
    private String chatMessage;
    private String sender;

    public ChatMessage(String chatMessage, String sender) {
        this.chatMessage = chatMessage;
        this.sender = sender;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

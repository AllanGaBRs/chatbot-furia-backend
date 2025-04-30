package com.furia.chatbot.model;

import java.util.List;
import java.util.Map;

public class Message {

    private String sender;
    private String text;
    private List<Map<String,Object>> players;

    public Message() {
    }

    public Message(String sender, String text, List<Map<String, Object>> players) {
        this.sender = sender;
        this.text = text;
        this.players = players;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Map<String, Object>> getPlayers() {
        return players;
    }
}

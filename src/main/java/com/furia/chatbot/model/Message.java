package com.furia.chatbot.model;

import java.util.List;
import java.util.Map;

public class Message {

    private String sender;
    private String text;
    private List<Map<String,Object>> players;
    private List<Map<String,Object>> matches;

    public Message() {
    }

    public Message(String sender, String text, List<Map<String, Object>> players, List<Map<String, Object>> matches) {
        this.sender = sender;
        this.text = text;
        this.players = players;
        this.matches = matches;
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

    public List<Map<String, Object>> getMatches() {
        return matches;
    }
}

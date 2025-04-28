package com.furia.chatbot.service;

import com.furia.chatbot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private FuriaService furiaService;

    private Map<String, Function<String, String>> commandMap;

    public ChatService() {
        commandMap = new HashMap<>();

        commandMap.put("olá", this::handleGreeting);
        commandMap.put("oi", this::handleGreeting);
        commandMap.put("furia", this::handleFuria);
        commandMap.put("tchau", this::handleGoodbye);
        commandMap.put("joagdores", this::handlePlayers);
        commandMap.put("novidades", this::handleNews);
    }

    public Message getBotResponse(Message userMessage) {
        String userText = userMessage.getText().toLowerCase();

        for (Map.Entry<String, Function<String, String>> entry : commandMap.entrySet()) {
            if (userText.contains(entry.getKey())) {
                String botReply = entry.getValue().apply(userText);
                return new Message("bot", botReply);
            }
        }

        return new Message("bot", "Desculpe, não entendi sua pergunta. Pode reformular?");
    }

    private String handleGreeting(String userText) {
        return "Olá! Como posso ajudar você hoje?";
    }

    private String handleFuria(String userText) {
        return "Ah, a FURIA! Um grande time! Como posso ajudar com informações sobre a FURIA?";
    }

    private String handleGoodbye(String userText) {
        return "Tchau! Até logo!";
    }

    private String handlePlayers(String userText) {
        List<String> nicknames = furiaService.getNicknames();
        return buildPlayerListResponse(nicknames);
    }

    private String handleNews(String userText) {
        List<Map<String, Object>> news = furiaService.getInfo();
        return formatNewsResponse(news);
    }

    private String buildPlayerListResponse(List<String> nicknames) {
        if (nicknames.isEmpty()) {
            return "Atualmente, não há jogadores da FURIA disponíveis.";
        }
        return "Jogadores da FURIA: " + String.join(", ", nicknames);
    }

    private String formatNewsResponse(List<Map<String, Object>> news) {
        if (news.isEmpty()) {
            return "Não há novidades no momento.";
        }

        List<String> response = news.stream()
                .flatMap(map -> map.values().stream())
                .map(Object::toString)
                .collect(Collectors.toList());

        return String.join("<br>", response);
    }
}

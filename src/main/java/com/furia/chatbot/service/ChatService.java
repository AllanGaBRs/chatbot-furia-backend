package com.furia.chatbot.service;

import com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer;
import com.furia.chatbot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

        commandMap.put("furia", this::handleFuria);
        commandMap.put("tchau", this::handleGoodbye);
        commandMap.put("nicknames", this::handlePlayers);
        commandMap.put("novidades", this::handleNews);
    }

    public Message getBotResponse(Message userMessage) {
        String userText = userMessage.getText().toLowerCase();

        if(userText.contains("jogadores")){
            List<Map<String, Object>> players = furiaService.getPlayerInfo();
            return new Message("bot", "Aqui estão os jogadores: ", players);
        }

        for (Map.Entry<String, Function<String, String>> entry : commandMap.entrySet()) {
            if (userText.contains(entry.getKey())) {
                String botReply = entry.getValue().apply(userText);

                return new Message("bot", botReply, null);
            }
        }

        return new Message("bot", "Desculpe, não entendi sua pergunta. Pode reformular?", null);
    }

    private String handleFuria(String userText) {
        return "VAIII FURIAAAA!";
    }

    private String handleGoodbye(String userText) {
        return "Tchau! Até logo!";
    }

    private String handlePlayers(String userText) {
        List<String> players = furiaService.getNicknames();
        return buildPlayerListResponse(players);
    }

    private String handleNews(String userText) {
        List<Map<String, Object>> news = furiaService.getInfo();
        return formatResponse(news);
    }

    private String buildPlayerListResponse(List<String> nicknames) {
        if (nicknames.isEmpty()) {
            return "Atualmente, não há jogadores da FURIA disponíveis.";
        }
        return "Jogadores da FURIA: " + String.join(", ", nicknames);
    }

    private String formatResponse(List<Map<String, Object>> news) {
        if (news.isEmpty()) {
            return "Sem dados no momento.";
        }

        List<String> response = news.stream()
                .flatMap(map -> map.values().stream())
                .map(Object::toString)
                .collect(Collectors.toList());

        return String.join("<br>", response);
    }
}

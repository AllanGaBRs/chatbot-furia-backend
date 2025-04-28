package com.furia.chatbot.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FuriaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://hltv-api.vercel.app/api";

    public List<Map<String, Object>> getInfo() {
        String url = BASE_URL + "/news.json";
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<>() {
                });
        List<Map<String, Object>> news = response.getBody();
        return news.stream()
                .filter(n -> {
                    String title = (String) n.get("title");
                    return title != null && title.contains("FURIA");
                }).collect(Collectors.toList());
    }

    public List<String> getNicknames() {
        String url = BASE_URL + "/player.json";

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<>() {
                });

        List<Map<String, Object>> players = response.getBody();

        return players.stream()
                .filter(player -> "FURIA".equalsIgnoreCase((String) player.get("name")))
                .flatMap(player -> {
                    List<Map<String, Object>> playerList = (List<Map<String, Object>>) player.get("players");
                    return playerList.stream()
                            .map(p -> (String) p.get("nickname"));
                })
                .collect(Collectors.toList());
    }
}

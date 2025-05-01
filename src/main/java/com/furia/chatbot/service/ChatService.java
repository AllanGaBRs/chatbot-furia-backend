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

    private final String options = "\"nicknames\", \"novidades\", \"jogadores\", \"partidas\", \"furia\"";

    public ChatService() {
        commandMap = new HashMap<>();

        commandMap.put("furia", this::handleFuria);
        commandMap.put("nicknames", this::handlePlayers);
        commandMap.put("novidades", this::handleNews);

    }

    public Message getBotResponse(Message userMessage) {
        String userText = userMessage.getText().toLowerCase();

        Message greetingResponse = handleGreetings(userText);
        if (greetingResponse != null) {
            return greetingResponse;
        }
        if(userText.contains("jogadores")){
            List<Map<String, Object>> players = furiaService.getPlayerInfo();
            return new Message("bot", "Aqui os brabooos ", players, null);
        }
        if(userText.contains("partidas")){
            List<Map<String, Object>> matches = furiaService.getFutureMatches();
            return new Message("bot", "Proxímas partidas que a furia vai arrebentaar 💪💪 ", null, matches);
        }

        for (Map.Entry<String, Function<String, String>> entry : commandMap.entrySet()) {
            if (userText.contains(entry.getKey())) {
                String botReply = entry.getValue().apply(userText);

                return new Message("bot", botReply, null, null);
            }
        }

        return new Message("bot", "Foi mal, mas não entendi!🤣 digita uma dessas que eu te mando sem enrolação! " + options, null, null);
    }

    private String handleFuria(String userText) {
        return "Uma organização de esports que nasceu do desejo de " +
                "representar o Brasil no CS e conquistou muito mais que isso: " +
                "expandimos nossas ligas, disputamos os principais títulos, " +
                "adotamos novos objetivos e ganhamos um propósito maior. " +
                "Somos muito mais que o sucesso competitivo. " +
                "Somos um movimento sociocultural!! " +
                "VAI FURIIAAAAAAA!!!";
    }


    private String handlePlayers(String userText) {
        List<String> players = furiaService.getNicknames();
        return buildPlayerListResponse(players);
    }

    private String handleNews(String userText) {
        List<Map<String, Object>> news = furiaService.getInfo();
        return "Vou mandar em inglês mesmo pq sei que tu é craque!! pega as boas ai 🤪 <br><br>" + formatResponse(news);
    }
    private Message handleGreetings(String userText) {
        userText = userText.toLowerCase().trim();

        if (userText.matches(".*\\b(fala+|opa+|ae+|eae+|eai+|yo+|hey+)\\b.*")) {
            return new Message("bot", "Eae, firmeza? 👊 Qual vai ser a boa? " + options, null, null);
        }

        if (userText.matches(".*\\b(oi+|ol[aá]+|bom dia+|boa tarde+|boa noite+|al[oóô]+|alo+)\\b.*")) {
            return new Message("bot", "Olá! Como posso te ajudar hoje? "  + options, null, null);
        }

        if (userText.matches(".*\\b(salve+|saaalve+|to na [áa]rea+|cheguei+|presente+)\\b.*")) {
            return new Message("bot", "Chegou o brabo, manda aí o que tá querendo 😎 " + options, null, null);
        }

        if (userText.matches(".*\\b(tudo bem+|td bem+|como vai+|como vc ta+|como c[êe]+ ta+)\\b.*")) {
            return new Message("bot", "Tudo certo por aqui! E você, tranquilo? O quer que eu te mostre? "  + options, null, null);
        }

        if(userText.matches(".*\\b(valeu|vlw|obrigado|obrigada|tmj|agradecid[ao]|gratid[aã]o|muito obrigado|brigad[ao]|tks|thanks)\\b.*")){
            return new Message("bot", "Tamo Junto, se precisar de mais alguma coisa so digitar " + options, null, null);
        }

        if(userText.matches(".*\\b(falou|flw|tchau|xau|até mais|ate[é] logo|até breve|até|\\+1|fui|adeus|nos vemos|see you|bye|bye bye|t+e+ +m+a+i+s*)\\b.*")){
            return new Message("bot", "Flwww, mas cola mais por aqui, em breve teremos novidades!!", null, null);
        }

        return null;
    }


    private String buildPlayerListResponse(List<String> nicknames) {
        if (nicknames.isEmpty()) {
            return "No momento não há jogadores da FURIA disponíveis. Estou com problemas internos 😶";
        }
        return "Aqui o nick dos braboss, mas digita \"jogadores\" que te mando algumas coisas a mais 😎 <br> " + String.join(", ", nicknames);
    }

    private String formatResponse(List<Map<String, Object>> text) {
        if (text.isEmpty()) {
            return "Sem dados por agora! Quem sabe mais tarde ✌️";
        }

        List<String> response = text.stream()
                .flatMap(map -> map.values().stream())
                .map(Object::toString)
                .collect(Collectors.toList());

        return String.join("<br>", response);
    }
}

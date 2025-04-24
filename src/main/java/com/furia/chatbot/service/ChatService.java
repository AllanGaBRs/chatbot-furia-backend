package com.furia.chatbot.service;

import com.furia.chatbot.model.Message;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    public Message getBotResponse(Message userMessage) {
        String userText = userMessage.getText().toLowerCase();

        String botReply;

        if (userText.contains("olá") || userText.contains("oi")) {
            botReply = "Olá! Como posso ajudar você hoje?";
        } else if (userText.contains("furia")) {
            botReply = "A FURIA é uma organização de esports conhecida no Brasil!";
        } else if (userText.contains("tchau")) {
            botReply = "Tchau! Até logo!";
        } else {
            botReply = "Desculpe, não entendi sua pergunta. Pode reformular?";
        }

        return new Message("bot", botReply);
    }
}

package com.furia.chatbot.controller;

import com.furia.chatbot.model.Message;
import com.furia.chatbot.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatbotService;

    public ChatController(ChatService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping
    public Message sendMessage(@RequestBody Message userMessage) {
        return chatbotService.getBotResponse(userMessage);
    }
}

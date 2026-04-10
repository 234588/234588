package com.example.aiproject.controller;

import com.example.aiproject.model.ChatRequest;
import com.example.aiproject.model.ChatResponse;
import com.example.aiproject.service.AiChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final AiChatService aiChatService;

    public ChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String reply = aiChatService.chat(request.message());
        return new ChatResponse(reply);
    }
}

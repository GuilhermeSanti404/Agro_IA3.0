
package com.example.chat.controller;

import com.example.chat.service.OpenAiService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final OpenAiService openAiService;

    public ChatController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        String reply = openAiService.getChatCompletion(userMessage);
        return Map.of("reply", reply);
    }
}

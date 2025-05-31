
package com.example.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    public String getChatCompletion(String prompt) {
        WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .build();

        Map<String, Object> requestBody = Map.of(
            "model", "gpt-4o",
            "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        Mono<Map> responseMono = webClient.post()
            .uri("/chat/completions")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class);

        Map response = responseMono.block();
        if (response != null) {
            List choices = (List) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map firstChoice = (Map) choices.get(0);
                Map message = (Map) firstChoice.get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
        }
        return "Desculpe, não consegui responder.";
    }
}

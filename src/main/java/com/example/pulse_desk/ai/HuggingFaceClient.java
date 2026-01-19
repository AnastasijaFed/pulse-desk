package com.example.pulse_desk.ai;

import java.time.Duration;
import java.util.List;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HuggingFaceClient {

    private final RestClient restClient;
    private final String baseUrl;
    private final String model;

    public HuggingFaceClient(
            RestClient.Builder restClientBuilder,
            @Value("${huggingface.base-url}") String baseUrl,
            @Value("${huggingface.model}") String model,
            @Value("${huggingface.timeout-ms:8000}") long timeoutMs,
            @Value("${HF_TOKEN}") String hfToken
    ) {
        this.baseUrl = baseUrl;
        this.model = model;
        SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();
        rf.setConnectTimeout((int) Duration.ofMillis(timeoutMs).toMillis());
        rf.setReadTimeout((int) Duration.ofMillis(timeoutMs).toMillis());

        this.restClient = restClientBuilder
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + hfToken)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .requestFactory(rf)
            .build();
            System.out.println("HF TOKEN PRESENT = " + (hfToken != null && !hfToken.isBlank()));
                System.out.println("HF_TOKEN starts with: " + (hfToken == null ? "null" : hfToken.substring(0, Math.min(6, hfToken.length()))));
    }

    public String generateText(String prompt) {
        HfChatRequest body = new HfChatRequest(
                model,
                List.of(new HfChatRequest.Message("user", prompt)),
                0.0,
                600
        );
        HfChatResponse response = restClient.post()
            .uri("/chat/completions")
            .body(body)
            .retrieve()
            .body(HfChatResponse.class);

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
                throw new RuntimeException("Empty response from Hugging Face router");
        }

        HfChatResponse.Message msg = response.choices().get(0).message();
        if (msg == null || msg.content() == null || msg.content().isBlank()) {
                throw new RuntimeException("No message content from Hugging Face router");
        }

        return msg.content();

    }
}

package com.example.pulse_desk.ai;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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
    }

    public String generateText(String prompt) {
        HfRequest body = new HfRequest(
                prompt,
                Map.of(
                        "max_new_tokens", 220,
                        "temperature", 0.0,
                        "return_full_text", false
                )
        );

        List<HfGeneratedItem> response = restClient.post()
                .uri("/" + model)
                .body(body)
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<HfGeneratedItem>>() {});

        if (response == null || response.isEmpty() || response.get(0).generatedText() == null) {
            throw new RuntimeException("Empty response from Hugging Face");
        }

        return response.get(0).generatedText();
    }
}

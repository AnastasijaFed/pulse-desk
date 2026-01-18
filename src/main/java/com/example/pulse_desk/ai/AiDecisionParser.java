package com.example.pulse_desk.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class AiDecisionParser {

    private final ObjectMapper objectMapper;

    public AiDecisionParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AiTicketDecision parse(String raw) {
        String json = extractFirstJsonObject(raw);
        try {
            return objectMapper.readValue(json, AiTicketDecision.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI JSON: " + json, e);
        }
    }

    private String extractFirstJsonObject(String text) {
        if (text == null) throw new RuntimeException("AI output is null");

        int start = text.indexOf('{');
        if (start < 0) throw new RuntimeException("No JSON object found in AI output: " + text);

        int depth = 0;
        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') depth--;

            if (depth == 0) {
                return text.substring(start, i + 1).trim();
            }
        }
        throw new RuntimeException("Unclosed JSON object in AI output: " + text);
    }
}

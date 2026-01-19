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
        if (raw == null || raw.isBlank()) {
            throw new RuntimeException("AI output is empty");
        }

        String cleaned = raw
                .replaceAll("(?s)<think>.*?</think>", "")  // remove <think>...</think>
                .trim();

        String json = extractFirstJsonObject(cleaned);

        try {
            return objectMapper.readValue(json, AiTicketDecision.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI JSON: " + json, e);
        }
    }

        private String extractFirstJsonObject(String text) {
        int start = text.indexOf('{');
        if (start < 0) {
            throw new RuntimeException("No JSON object found in AI output: " + preview(text));
        }

        int depth = 0;
        boolean inString = false;
        boolean escaped = false;

        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);

            if (inString) {
                if (escaped) {
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            } else {
                if (c == '"') {
                    inString = true;
                    continue;
                }
            }

            if (c == '{') depth++;
            else if (c == '}') depth--;

            if (depth == 0) {
                return text.substring(start, i + 1).trim();
            }
        }


        throw new RuntimeException("Unclosed JSON object in AI output: " + preview(text));
    }

    private String preview(String text) {

        int max = 600;
        String t = text == null ? "null" : text.replaceAll("\\s+", " ").trim();
        return t.length() <= max ? t : t.substring(0, max) + "...(truncated)";
    }
}

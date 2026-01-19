package com.example.pulse_desk.ai;

public class PromptFactory {

    private PromptFactory() {}

    public static String ticketTriagePrompt(String commentText) {
        return """
        Return ONLY a single valid JSON object. No markdown. No explanations. No <think> tags.
        Do NOT include any text before or after the JSON.

        Schema:
        {
        "shouldCreateTicket": true|false,
        "title": "...",
        "category": "bug"|"feature"|"billing"|"account"|"other",
        "priority": "low"|"medium"|"high",
        "summary": "..."
        }

        Rules:
        - Compliments or vague positivity => shouldCreateTicket=false and use empty strings for other fields.
        - If shouldCreateTicket=false, set title/category/priority/summary to "".
        - Summary must be 1-2 short sentences.

        Comment:
        "%s"
        """.formatted(escapeForPrompt(commentText));
    }

    private static String escapeForPrompt(String input) {
        return input == null ? "" : input.replace("\"", "\\\"");
    }
}

package com.example.pulse_desk.ai;

public class PromptFactory {

    private PromptFactory() {}

    public static String ticketTriagePrompt(String commentText) {
        return """
            You are a support triage assistant.
            Analyze the user comment and decide if it should become a support ticket.

            Output ONLY valid JSON. No markdown. No extra text.

            JSON schema:
            {
            "shouldCreateTicket": boolean,
            "title": string,
            "category": "bug" | "feature" | "billing" | "account" | "other",
            "priority": "low" | "medium" | "high",
            "summary": string
            }

            Rules:
            - Compliments or vague positivity => shouldCreateTicket=false.
            - Clear problem, request for help, bug report, billing/account issue => shouldCreateTicket=true.
            - Keep title short (max 80 chars).
            - Keep summary max 2 sentences.

            Comment:
            "%s"
            """.formatted(escapeForPrompt(commentText));
    }

    private static String escapeForPrompt(String input) {
        return input == null ? "" : input.replace("\"", "\\\"");
    }
}

package com.example.pulse_desk.ai;

import org.springframework.stereotype.Service;

@Service
public class AiTriageService {

    private final HuggingFaceClient hfClient;
    private final AiDecisionParser parser;

    public AiTriageService(HuggingFaceClient hfClient, AiDecisionParser parser) {
        this.hfClient = hfClient;
        this.parser = parser;
    }

    public AiTicketDecision analyzeComment(String commentText) {
        String prompt = PromptFactory.ticketTriagePrompt(commentText);
        String generated = hfClient.generateText(prompt);
        return parser.parse(generated);
    }
}

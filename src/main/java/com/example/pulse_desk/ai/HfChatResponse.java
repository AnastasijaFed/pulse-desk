package com.example.pulse_desk.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HfChatResponse(
        List<Choice> choices
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(Message message) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Message(String role, String content) {}
}
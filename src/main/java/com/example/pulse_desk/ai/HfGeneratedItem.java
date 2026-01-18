package com.example.pulse_desk.ai;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HfGeneratedItem(
        @JsonProperty("generated_text") String generatedText
) {}


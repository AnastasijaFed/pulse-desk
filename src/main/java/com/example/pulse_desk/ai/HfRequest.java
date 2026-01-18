package com.example.pulse_desk.ai;

import java.util.Map;

public record HfRequest(
        String inputs,
        Map<String, Object> parameters
) {}

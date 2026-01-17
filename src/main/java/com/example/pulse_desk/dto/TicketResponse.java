package com.example.pulse_desk.dto;

import java.time.Instant;

public record TicketResponse(
        Long id,
        Long commentId,
        String title,
        String category,   
        String priority,   
        String summary,
        Instant createdAt
) {}


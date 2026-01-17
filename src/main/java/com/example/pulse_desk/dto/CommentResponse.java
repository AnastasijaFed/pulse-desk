package com.example.pulse_desk.dto;

import java.time.Instant;

import com.example.pulse_desk.model.CommentStatus;

public record CommentResponse (
    Long id,
    String content,
    Long userId,
    String status,
    Instant createdAt

){}

package com.example.pulse_desk.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.pulse_desk.repository.CommentRepository;
import com.example.pulse_desk.service.CommentService;
import com.example.pulse_desk.dto.CommentResponse;
import com.example.pulse_desk.model.Comment;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class CommentController {

    private final CommentService service;

    CommentController(CommentService service){
        this.service = service;
    }

    @GetMapping("/comments")
    public List<CommentResponse> getComments() {
        return service.getAllComments();
    }
    
    
}

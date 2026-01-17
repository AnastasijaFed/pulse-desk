package com.example.pulse_desk.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.pulse_desk.repository.CommentRepository;
import com.example.pulse_desk.service.CommentService;

import jakarta.validation.Valid;

import com.example.pulse_desk.dto.CommentResponse;
import com.example.pulse_desk.dto.CreateCommentRequest;
import com.example.pulse_desk.model.Comment;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @GetMapping("/comments/{id}")
    public CommentResponse getComment(@PathVariable Long id) {
        return service.getCommentById(id);
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentResponse> newComment(@Valid @RequestBody CreateCommentRequest request) {
        CommentResponse created = service.submitComment(request.content(), request.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }
    
    
    
    
}

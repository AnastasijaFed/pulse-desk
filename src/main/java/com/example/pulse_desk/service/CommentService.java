package com.example.pulse_desk.service;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pulse_desk.dto.CommentResponse;
import com.example.pulse_desk.model.Comment;
import com.example.pulse_desk.model.CommentStatus;
import com.example.pulse_desk.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository repository;

    public CommentService(CommentRepository repository){
        this.repository = repository;
    }

     public List<CommentResponse> getAllComments() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUserId(),
                comment.getStatus().name(),
                comment.getCreatedAt() 
        );
    }

    public CommentResponse submitComment(String content, Long userId){
        Comment comment = new Comment(content, userId, CommentStatus.RECEIVED);
        repository.save(comment);
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getUserId(), comment.getStatus().name(), comment.getCreatedAt());

    }



    
}

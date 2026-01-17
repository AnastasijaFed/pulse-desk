package com.example.pulse_desk.service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.pulse_desk.dto.CommentResponse;
import com.example.pulse_desk.exception.ResourceNotFoundException;
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

    public CommentResponse getCommentById(Long id){
        return repository.findById(id).map(this::toResponse).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
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
        Comment saved = repository.save(comment);
        return new CommentResponse(saved.getId(), saved.getContent(), saved.getUserId(), saved.getStatus().name(), saved.getCreatedAt());

    }





    
}

package com.example.pulse_desk.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pulse_desk.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}   


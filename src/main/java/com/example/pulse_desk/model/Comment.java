package com.example.pulse_desk.model;

import java.time.Instant;

import jakarta.persistence.*;


@Entity
@Table(name= "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content; 

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    protected Comment(){

    }

    public Comment(String content, Long userId, CommentStatus status){
        this.content = content;
        this.userId = userId;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public Long getId(){
        return id;
    }

    public String getContent(){
        return content;
    }
    
    public Long getUserId(){
        return userId;
    }

    public CommentStatus getStatus(){
        return status;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

   
    public void setStatus(CommentStatus status){
        this.status = status;
    }

 





    
}

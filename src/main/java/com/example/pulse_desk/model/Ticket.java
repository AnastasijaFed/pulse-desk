package com.example.pulse_desk.model;

import java.time.Instant;

import jakarta.persistence.*;



@Entity
@Table(name="tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority priority;

    @Column(nullable = false)
    private Long commentId;

    @Column(nullable = false)
    private Instant createdAt;

    protected Ticket(){}

    public Ticket(String title, String summary, TicketCategory category, TicketPriority priority, Long commentId){
        this.title = title;
        this.summary = summary;
        this.category = category;
        this.commentId = commentId;
        this.priority = priority;
        this.createdAt = Instant.now();
    }
    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getSummary(){
        return summary;
    }

    public TicketCategory getCategory(){
        return category;
    }

    public TicketPriority getPriority(){
        return priority;
    }

    public Long getCommentId(){
        return commentId;
    }
    
    public Instant getCreatedAt(){
        return createdAt;
    }
}

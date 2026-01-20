package com.example.pulse_desk.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pulse_desk.ai.AiTicketDecision;
import com.example.pulse_desk.ai.AiTriageService;
import com.example.pulse_desk.dto.CommentResponse;
import com.example.pulse_desk.exception.ResourceNotFoundException;
import com.example.pulse_desk.model.Comment;
import com.example.pulse_desk.model.CommentStatus;
import com.example.pulse_desk.model.TicketCategory;
import com.example.pulse_desk.model.TicketPriority;
import com.example.pulse_desk.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final AiTriageService aiService;
    private final TicketService ticketService;

    public CommentService(CommentRepository commentRepository, AiTriageService aiService, TicketService ticketService ){
        this.commentRepository = commentRepository;
        this.aiService = aiService;
        this.ticketService = ticketService;
    }

     public List<CommentResponse> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CommentResponse getCommentById(Long id){
        return commentRepository.findById(id).map(this::toResponse).orElseThrow(() -> new ResourceNotFoundException("No comment with id "+ id));
    }


    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getStatus().name(),
                comment.getCreatedAt() 
        );
    }

    public CommentResponse submitComment(String content){
        Comment comment = new Comment(content, CommentStatus.RECEIVED);
        commentRepository.save(comment);
        try {

            AiTicketDecision decision = aiService.analyzeComment(comment.getContent());
            if (decision.shouldCreateTicket()) {
                log.info("Creating ticket for comment {}", comment.getId());
                ticketService.createNewTicket(decision.title(), decision.summary(), TicketCategory.valueOf(decision.category().toUpperCase()), TicketPriority.valueOf(decision.priority().toUpperCase()), comment.getId());
                comment.setStatus(CommentStatus.TICKET_CREATED);
            }else{
                log.info("No ticket created for comment {}", comment.getId());
                comment.setStatus(CommentStatus.ANALYZED);
            }

        } catch (Exception ex) {
            log.error("AI analysis failed for comment {}", comment.getId(), ex);
            comment.setStatus(CommentStatus.ANALYSIS_FAILED);
        }

        commentRepository.save(comment);
        return toResponse(comment);

    }





    
}

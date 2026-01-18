package com.example.pulse_desk.service;
import com.example.pulse_desk.dto.TicketResponse;
import com.example.pulse_desk.exception.ResourceNotFoundException;
import com.example.pulse_desk.model.Ticket;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pulse_desk.repository.TicketRepository;

@Service
public class TicketService {
    private final TicketRepository repository;

    public TicketService(TicketRepository repository){
        this.repository = repository;
    }

      private TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getCommentId(),
                ticket.getTitle(),
                ticket.getCategory().name(),
                ticket.getPriority().name(),
                ticket.getSummary(),
                ticket.getCreatedAt() 
        );
    }

    public List<TicketResponse> getAllTickets(){
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public TicketResponse getTicket(Long id){
        return repository.findById(id).map(this::toResponse).orElseThrow(() -> new ResourceNotFoundException("No ticket with id " + id));
    }
}

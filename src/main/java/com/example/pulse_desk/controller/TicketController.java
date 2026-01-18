package com.example.pulse_desk.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.pulse_desk.dto.TicketResponse;
import com.example.pulse_desk.service.TicketService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService service;

    TicketController(TicketService service){
        this.service = service;
    }

    @GetMapping
    public List<TicketResponse> getTickets() {
        return service.getAllTickets();
    }
    
    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable Long id) {
        return service.getTicket(id);
    }
    
}

package com.example.pulse_desk.ai;

import com.example.pulse_desk.model.TicketCategory;
import com.example.pulse_desk.model.TicketPriority;



public record AITicketDecision(
        boolean shouldCreateTicket,
        String title,
        String category,   
        String priority,  
        String summary
) {}
package com.example.pulse_desk.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pulse_desk.model.Ticket;
public interface TicketRepository extends JpaRepository<Ticket, Long>{
    
}

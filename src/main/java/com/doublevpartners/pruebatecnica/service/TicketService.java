package com.doublevpartners.pruebatecnica.service;

import com.doublevpartners.pruebatecnica.dto.TicketDTO;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    TicketDTO createTicket(TicketDTO ticketDTO);
    TicketDTO updateTicket(UUID ticketId, TicketDTO ticketDTO);
    void deleteTicket(UUID ticketId);
    TicketDTO getTicketById(UUID ticketId);
    List<TicketDTO> getTicketsByFilters(String status, UUID userId);
}

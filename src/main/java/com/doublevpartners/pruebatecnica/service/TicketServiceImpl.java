package com.doublevpartners.pruebatecnica.service;

import com.doublevpartners.pruebatecnica.dto.TicketDTO;
import com.doublevpartners.pruebatecnica.exception.ResourceNotFoundException;
import com.doublevpartners.pruebatecnica.model.Status;
import com.doublevpartners.pruebatecnica.model.Ticket;
import com.doublevpartners.pruebatecnica.model.User;
import com.doublevpartners.pruebatecnica.repository.TicketRepository;
import com.doublevpartners.pruebatecnica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        User user = userRepository.findById(ticketDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID "
                        + ticketDTO.getUserId() + " no encontrado"));

        Ticket ticket = new Ticket();
        ticket.setUsuario(user);
        ticket.setFechaCreacion(LocalDateTime.now());
        ticket.setFechaActualizacion(LocalDateTime.now());
        ticket.setEstatus(Status.valueOf(ticketDTO.getEstatus()));

        ticketRepository.save(ticket);
        return entityToDTO(ticket);
    }

    @Override
    public TicketDTO updateTicket(UUID ticketId, TicketDTO ticketDTO) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket con ID "
                        + ticketId + " no encontrado"));

        if (ticketDTO.getEstatus() != null) {
            ticket.setEstatus(Status.valueOf(ticketDTO.getEstatus()));
        }
        ticket.setFechaActualizacion(LocalDateTime.now());
        ticketRepository.save(ticket);
        return entityToDTO(ticket);
    }

    @Override
    public void deleteTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket con ID "
                        + ticketId + " no encontrado"));
        ticketRepository.delete(ticket);
    }

    @Override
    public TicketDTO getTicketById(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket con ID "
                        + ticketId + " no encontrado"));
        return entityToDTO(ticket);
    }

    @Override
    public List<TicketDTO> getTicketsByFilters(String status, UUID userId) {
        if (status != null && userId != null) {
            return ticketRepository.findByEstatusAndUsuario_Id(Status.valueOf(status), userId)
                    .stream()
                    .map(this::entityToDTO)
                    .collect(Collectors.toList());
        } else if (status != null) {
            return ticketRepository.findByEstatus(Status.valueOf(status))
                    .stream()
                    .map(this::entityToDTO)
                    .collect(Collectors.toList());
        } else if (userId != null) {
            return ticketRepository.findByUsuario_Id(userId)
                    .stream()
                    .map(this::entityToDTO)
                    .collect(Collectors.toList());
        }

        return ticketRepository.findAll()
                .stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    private TicketDTO entityToDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getUsuario().getId(),
                ticket.getFechaCreacion(),
                ticket.getFechaActualizacion(),
                ticket.getEstatus().name()
        );
    }
}
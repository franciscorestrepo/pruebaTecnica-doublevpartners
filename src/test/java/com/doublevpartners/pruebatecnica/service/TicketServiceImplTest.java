package com.doublevpartners.pruebatecnica.service;

import com.doublevpartners.pruebatecnica.dto.TicketDTO;
import com.doublevpartners.pruebatecnica.exception.ResourceNotFoundException;
import com.doublevpartners.pruebatecnica.model.Status;
import com.doublevpartners.pruebatecnica.model.Ticket;
import com.doublevpartners.pruebatecnica.model.User;
import com.doublevpartners.pruebatecnica.repository.TicketRepository;
import com.doublevpartners.pruebatecnica.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User user;
    private Ticket ticket;
    private TicketDTO ticketDTO;
    private UUID userId;
    private UUID ticketId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        ticketId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setNombres("John");
        user.setApellidos("Doe");

        ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setUsuario(user);
        ticket.setFechaCreacion(LocalDateTime.of(2025, 1, 1, 12, 0));
        ticket.setFechaActualizacion(LocalDateTime.of(2025, 1, 1, 12, 0));
        ticket.setEstatus(Status.ABIERTO);
        ticketDTO = new TicketDTO(
                null,
                userId,
                LocalDateTime.of(2025, 1, 1, 12, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0),
                "ABIERTO"
        );
    }

    @Test
    void createTicket_ShouldCreateTicket_WhenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket t = invocation.getArgument(0);
            t.setId(ticketId);
            return t;
        });
        TicketDTO result = ticketService.createTicket(ticketDTO);
        assertNotNull(result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals("ABIERTO", result.getEstatus());
        verify(userRepository, times(1)).findById(userId);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void createTicket_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ticketService.createTicket(ticketDTO));
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void updateTicket_ShouldUpdateStatus_WhenTicketExists() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        TicketDTO updateDTO = new TicketDTO(
                null,
                userId,
                null,
                null,
                "CERRADO"
        );
        TicketDTO result = ticketService.updateTicket(ticketId, updateDTO);
        assertEquals("CERRADO", result.getEstatus());
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void updateTicket_ShouldThrowException_WhenTicketNotFound() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ticketService.updateTicket(ticketId, ticketDTO));
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void deleteTicket_ShouldDelete_WhenTicketExists() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        ticketService.deleteTicket(ticketId);
        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test
    void deleteTicket_ShouldThrowException_WhenTicketNotFound() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ticketService.deleteTicket(ticketId));
        verify(ticketRepository, never()).delete(any(Ticket.class));
    }

    @Test
    void getTicketById_ShouldReturnTicket_WhenFound() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        TicketDTO result = ticketService.getTicketById(ticketId);
        assertNotNull(result);
        assertEquals(ticketId, result.getId());
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void getTicketById_ShouldThrowException_WhenNotFound() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ticketService.getTicketById(ticketId));
    }

    @Test
    void getTicketsByFilters_ShouldReturnTickets_WhenStatusAndUserIdProvided() {
        String status = "ABIERTO";
        List<Ticket> tickets = Collections.singletonList(ticket);
        when(ticketRepository.findByEstatusAndUsuario_Id(Status.ABIERTO, userId)).thenReturn(tickets);
        List<TicketDTO> result = ticketService.getTicketsByFilters(status, userId);
        assertEquals(1, result.size());
        assertEquals(ticketId, result.get(0).getId());
        verify(ticketRepository, times(1))
                .findByEstatusAndUsuario_Id(Status.ABIERTO, userId);
    }

    @Test
    void getTicketsByFilters_ShouldReturnTickets_WhenOnlyStatus() {
        String status = "ABIERTO";
        List<Ticket> tickets = List.of(ticket);
        when(ticketRepository.findByEstatus(Status.ABIERTO)).thenReturn(tickets);
        List<TicketDTO> result = ticketService.getTicketsByFilters(status, null);
        assertEquals(1, result.size());
        assertEquals(ticketId, result.get(0).getId());
        verify(ticketRepository).findByEstatus(Status.ABIERTO);
    }

    @Test
    void getTicketsByFilters_ShouldReturnTickets_WhenOnlyUserId() {
        List<Ticket> tickets = Collections.singletonList(ticket);
        when(ticketRepository.findByUsuario_Id(userId)).thenReturn(tickets);
        List<TicketDTO> result = ticketService.getTicketsByFilters(null, userId);
        assertEquals(1, result.size());
        verify(ticketRepository).findByUsuario_Id(userId);
    }

    @Test
    void getTicketsByFilters_ShouldReturnAll_WhenNoFilters() {
        List<Ticket> allTickets = Collections.singletonList(ticket);
        when(ticketRepository.findAll()).thenReturn(allTickets);
        List<TicketDTO> result = ticketService.getTicketsByFilters(null, null);
        assertEquals(1, result.size());
        verify(ticketRepository).findAll();
    }
}

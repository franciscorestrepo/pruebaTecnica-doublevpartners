package com.doublevpartners.pruebatecnica.controller;

import com.doublevpartners.pruebatecnica.dto.TicketDTO;
import com.doublevpartners.pruebatecnica.security.JwtAuthFilter;
import com.doublevpartners.pruebatecnica.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TicketController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTicket_ShouldReturnOkAndTicketDTO() throws Exception {
        TicketDTO requestDTO = new TicketDTO(
                null,
                UUID.randomUUID(),
                LocalDateTime.of(2025, 1, 10, 10, 0),
                LocalDateTime.of(2025, 1, 10, 10, 0),
                "ABIERTO"
        );

        TicketDTO responseDTO = new TicketDTO(
                UUID.randomUUID(),
                requestDTO.getUserId(),
                requestDTO.getFechaCreacion(),
                requestDTO.getFechaActualizacion(),
                "ABIERTO"
        );

        BDDMockito.given(ticketService.createTicket(any(TicketDTO.class)))
                .willReturn(responseDTO);
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.estatus").value("ABIERTO"));
        verify(ticketService).createTicket(any(TicketDTO.class));
    }

    @Test
    void deleteTicket_ShouldReturnNoContent() throws Exception {
        UUID ticketId = UUID.randomUUID();
        mockMvc.perform(delete("/api/tickets/{id}", ticketId))
                .andExpect(status().isNoContent());
        verify(ticketService).deleteTicket(ticketId);
    }

    @Test
    void getTicketById_ShouldReturnOkAndTicket() throws Exception {
        UUID ticketId = UUID.randomUUID();
        TicketDTO responseDTO = new TicketDTO(
                ticketId,
                UUID.randomUUID(),
                LocalDateTime.of(2025, 3, 10, 10, 0),
                LocalDateTime.of(2025, 3, 10, 10, 0),
                "ABIERTO"
        );
        BDDMockito.given(ticketService.getTicketById(ticketId)).willReturn(responseDTO);
        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId.toString()))
                .andExpect(jsonPath("$.estatus").value("ABIERTO"));
        verify(ticketService).getTicketById(ticketId);
    }

    @Test
    void getTicketsByFilters_ShouldReturnOkAndList() throws Exception {
        TicketDTO dto1 = new TicketDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2025, 4, 10, 9, 0),
                LocalDateTime.of(2025, 4, 10, 9, 0),
                "ABIERTO"
        );

        BDDMockito.given(ticketService.getTicketsByFilters("ABIERTO", null))
                .willReturn(List.of(dto1));
        mockMvc.perform(get("/api/tickets")
                        .param("status", "ABIERTO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].estatus").value("ABIERTO"));
        verify(ticketService).getTicketsByFilters("ABIERTO", null);
    }

    @Test
    void getTicketsByFilters_ShouldReturnEmptyList() throws Exception {
        BDDMockito.given(ticketService.getTicketsByFilters(null, null))
                .willReturn(Collections.emptyList());
        mockMvc.perform(get("/api/tickets"))
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(ticketService).getTicketsByFilters(null, null);
    }
}


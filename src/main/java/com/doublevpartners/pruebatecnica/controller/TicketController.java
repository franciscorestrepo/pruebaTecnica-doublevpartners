package com.doublevpartners.pruebatecnica.controller;

import com.doublevpartners.pruebatecnica.dto.TicketDTO;
import com.doublevpartners.pruebatecnica.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Crear un ticket",
            description = "Este endpoint permite crear un nuevo ticket.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket creado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDTO.class)))
            })
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        TicketDTO created = ticketService.createTicket(ticketDTO);
        return ResponseEntity.ok(created);
    }


    @Operation(summary = "Actualizar un ticket",
            description = "Este endpoint permite actualizar un ticket existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket actualizado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDTO.class)))
            })
    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable UUID id, @RequestBody TicketDTO ticketDTO) {
        TicketDTO updated = ticketService.updateTicket(id, ticketDTO);
        return ResponseEntity.ok(updated);
    }


    @Operation(summary = "Eliminar un ticket",
            description = "Este endpoint permite eliminar un ticket por ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Ticket eliminado exitosamente")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Obtener un ticket por ID",
            description = "Este endpoint permite obtener un ticket por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
            })
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable UUID id) {
        TicketDTO ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }


    @Operation(summary = "Obtener tickets filtrados",
            description = "Este endpoint permite obtener tickets filtrados por estado o ID de usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tickets encontrados",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDTO.class)))
            })
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getTicketsByFilters(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID userId) {
        List<TicketDTO> tickets = ticketService.getTicketsByFilters(status, userId);
        return ResponseEntity.ok(tickets);
    }
}

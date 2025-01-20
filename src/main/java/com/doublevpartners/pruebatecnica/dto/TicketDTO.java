package com.doublevpartners.pruebatecnica.dto;


import lombok.Builder;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;
import java.util.UUID;



@NoArgsConstructor
@Builder
public class TicketDTO {

    private UUID id;


    private UUID userId;


    private LocalDateTime fechaCreacion;


    private LocalDateTime fechaActualizacion;


    private String estatus;

    public TicketDTO(UUID id, UUID userId, LocalDateTime fechaCreacion,
                     LocalDateTime fechaActualizacion, String estatus) {
        this.id = id;
        this.userId = userId;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.estatus = estatus;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}

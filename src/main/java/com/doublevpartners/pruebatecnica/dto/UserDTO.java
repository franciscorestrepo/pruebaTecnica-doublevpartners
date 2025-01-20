package com.doublevpartners.pruebatecnica.dto;


import lombok.Builder;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.UUID;



@NoArgsConstructor
@Builder
public class UserDTO {

    private UUID id;


    private String nombres;


    private String apellidos;

    private LocalDateTime fechaCreacion;


    private LocalDateTime fechaActualizacion;


    public UserDTO(UUID id, String nombres, String apellidos,
                   LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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



}

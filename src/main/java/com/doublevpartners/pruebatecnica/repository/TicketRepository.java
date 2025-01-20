package com.doublevpartners.pruebatecnica.repository;

import com.doublevpartners.pruebatecnica.model.Status;
import com.doublevpartners.pruebatecnica.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findByEstatus(Status estatus);
    List<Ticket> findByUsuario_Id(UUID userId);
    List<Ticket> findByEstatusAndUsuario_Id(Status estatus, UUID userId);

}

package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByProjetoId(UUID projetoId);
    List<Ticket> findByProjetoIdIn(List<UUID> projetoIds);
    List<Ticket> findByProjetoClienteId(UUID clienteId);
}

package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.TicketAnexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketAnexoRepository extends JpaRepository<TicketAnexo, UUID> {
    List<TicketAnexo> findByTicketId(UUID ticketId);
}

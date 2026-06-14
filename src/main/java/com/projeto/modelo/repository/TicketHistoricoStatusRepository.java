package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.TicketHistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TicketHistoricoStatusRepository extends JpaRepository<TicketHistoricoStatus, UUID> {
}

package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.ProjetoNotificacaoTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProjetoNotificacaoTicketRepository extends JpaRepository<ProjetoNotificacaoTicket, UUID> {
    java.util.List<ProjetoNotificacaoTicket> findByProjetoId(UUID projetoId);
    void deleteByProjetoId(UUID projetoId);
}

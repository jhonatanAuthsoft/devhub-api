package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {
    List<Notificacao> findByUsuarioIdAndLidaOrderByDataCriacaoDesc(UUID usuarioId, Boolean lida);
    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(UUID usuarioId);
}

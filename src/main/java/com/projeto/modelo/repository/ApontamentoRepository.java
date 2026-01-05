package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Apontamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ApontamentoRepository extends JpaRepository<Apontamento, UUID> {
    List<Apontamento> findByProjetoId(UUID projetoId);

    @Query("SELECT  COALESCE(SUM(a.horas), 0) FROM Apontamento a WHERE a.projeto.id = :projetoId")
    BigDecimal sumHorasByProjetoId(@Param("projetoId") UUID projetoId);
}

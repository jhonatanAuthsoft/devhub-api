package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.EquipeProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EquipeProjetoRepository extends JpaRepository<EquipeProjeto, UUID> {
    List<EquipeProjeto> findByProjetoId(UUID projetoId);

    @Query("SELECT ep FROM EquipeProjeto ep " +
           "WHERE (ep.usaSalarioFixo IS NULL OR ep.usaSalarioFixo = false) " +
           "AND ep.projeto.status = com.projeto.modelo.model.enums.StatusProjeto.EM_ANDAMENTO " +
           "AND NOT EXISTS (" +
           "   SELECT a FROM Apontamento a " +
           "   WHERE a.projeto = ep.projeto AND a.colaborador = ep.colaborador " +
           "   AND a.dataApontamento >= :dataLimite" +
           ")")
    List<EquipeProjeto> findColaboradoresSemApontamentoRecente(@Param("dataLimite") LocalDate dataLimite);
}

package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Apontamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApontamentoRepository extends JpaRepository<Apontamento, UUID>, JpaSpecificationExecutor<Apontamento> {

    List<Apontamento> findByProjetoId(UUID projetoId);
    
    List<Apontamento> findByDataApontamentoBetween(LocalDate dataInicio, LocalDate dataFim);
    
    List<Apontamento> findByProjetoIdAndDataApontamentoBetween(UUID projetoId, LocalDate dataInicio, LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(a.horas), 0) FROM Apontamento a WHERE a.projeto.id = :projetoId")
    BigDecimal sumHorasByProjetoId(@Param("projetoId") UUID projetoId);

    @Query("SELECT COALESCE(SUM(a.horas), 0) FROM Apontamento a WHERE a.projeto.id = :projetoId AND a.colaborador.id = :colaboradorId")
    BigDecimal sumHorasByProjetoIdAndColaboradorId(@Param("projetoId") UUID projetoId, @Param("colaboradorId") UUID colaboradorId);

    @Query("SELECT ep.horasPrevistas FROM EquipeProjeto ep WHERE ep.projeto.id = :projetoId AND ep.colaborador.id = :colaboradorId")
    BigDecimal findHorasPrevistasByProjetoAndColaborador(@Param("projetoId") UUID projetoId, @Param("colaboradorId") UUID colaboradorId);

    @Query("SELECT COALESCE(ep.usaSalarioFixo, false) FROM EquipeProjeto ep WHERE ep.projeto.id = :projetoId AND ep.colaborador.id = :colaboradorId")
    Boolean findUsaSalarioFixoByProjetoAndColaborador(@Param("projetoId") UUID projetoId, @Param("colaboradorId") UUID colaboradorId);

    @Query("SELECT a FROM Apontamento a WHERE " +
           "(:projetoId IS NULL OR a.projeto.id = :projetoId) AND " +
           "(:colaboradorId IS NULL OR a.colaborador.id = :colaboradorId) AND " +
           "(:dataInicio IS NULL OR a.dataApontamento >= :dataInicio) AND " +
           "(:dataFim IS NULL OR a.dataApontamento <= :dataFim)")
    List<Apontamento> findPorFiltros(@Param("projetoId") UUID projetoId, 
                                     @Param("colaboradorId") UUID colaboradorId, 
                                     @Param("dataInicio") LocalDate dataInicio, 
                                     @Param("dataFim") LocalDate dataFim);
}

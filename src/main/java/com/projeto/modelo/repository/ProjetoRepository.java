package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Projeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, UUID> {
    
    @Query("SELECT p FROM Projeto p WHERE " +
           "(:statuses IS NULL OR p.status IN :statuses) AND " +
           "(LOWER(p.titulo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.cliente.nome) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Projeto> buscarPorTermoEStatus(@Param("search") String search, @Param("statuses") List<com.projeto.modelo.model.enums.StatusProjeto> statuses, Pageable pageable);
    List<Projeto> findDistinctByEquipeColaboradorId(UUID colaboradorId);
    List<Projeto> findByTipoProjetoIn(List<com.projeto.modelo.model.enums.TipoProjeto> tipos);
    List<Projeto> findByProjetoOrigemIdAndTipoProjeto(UUID projetoOrigemId, com.projeto.modelo.model.enums.TipoProjeto tipoProjeto);
}

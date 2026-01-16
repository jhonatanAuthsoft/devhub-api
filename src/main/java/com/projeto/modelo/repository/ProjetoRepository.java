package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, UUID> {
    List<Projeto> findDistinctByEquipeColaboradorId(UUID colaboradorId);
    List<Projeto> findByTipoProjetoIn(List<com.projeto.modelo.model.enums.TipoProjeto> tipos);
}

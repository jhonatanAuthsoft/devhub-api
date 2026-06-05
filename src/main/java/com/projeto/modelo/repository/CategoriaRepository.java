package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import java.util.Optional;
import com.projeto.modelo.model.enums.TipoCategoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    List<Categoria> findByPaiIsNull();
    List<Categoria> findByPaiIsNullAndTipoIn(List<TipoCategoria> tipos);
    List<Categoria> findByTipoIn(List<TipoCategoria> tipos);
    Optional<Categoria> findByNomeIgnoreCase(String nome);
}

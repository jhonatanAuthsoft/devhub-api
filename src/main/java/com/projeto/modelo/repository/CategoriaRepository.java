package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    List<Categoria> findByPaiIsNull();
    Optional<Categoria> findByNomeIgnoreCase(String nome);
}

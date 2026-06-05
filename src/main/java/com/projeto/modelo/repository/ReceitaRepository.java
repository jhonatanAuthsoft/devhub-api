package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
import com.projeto.modelo.model.enums.StatusReceita;
import java.time.LocalDate;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, UUID> {
    List<Receita> findByRecorrenciaPaiId(UUID paiId);
    List<Receita> findByStatusAndDataVencimentoBefore(StatusReceita status, LocalDate data);
}

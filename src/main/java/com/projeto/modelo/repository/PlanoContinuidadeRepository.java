package com.projeto.modelo.repository;

import com.projeto.modelo.model.PlanoContinuidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlanoContinuidadeRepository extends JpaRepository<PlanoContinuidade, UUID> {
}

package com.projeto.modelo.service;

import com.projeto.modelo.model.PlanoContinuidade;

import java.util.List;
import java.util.UUID;

public interface PlanoContinuidadeService {
    List<PlanoContinuidade> findAll();
    PlanoContinuidade findById(UUID id);
    PlanoContinuidade create(PlanoContinuidade plano);
    PlanoContinuidade update(UUID id, PlanoContinuidade plano);
    void delete(UUID id);
}

package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.ContratoTemplate;
import com.projeto.modelo.model.enums.TipoProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContratoTemplateRepository extends JpaRepository<ContratoTemplate, Long> {
    Optional<ContratoTemplate> findByTipoProjeto(TipoProjeto tipoProjeto);
}

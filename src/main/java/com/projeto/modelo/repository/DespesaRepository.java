package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, UUID>, JpaSpecificationExecutor<Despesa> {
    List<Despesa> findByRecorrenciaPaiId(UUID recorrenciaPaiId);
    List<Despesa> findByRecorrenciaPaiIdAndParcelaNumeroGreaterThanEqual(UUID recorrenciaPaiId, Integer parcelaNumero);
    long countByCategoriaId(UUID categoriaId);
    long countByContaId(UUID contaId);
}

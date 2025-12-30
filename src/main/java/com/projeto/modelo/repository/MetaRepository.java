package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Meta;
import com.projeto.modelo.model.enums.CategoriaMeta;
import com.projeto.modelo.model.enums.TipoMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetaRepository extends JpaRepository<Meta, UUID> {
    Optional<Meta> findByAnoAndCategoriaAndTipoMeta(Integer ano, CategoriaMeta categoria, TipoMeta tipoMeta);
    List<Meta> findByAno(Integer ano);
}

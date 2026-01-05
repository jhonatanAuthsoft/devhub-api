package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.EquipeProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EquipeProjetoRepository extends JpaRepository<EquipeProjeto, UUID> {
    List<EquipeProjeto> findByProjetoId(UUID projetoId);
}

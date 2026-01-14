package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssinaturaRepository extends JpaRepository<Assinatura, UUID> {
    List<Assinatura> findByClienteId(UUID clienteId);
    List<Assinatura> findByServidorId(UUID servidorId);
}

package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, UUID> {
}

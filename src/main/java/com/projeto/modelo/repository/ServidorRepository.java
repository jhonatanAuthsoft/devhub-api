package com.projeto.modelo.repository;

import com.projeto.modelo.model.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServidorRepository extends JpaRepository<Servidor, UUID> {

    List<Servidor> findAllByOrderByNomeAsc();

    List<Servidor> findByStatus(Servidor.StatusServidor status);
}

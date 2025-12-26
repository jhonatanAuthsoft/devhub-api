package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Cliente;
import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    
    List<Cliente> findByStatus(ClienteStatus status);
    
    List<Cliente> findByTipoPessoa(TipoPessoa tipoPessoa);
    
    List<Cliente> findByStatusAndTipoPessoa(ClienteStatus status, TipoPessoa tipoPessoa);
    
    boolean existsByCpfCnpj(String cpfCnpj);
    
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}

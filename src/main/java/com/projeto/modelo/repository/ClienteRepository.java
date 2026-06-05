package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Cliente;
import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    
    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nome) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.emailPrincipal) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "c.cpfCnpj LIKE CONCAT('%', :search, '%')")
    Page<Cliente> buscarPorTermo(@Param("search") String search, Pageable pageable);
    
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    
    List<Cliente> findByStatus(ClienteStatus status);
    Page<Cliente> findByStatus(ClienteStatus status, Pageable pageable);
    
    List<Cliente> findByTipoPessoa(TipoPessoa tipoPessoa);
    Page<Cliente> findByTipoPessoa(TipoPessoa tipoPessoa, Pageable pageable);
    
    List<Cliente> findByStatusAndTipoPessoa(ClienteStatus status, TipoPessoa tipoPessoa);
    
    boolean existsByCpfCnpj(String cpfCnpj);
    
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}

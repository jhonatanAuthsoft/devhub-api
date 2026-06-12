package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.enums.TipoPessoaVinculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {
    
    @Query("SELECT p FROM Pessoa p WHERE " +
           "(:ativo IS NULL OR p.ativo = :ativo) AND " +
           "(LOWER(p.nome) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.cargo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.cliente.nome) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Pessoa> buscarPorTermoEAtivo(@Param("search") String search, @Param("ativo") Boolean ativo);
    
    List<Pessoa> findByAtivo(Boolean ativo);
    
    List<Pessoa> findByClienteId(UUID clienteId);
    
    List<Pessoa> findByClienteIdAndTipoPessoa(UUID clienteId, TipoPessoaVinculo tipoPessoa);
    
    List<Pessoa> findByTipoPessoa(TipoPessoaVinculo tipoPessoa);
    
    List<Pessoa> findByRecebeBoletoTrue();
    
    List<Pessoa> findByRecebeNfTrue();
    
    List<Pessoa> findByClienteIdAndRecebeBoletoTrue(UUID clienteId);
    
    List<Pessoa> findByClienteIdAndRecebeNfTrue(UUID clienteId);
}

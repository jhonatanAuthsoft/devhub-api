package com.projeto.modelo.repository;

import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.enums.TipoPessoaVinculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {
    
    List<Pessoa> findByClienteId(UUID clienteId);
    
    List<Pessoa> findByClienteIdAndTipoPessoa(UUID clienteId, TipoPessoaVinculo tipoPessoa);
    
    List<Pessoa> findByTipoPessoa(TipoPessoaVinculo tipoPessoa);
    
    List<Pessoa> findByRecebeBoletoTrue();
    
    List<Pessoa> findByRecebeNfTrue();
    
    List<Pessoa> findByClienteIdAndRecebeBoletoTrue(UUID clienteId);
    
    List<Pessoa> findByClienteIdAndRecebeNfTrue(UUID clienteId);
}

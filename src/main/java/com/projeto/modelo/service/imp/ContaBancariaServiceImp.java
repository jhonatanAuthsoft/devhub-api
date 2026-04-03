package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.request.ContaBancariaRequestDTO;
import com.projeto.modelo.controller.dto.response.ContaBancariaResponseDTO;
import com.projeto.modelo.model.entity.ContaBancaria;
import com.projeto.modelo.repository.ContaBancariaRepository;
import com.projeto.modelo.service.ContaBancariaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContaBancariaServiceImp implements ContaBancariaService {

    private final ContaBancariaRepository repository;

    @Override
    @Transactional
    public ContaBancariaResponseDTO criar(ContaBancariaRequestDTO dto) {
        ContaBancaria conta = ContaBancaria.builder()
                .nome(dto.getNome())
                .saldoAtual(dto.getSaldoInicial() != null ? dto.getSaldoInicial() : BigDecimal.ZERO)
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .emiteBoleto(dto.getEmiteBoleto() != null ? dto.getEmiteBoleto() : false)
                .build();
        return ContaBancariaResponseDTO.fromEntity(repository.save(conta));
    }

    @Override
    public ContaBancariaResponseDTO buscarPorId(UUID id) {
        ContaBancaria conta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
        return ContaBancariaResponseDTO.fromEntity(conta);
    }

    @Override
    public List<ContaBancariaResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(ContaBancariaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContaBancariaResponseDTO atualizar(UUID id, ContaBancariaRequestDTO dto) {
        ContaBancaria conta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
        conta.setNome(dto.getNome());
        if (dto.getAtivo() != null) conta.setAtivo(dto.getAtivo());
        if (dto.getEmiteBoleto() != null) conta.setEmiteBoleto(dto.getEmiteBoleto());
        return ContaBancariaResponseDTO.fromEntity(repository.save(conta));
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        ContaBancaria conta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
        repository.delete(conta);
    }

    @Override
    @Transactional
    public void atualizarSaldo(UUID contaId, BigDecimal valorAdicional) {
        ContaBancaria conta = repository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
        conta.setSaldoAtual(conta.getSaldoAtual().add(valorAdicional));
        repository.save(conta);
    }
}

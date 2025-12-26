package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.PessoaRequestDTO;
import com.projeto.modelo.controller.dto.response.PessoaResponseDTO;
import com.projeto.modelo.model.entity.Cliente;
import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.enums.TipoPessoaVinculo;
import com.projeto.modelo.repository.ClienteRepository;
import com.projeto.modelo.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public PessoaResponseDTO criar(PessoaRequestDTO dto) {
        // Validar cliente existe
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Validar campos obrigatórios por tipo
        if (dto.getTipoPessoa() == TipoPessoaVinculo.CONTATO) {
            if (dto.getCargo() == null || dto.getCargo().trim().isEmpty()) {
                throw new RuntimeException("Cargo é obrigatório para contatos");
            }
        }

        Pessoa pessoa = Pessoa.builder()
                .cliente(cliente)
                .tipoPessoa(dto.getTipoPessoa())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .cargo(dto.getCargo())
                .recebeBoleto(dto.getRecebeBoleto() != null ? dto.getRecebeBoleto() : false)
                .recebeNf(dto.getRecebeNf() != null ? dto.getRecebeNf() : false)
                .build();

        Pessoa saved = pessoaRepository.save(pessoa);
        return toResponseDTO(saved);
    }

    @Transactional
    public PessoaResponseDTO atualizar(UUID id, PessoaRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        // Validar campos obrigatórios por tipo
        if (dto.getTipoPessoa() == TipoPessoaVinculo.CONTATO) {
            if (dto.getCargo() == null || dto.getCargo().trim().isEmpty()) {
                throw new RuntimeException("Cargo é obrigatório para contatos");
            }
        }

        pessoa.setTipoPessoa(dto.getTipoPessoa());
        pessoa.setNome(dto.getNome());
        pessoa.setEmail(dto.getEmail());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setCargo(dto.getCargo());
        pessoa.setRecebeBoleto(dto.getRecebeBoleto() != null ? dto.getRecebeBoleto() : false);
        pessoa.setRecebeNf(dto.getRecebeNf() != null ? dto.getRecebeNf() : false);

        Pessoa updated = pessoaRepository.save(pessoa);
        return toResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public PessoaResponseDTO buscarPorId(UUID id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
        return toResponseDTO(pessoa);
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarTodas() {
        return pessoaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarPorCliente(UUID clienteId) {
        return pessoaRepository.findByClienteId(clienteId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarResponsaveisPorCliente(UUID clienteId) {
        return pessoaRepository.findByClienteIdAndTipoPessoa(clienteId, TipoPessoaVinculo.RESPONSAVEL).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarContatosPorCliente(UUID clienteId) {
        return pessoaRepository.findByClienteIdAndTipoPessoa(clienteId, TipoPessoaVinculo.CONTATO).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarPorTipo(TipoPessoaVinculo tipo) {
        return pessoaRepository.findByTipoPessoa(tipo).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(UUID id) {
        if (!pessoaRepository.existsById(id)) {
            throw new RuntimeException("Pessoa não encontrada");
        }
        pessoaRepository.deleteById(id);
    }

    private PessoaResponseDTO toResponseDTO(Pessoa pessoa) {
        return PessoaResponseDTO.builder()
                .id(pessoa.getId())
                .clienteId(pessoa.getCliente().getId())
                .tipoPessoa(pessoa.getTipoPessoa())
                .nome(pessoa.getNome())
                .email(pessoa.getEmail())
                .telefone(pessoa.getTelefone())
                .cargo(pessoa.getCargo())
                .recebeBoleto(pessoa.getRecebeBoleto())
                .recebeNf(pessoa.getRecebeNf())
                .dataCriacao(pessoa.getDataCriacao())
                .dataAtualizacao(pessoa.getDataAtualizacao())
                .build();
    }
}

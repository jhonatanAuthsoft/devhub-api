package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.ClienteRequestDTO;
import com.projeto.modelo.controller.dto.request.PessoaRequestDTO;
import com.projeto.modelo.controller.dto.response.ClienteResponseDTO;
import com.projeto.modelo.controller.dto.response.PessoaResponseDTO;
import com.projeto.modelo.model.entity.Cliente;
import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import com.projeto.modelo.model.enums.TipoPessoaVinculo;
import com.projeto.modelo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        // Validar CPF/CNPJ único
        if (clienteRepository.existsByCpfCnpj(dto.getCpfCnpj())) {
            throw new RuntimeException("CPF/CNPJ já cadastrado");
        }

        // Validar responsáveis para CNPJ
        if (dto.getTipoPessoa() == TipoPessoa.CNPJ && 
            (dto.getResponsaveis() == null || dto.getResponsaveis().isEmpty())) {
            throw new RuntimeException("Cliente CNPJ deve ter pelo menos um responsável");
        }

        Cliente cliente = Cliente.builder()
                .tipoPessoa(dto.getTipoPessoa())
                .cpfCnpj(dto.getCpfCnpj())
                .nome(dto.getNome())
                .emailPrincipal(dto.getEmailPrincipal())
                .telefone(dto.getTelefone())
                .status(dto.getStatus() != null ? dto.getStatus() : ClienteStatus.ATIVO)
                .observacao(dto.getObservacao())
                .logradouro(dto.getLogradouro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .build();

        // Adicionar responsáveis se for CNPJ
        if (dto.getTipoPessoa() == TipoPessoa.CNPJ && dto.getResponsaveis() != null) {
            for (PessoaRequestDTO responsavelDto : dto.getResponsaveis()) {
                Pessoa responsavel = Pessoa.builder()
                        .tipoPessoa(TipoPessoaVinculo.RESPONSAVEL)
                        .nome(responsavelDto.getNome())
                        .telefone(responsavelDto.getTelefone())
                        .email(responsavelDto.getEmail())
                        .build();
                cliente.addPessoa(responsavel);
            }
        }

        Cliente saved = clienteRepository.save(cliente);
        return toResponseDTO(saved);
    }

    @Transactional
    public ClienteResponseDTO atualizar(UUID id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Validar CPF/CNPJ único (exceto o próprio cliente)
        if (!cliente.getCpfCnpj().equals(dto.getCpfCnpj()) && 
            clienteRepository.existsByCpfCnpj(dto.getCpfCnpj())) {
            throw new RuntimeException("CPF/CNPJ já cadastrado");
        }

        cliente.setTipoPessoa(dto.getTipoPessoa());
        cliente.setCpfCnpj(dto.getCpfCnpj());
        cliente.setNome(dto.getNome());
        cliente.setEmailPrincipal(dto.getEmailPrincipal());
        cliente.setTelefone(dto.getTelefone());
        cliente.setStatus(dto.getStatus());
        cliente.setObservacao(dto.getObservacao());
        cliente.setLogradouro(dto.getLogradouro());
        cliente.setCidade(dto.getCidade());
        cliente.setEstado(dto.getEstado());
        cliente.setCep(dto.getCep());

        Cliente updated = clienteRepository.save(cliente);
        return toResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return toResponseDTO(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorCpfCnpj(String cpfCnpj) {
        Cliente cliente = clienteRepository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return toResponseDTO(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarPorStatus(ClienteStatus status) {
        return clienteRepository.findByStatus(status).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarPorTipo(TipoPessoa tipoPessoa) {
        return clienteRepository.findByTipoPessoa(tipoPessoa).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        List<PessoaResponseDTO> pessoas = cliente.getPessoas().stream()
                .map(this::pessoaToResponseDTO)
                .collect(Collectors.toList());

        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .tipoPessoa(cliente.getTipoPessoa())
                .cpfCnpj(cliente.getCpfCnpj())
                .nome(cliente.getNome())
                .emailPrincipal(cliente.getEmailPrincipal())
                .telefone(cliente.getTelefone())
                .status(cliente.getStatus())
                .observacao(cliente.getObservacao())
                .logradouro(cliente.getLogradouro())
                .cidade(cliente.getCidade())
                .estado(cliente.getEstado())
                .cep(cliente.getCep())
                .pessoas(pessoas)
                .dataCriacao(cliente.getDataCriacao())
                .dataAtualizacao(cliente.getDataAtualizacao())
                .build();
    }

    private PessoaResponseDTO pessoaToResponseDTO(Pessoa pessoa) {
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

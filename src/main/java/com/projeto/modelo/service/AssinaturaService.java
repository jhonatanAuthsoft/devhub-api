package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.response.ClienteResponseDTO;
import com.projeto.modelo.dto.AssinaturaRequest;
import com.projeto.modelo.dto.AssinaturaResponse;
import com.projeto.modelo.dto.ServidorResponse;
import com.projeto.modelo.model.Servidor;
import com.projeto.modelo.model.entity.Assinatura;
import com.projeto.modelo.model.entity.Cliente;
import com.projeto.modelo.repository.AssinaturaRepository;
import com.projeto.modelo.repository.ClienteRepository;
import com.projeto.modelo.repository.ServidorRepository;
import com.projeto.modelo.repository.PlanoContinuidadeRepository;
import com.projeto.modelo.model.PlanoContinuidade;
import com.projeto.modelo.controller.dto.request.CadastrarProjetoDTO;
import com.projeto.modelo.model.enums.StatusProjeto;
import com.projeto.modelo.model.enums.TipoProjeto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;
    private final ClienteRepository clienteRepository;
    private final ServidorRepository servidorRepository;
    private final PlanoContinuidadeRepository planoContinuidadeRepository;
    private final ProjetoService projetoService;

    @Transactional(readOnly = true)
    public List<AssinaturaResponse> findAll() {
        return assinaturaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssinaturaResponse create(AssinaturaRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        boolean hasServidor = request.getServidorId() != null;
        boolean hasPlano = request.getPlanoContinuidadeId() != null;

        if (hasServidor && hasPlano) {
            throw new RuntimeException("A assinatura deve ser para um Servidor OU um Plano de Continuidade, nunca ambos.");
        }
        if (!hasServidor && !hasPlano) {
            throw new RuntimeException("A assinatura deve especificar um Servidor ou um Plano de Continuidade.");
        }

        Assinatura assinatura = new Assinatura();
        assinatura.setCliente(cliente);
        assinatura.setDataInicio(request.getDataInicio());
        assinatura.setTipoPlano(Assinatura.TipoPlano.valueOf(request.getTipoPlano()));
        
        if (hasServidor) {
            Servidor servidor = servidorRepository.findById(request.getServidorId())
                    .orElseThrow(() -> new RuntimeException("Servidor não encontrado"));
            assinatura.setServidor(servidor);
            assinatura.setIdentificadorServidor(request.getIdentificadorServidor());
            if (request.getValorMensal() != null) {
                assinatura.setValorMensal(request.getValorMensal());
            } else {
                switch (assinatura.getTipoPlano()) {
                    case BIANUAL -> assinatura.setValorMensal(servidor.getPlanoDoisAnosValor());
                    case ANUAL -> assinatura.setValorMensal(servidor.getPlanoUmAnoValor());
                    case MENSAL -> assinatura.setValorMensal(servidor.getPlanoSemFidelidadeValor());
                }
            }
        } else {
            PlanoContinuidade plano = planoContinuidadeRepository.findById(request.getPlanoContinuidadeId())
                    .orElseThrow(() -> new RuntimeException("Plano de Continuidade não encontrado"));
            assinatura.setPlanoContinuidade(plano);
            
            if (request.getValorMensal() != null) {
                assinatura.setValorMensal(request.getValorMensal());
            } else {
                switch (assinatura.getTipoPlano()) {
                    case BIANUAL -> assinatura.setValorMensal(plano.getPrecoDoisAnos());
                    case ANUAL -> assinatura.setValorMensal(plano.getPrecoUmAno());
                    case MENSAL -> assinatura.setValorMensal(plano.getPrecoSemFidelidade());
                }
            }

            // Gerar Projeto
            CadastrarProjetoDTO dto = new CadastrarProjetoDTO(
                "Plano de Continuidade: " + plano.getNome(),
                plano.getDescricaoDestino(),
                "",
                cliente.getId(),
                null,
                request.getDataInicio(),
                null,
                null,
                TipoProjeto.SUSTENTACAO,
                null,
                null,
                null,
                assinatura.getValorMensal(),
                assinatura.getValorMensal(),
                null,
                1,
                false,
                true,
                null,
                StatusProjeto.EM_ANDAMENTO,
                null,
                null,
                null,
                null
            );
            var projetoResponse = projetoService.cadastrarProjeto(dto);
            assinatura.setProjetoId(projetoResponse.id());
        }

        // Calcular vigência
        calculateVigencia(assinatura);

        assinatura.setStatus(Assinatura.StatusAssinatura.ATIVA);
        
        Assinatura saved = assinaturaRepository.save(assinatura);
        return toResponse(saved);
    }

    private void calculateVigencia(Assinatura assinatura) {
        LocalDate inicio = assinatura.getDataInicio();
        switch (assinatura.getTipoPlano()) {
            case MENSAL -> assinatura.setDataFim(inicio.plusMonths(1));
            case ANUAL -> assinatura.setDataFim(inicio.plusYears(1));
            case BIANUAL -> assinatura.setDataFim(inicio.plusYears(2));
        }
    }

    private AssinaturaResponse toResponse(Assinatura assinatura) {
        AssinaturaResponse response = new AssinaturaResponse();
        response.setId(assinatura.getId());
        
        // Mapea Cliente de forma simplificada ou usando mapper existente se houver
        // Por hora, manual simplificado para evitar dependências circulares complexas
        ClienteResponseDTO clienteDTO = new ClienteResponseDTO();
        clienteDTO.setId(assinatura.getCliente().getId());
        clienteDTO.setNome(assinatura.getCliente().getNome());
        // ... outros campos se necessário
        response.setCliente(clienteDTO);

        // Mapea Servidor ou Plano Continuidade
        if (assinatura.getServidor() != null) {
            ServidorResponse servidorDTO = new ServidorResponse();
            servidorDTO.setId(assinatura.getServidor().getId());
            servidorDTO.setNome(assinatura.getServidor().getNome());
            response.setServidor(servidorDTO);
            response.setIdentificadorServidor(assinatura.getIdentificadorServidor());
        }

        if (assinatura.getPlanoContinuidade() != null) {
            response.setPlanoContinuidade(assinatura.getPlanoContinuidade());
            response.setProjetoId(assinatura.getProjetoId());
        }
        response.setDataInicio(assinatura.getDataInicio());
        response.setDataFim(assinatura.getDataFim());
        response.setTipoPlano(assinatura.getTipoPlano().name());
        response.setValorMensal(assinatura.getValorMensal());
        response.setStatus(assinatura.getStatus().name());
        response.setCreatedAt(assinatura.getCreatedAt());
        response.setUpdatedAt(assinatura.getUpdatedAt());
        
        return response;
    }
}

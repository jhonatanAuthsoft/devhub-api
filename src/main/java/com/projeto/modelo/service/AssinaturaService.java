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

        Servidor servidor = servidorRepository.findById(request.getServidorId())
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado"));

        Assinatura assinatura = new Assinatura();
        assinatura.setCliente(cliente);
        assinatura.setServidor(servidor);
        assinatura.setDataInicio(request.getDataInicio());
        assinatura.setTipoPlano(Assinatura.TipoPlano.valueOf(request.getTipoPlano()));
        
        // Calcular vigência
        calculateVigencia(assinatura);
        
        // Definir valor com base no plano (snapshot do preço atual)
        // Se o request vier com valor explícito, usamos ele (override manual), caso contrário, pegamos do servidor
        if (request.getValorMensal() != null) {
            assinatura.setValorMensal(request.getValorMensal());
        } else {
            switch (assinatura.getTipoPlano()) {
                case BIANUAL -> assinatura.setValorMensal(servidor.getPlanoDoisAnosValor());
                case ANUAL -> assinatura.setValorMensal(servidor.getPlanoUmAnoValor());
                case MENSAL -> assinatura.setValorMensal(servidor.getPlanoSemFidelidadeValor());
            }
        }

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

        // Mapea Servidor
        ServidorResponse servidorDTO = new ServidorResponse();
        servidorDTO.setId(assinatura.getServidor().getId());
        servidorDTO.setNome(assinatura.getServidor().getNome());
        // ... outros campos se necessário
        response.setServidor(servidorDTO);

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

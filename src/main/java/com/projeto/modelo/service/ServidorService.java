package com.projeto.modelo.service;

import com.projeto.modelo.dto.ServidorRequest;
import com.projeto.modelo.dto.ServidorResponse;
import com.projeto.modelo.model.Servidor;
import com.projeto.modelo.repository.ServidorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServidorService {

    private final ServidorRepository servidorRepository;

    @Transactional(readOnly = true)
    public List<ServidorResponse> findAll() {
        return servidorRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServidorResponse findById(UUID id) {
        Servidor servidor = servidorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado com ID: " + id));
        return toResponse(servidor);
    }

    @Transactional
    public ServidorResponse create(ServidorRequest request) {
        Servidor servidor = toEntity(request);
        Servidor saved = servidorRepository.save(servidor);
        return toResponse(saved);
    }

    @Transactional
    public ServidorResponse update(UUID id, ServidorRequest request) {
        Servidor servidor = servidorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado com ID: " + id));

        updateEntityFromRequest(servidor, request);
        Servidor updated = servidorRepository.save(servidor);
        return toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        if (!servidorRepository.existsById(id)) {
            throw new RuntimeException("Servidor não encontrado com ID: " + id);
        }
        servidorRepository.deleteById(id);
    }

    // Métodos de conversão
    private Servidor toEntity(ServidorRequest request) {
        Servidor servidor = new Servidor();
        servidor.setNome(request.getNome());
        servidor.setDescricao(request.getDescricao());
        servidor.setStatus(Servidor.StatusServidor.valueOf(request.getStatus()));
        servidor.setCpuNucleos(request.getCpuNucleos());
        servidor.setRamGb(request.getRamGb());
        servidor.setArmazenamentoSsdGb(request.getArmazenamentoSsdGb());
        servidor.setLarguraBandaTb(request.getLarguraBandaTb());
        servidor.setMonitoramentoProativo(request.getMonitoramentoProativo());
        servidor.setBackupDiario(request.getBackupDiario());
        servidor.setGestaoSeguranca(request.getGestaoSeguranca());
        servidor.setSuporteEspecializado(request.getSuporteEspecializado());
        servidor.setSlaTempoRespostaHoras(request.getSlaTempoRespostaHoras());
        servidor.setPlanoDoisAnosValor(request.getPlanoDoisAnosValor());
        servidor.setPlanoUmAnoValor(request.getPlanoUmAnoValor());
        // ... existing fields ...
        servidor.setPlanoSemFidelidadeValor(request.getPlanoSemFidelidadeValor());
        servidor.setCustoServidor(request.getCustoServidor());
        servidor.setCustoBackupDiario(request.getCustoBackupDiario());
        return servidor;
    }

    private ServidorResponse toResponse(Servidor servidor) {
        ServidorResponse response = new ServidorResponse();
        response.setId(servidor.getId());
        response.setNome(servidor.getNome());
        response.setDescricao(servidor.getDescricao());
        response.setStatus(servidor.getStatus().name());
        response.setCpuNucleos(servidor.getCpuNucleos());
        response.setRamGb(servidor.getRamGb());
        response.setArmazenamentoSsdGb(servidor.getArmazenamentoSsdGb());
        response.setLarguraBandaTb(servidor.getLarguraBandaTb());
        response.setMonitoramentoProativo(servidor.getMonitoramentoProativo());
        response.setBackupDiario(servidor.getBackupDiario());
        response.setGestaoSeguranca(servidor.getGestaoSeguranca());
        response.setSuporteEspecializado(servidor.getSuporteEspecializado());
        response.setSlaTempoRespostaHoras(servidor.getSlaTempoRespostaHoras());
        response.setPlanoDoisAnosValor(servidor.getPlanoDoisAnosValor());
        response.setPlanoUmAnoValor(servidor.getPlanoUmAnoValor());
        response.setPlanoSemFidelidadeValor(servidor.getPlanoSemFidelidadeValor());
        response.setCustoServidor(servidor.getCustoServidor());
        response.setCustoBackupDiario(servidor.getCustoBackupDiario());
        response.setCreatedAt(servidor.getCreatedAt());
        response.setUpdatedAt(servidor.getUpdatedAt());
        return response;
    }

    private void updateEntityFromRequest(Servidor servidor, ServidorRequest request) {
        servidor.setNome(request.getNome());
        servidor.setDescricao(request.getDescricao());
        servidor.setStatus(Servidor.StatusServidor.valueOf(request.getStatus()));
        servidor.setCpuNucleos(request.getCpuNucleos());
        servidor.setRamGb(request.getRamGb());
        servidor.setArmazenamentoSsdGb(request.getArmazenamentoSsdGb());
        servidor.setLarguraBandaTb(request.getLarguraBandaTb());
        servidor.setMonitoramentoProativo(request.getMonitoramentoProativo());
        servidor.setBackupDiario(request.getBackupDiario());
        servidor.setGestaoSeguranca(request.getGestaoSeguranca());
        servidor.setSuporteEspecializado(request.getSuporteEspecializado());
        servidor.setSlaTempoRespostaHoras(request.getSlaTempoRespostaHoras());
        servidor.setPlanoDoisAnosValor(request.getPlanoDoisAnosValor());
        servidor.setPlanoUmAnoValor(request.getPlanoUmAnoValor());
        servidor.setPlanoSemFidelidadeValor(request.getPlanoSemFidelidadeValor());
        servidor.setCustoServidor(request.getCustoServidor());
        servidor.setCustoBackupDiario(request.getCustoBackupDiario());
    }
}

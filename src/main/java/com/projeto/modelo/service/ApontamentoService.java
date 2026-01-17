package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.CadastrarApontamentoDTO;
import com.projeto.modelo.controller.dto.response.ApontamentoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ApontamentoService {
    ApontamentoResponseDTO cadastrar(CadastrarApontamentoDTO dto);
    List<ApontamentoResponseDTO> listarPorProjeto(UUID projetoId, java.time.LocalDate dataInicio, java.time.LocalDate dataFim, UUID colaboradorId);
}

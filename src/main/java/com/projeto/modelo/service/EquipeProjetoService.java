package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.AtualizarHorasDTO;
import com.projeto.modelo.controller.dto.response.AlocacaoHorasResponseDTO;
import java.util.List;
import java.util.UUID;

public interface EquipeProjetoService {
    List<AlocacaoHorasResponseDTO> listarAlocacao(UUID projetoId);
    void atualizarHoras(UUID equipeId, AtualizarHorasDTO dto);
}

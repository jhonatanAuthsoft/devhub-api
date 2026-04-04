package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.EstornarDespesaDTO;
import com.projeto.modelo.controller.dto.request.PagarDespesaDTO;
import com.projeto.modelo.controller.dto.request.DespesaRequestDTO;
import com.projeto.modelo.controller.dto.response.DespesaResponseDTO;
import com.projeto.modelo.model.entity.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DespesaService {
    List<DespesaResponseDTO> criar(DespesaRequestDTO dto, Usuario usuarioLogado);
    DespesaResponseDTO buscarPorId(UUID id);
    List<DespesaResponseDTO> listarTodos(LocalDate dataInicio, LocalDate dataFim, UUID categoriaId);
    List<DespesaResponseDTO> atualizar(UUID id, DespesaRequestDTO dto, Usuario usuarioLogado);
    void deletar(UUID id, String escopoExclusao, Usuario usuarioLogado);
    DespesaResponseDTO pagar(UUID id, PagarDespesaDTO dto, Usuario usuarioLogado);
    List<DespesaResponseDTO> estornar(UUID id, EstornarDespesaDTO dto, Usuario usuarioLogado);
    void atualizarStatusAtraso();
}

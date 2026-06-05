package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.EstornarReceitaDTO;
import com.projeto.modelo.controller.dto.request.ReceberReceitaDTO;
import com.projeto.modelo.controller.dto.request.ReceitaRequestDTO;
import com.projeto.modelo.controller.dto.response.ReceitaResponseDTO;
import com.projeto.modelo.model.entity.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReceitaService {
    List<ReceitaResponseDTO> criar(ReceitaRequestDTO dto, Usuario usuarioLogado);
    ReceitaResponseDTO buscarPorId(UUID id);
    List<ReceitaResponseDTO> listarTodos(LocalDate dataInicio, LocalDate dataFim, UUID categoriaId);
    List<ReceitaResponseDTO> atualizar(UUID id, ReceitaRequestDTO dto, Usuario usuarioLogado);
    void deletar(UUID id, String escopoExclusao, Usuario usuarioLogado);
    ReceitaResponseDTO receber(UUID id, ReceberReceitaDTO dto, Usuario usuarioLogado);
    List<ReceitaResponseDTO> estornar(UUID id, EstornarReceitaDTO dto, Usuario usuarioLogado);
    void atualizarStatusAtraso();
}

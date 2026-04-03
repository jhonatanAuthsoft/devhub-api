package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.CategoriaRequestDTO;
import com.projeto.modelo.controller.dto.response.CategoriaResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CategoriaService {
    CategoriaResponseDTO criar(CategoriaRequestDTO dto);
    CategoriaResponseDTO buscarPorId(UUID id);
    List<CategoriaResponseDTO> listarTodos();
    List<CategoriaResponseDTO> listarRaizes();
    CategoriaResponseDTO atualizar(UUID id, CategoriaRequestDTO dto);
    void deletar(UUID id);
}

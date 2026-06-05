package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.CartaoCreditoRequestDTO;
import com.projeto.modelo.controller.dto.response.CartaoCreditoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CartaoCreditoService {
    CartaoCreditoResponseDTO salvar(CartaoCreditoRequestDTO dto);
    CartaoCreditoResponseDTO atualizar(UUID id, CartaoCreditoRequestDTO dto);
    CartaoCreditoResponseDTO buscarPorId(UUID id);
    List<CartaoCreditoResponseDTO> listarTodos();
    void deletar(UUID id);
}

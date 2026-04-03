package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.ContaBancariaRequestDTO;
import com.projeto.modelo.controller.dto.response.ContaBancariaResponseDTO;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

public interface ContaBancariaService {
    ContaBancariaResponseDTO criar(ContaBancariaRequestDTO dto);
    ContaBancariaResponseDTO buscarPorId(UUID id);
    List<ContaBancariaResponseDTO> listarTodos();
    ContaBancariaResponseDTO atualizar(UUID id, ContaBancariaRequestDTO dto);
    void deletar(UUID id);
    void atualizarSaldo(UUID contaId, BigDecimal valorAdicional);
}

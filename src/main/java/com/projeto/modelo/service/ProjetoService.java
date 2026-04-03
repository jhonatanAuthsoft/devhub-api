package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.CadastrarProjetoDTO;
import com.projeto.modelo.controller.dto.response.ProjetoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProjetoService {
    ProjetoResponseDTO cadastrarProjeto(CadastrarProjetoDTO dto);
    Page<ProjetoResponseDTO> listarProjetos(Pageable pageable);
    ProjetoResponseDTO buscarPorId(UUID id);
    ProjetoResponseDTO atualizarProjeto(UUID id, CadastrarProjetoDTO dto);
    List<ProjetoResponseDTO> listarProjetosPorColaborador(UUID colaboradorId);
    List<ProjetoResponseDTO> listarSMPsPorProjeto(UUID projetoOrigemId);
    void deletarProjeto(UUID id);
}

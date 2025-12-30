package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.CadastrarMetaDTO;
import com.projeto.modelo.controller.dto.response.MetaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface MetaService {

    @Transactional
    MetaResponseDTO cadastrarMeta(CadastrarMetaDTO cadastrarMetaDTO);

    @Transactional(readOnly = true)
    Page<MetaResponseDTO> listarMetasPaginado(Pageable pageable);

    @Transactional(readOnly = true)
    MetaResponseDTO buscarMetaPorId(UUID id);

    @Transactional
    MetaResponseDTO atualizarMeta(UUID id, CadastrarMetaDTO cadastrarMetaDTO);
}

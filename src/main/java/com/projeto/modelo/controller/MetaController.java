package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.CadastrarMetaDTO;
import com.projeto.modelo.controller.dto.response.MetaResponseDTO;
import com.projeto.modelo.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/meta")
public class MetaController {

    @Autowired
    private MetaService metaService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/cadastrar")
    public ResponseEntity<MetaResponseDTO> cadastrar(@RequestBody CadastrarMetaDTO cadastrarMetaDTO) {
        MetaResponseDTO metaResponseDTO = metaService.cadastrarMeta(cadastrarMetaDTO);
        return new ResponseEntity<>(metaResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<MetaResponseDTO>> listarMetas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("ano").descending());
        Page<MetaResponseDTO> metas = metaService.listarMetasPaginado(pageable);
        return ResponseEntity.ok(metas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaResponseDTO> buscarPorId(@PathVariable UUID id) {
        MetaResponseDTO meta = metaService.buscarMetaPorId(id);
        return ResponseEntity.ok(meta);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MetaResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody CadastrarMetaDTO cadastrarMetaDTO) {
        MetaResponseDTO metaResponseDTO = metaService.atualizarMeta(id, cadastrarMetaDTO);
        return ResponseEntity.ok(metaResponseDTO);
    }
}

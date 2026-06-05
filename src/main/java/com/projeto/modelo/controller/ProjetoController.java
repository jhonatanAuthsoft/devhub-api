package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.CadastrarProjetoDTO;
import com.projeto.modelo.controller.dto.response.ProjetoResponseDTO;
import com.projeto.modelo.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projeto")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<ProjetoResponseDTO> cadastrarProjeto(@RequestBody @Valid CadastrarProjetoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoService.cadastrarProjeto(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ProjetoResponseDTO>> listarProjetos(
            @RequestParam(required = false) String search,
            @PageableDefault(sort = "titulo", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(projetoService.listarProjetos(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(projetoService.buscarPorId(id));
    }

    @GetMapping("/colaborador/{colaboradorId}")
    public ResponseEntity<List<ProjetoResponseDTO>> listarProjetosPorColaborador(@PathVariable UUID colaboradorId) {
        return ResponseEntity.ok(projetoService.listarProjetosPorColaborador(colaboradorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> atualizarProjeto(@PathVariable UUID id, @RequestBody @Valid CadastrarProjetoDTO dto) {
        return ResponseEntity.ok(projetoService.atualizarProjeto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProjeto(@PathVariable UUID id) {
        projetoService.deletarProjeto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/smps")
    public ResponseEntity<List<ProjetoResponseDTO>> listarSMPsPorProjeto(@PathVariable UUID id) {
        return ResponseEntity.ok(projetoService.listarSMPsPorProjeto(id));
    }
}

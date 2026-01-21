package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.CadastrarApontamentoDTO;
import com.projeto.modelo.controller.dto.response.ApontamentoResponseDTO;
import com.projeto.modelo.service.ApontamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/apontamentos")
@RequiredArgsConstructor
public class ApontamentoController {

    private final ApontamentoService service;

    @PostMapping
    public ResponseEntity<ApontamentoResponseDTO> cadastrar(@RequestBody CadastrarApontamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApontamentoResponseDTO> atualizar(@PathVariable UUID id, @RequestBody CadastrarApontamentoDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<ApontamentoResponseDTO>> listarPorProjeto(
            @PathVariable UUID projetoId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) UUID colaboradorId
    ) {
        return ResponseEntity.ok(service.listarPorProjeto(projetoId, dataInicio, dataFim, colaboradorId));
    }
}

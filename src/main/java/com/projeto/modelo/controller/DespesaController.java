package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.EstornarDespesaDTO;
import com.projeto.modelo.controller.dto.request.PagarDespesaDTO;
import com.projeto.modelo.controller.dto.request.DespesaRequestDTO;
import com.projeto.modelo.controller.dto.response.DespesaResponseDTO;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService service;

    @PostMapping
    public ResponseEntity<List<DespesaResponseDTO>> criar(@RequestBody DespesaRequestDTO dto, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.criar(dto, usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<DespesaResponseDTO>> listarTodos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) UUID categoriaId) {
        return ResponseEntity.ok(service.listarTodos(dataInicio, dataFim, categoriaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<List<DespesaResponseDTO>> atualizar(@PathVariable UUID id, 
                                                              @RequestBody DespesaRequestDTO dto, 
                                                              @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.atualizar(id, dto, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, 
                                        @RequestParam(defaultValue = "APENAS_ESTA") String escopoExclusao, 
                                        @AuthenticationPrincipal Usuario usuario) {
        service.deletar(id, escopoExclusao, usuario);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<DespesaResponseDTO> pagar(@PathVariable UUID id, 
                                                    @RequestBody PagarDespesaDTO dto, 
                                                    @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.pagar(id, dto, usuario));
    }

    @PostMapping("/{id}/estornar")
    public ResponseEntity<List<DespesaResponseDTO>> estornar(@PathVariable UUID id, 
                                                             @RequestBody EstornarDespesaDTO dto, 
                                                             @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.estornar(id, dto, usuario));
    }
}

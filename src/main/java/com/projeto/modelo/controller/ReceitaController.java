package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.EstornarReceitaDTO;
import com.projeto.modelo.controller.dto.request.ReceberReceitaDTO;
import com.projeto.modelo.controller.dto.request.ReceitaRequestDTO;
import com.projeto.modelo.controller.dto.response.ReceitaResponseDTO;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/receitas")
@RequiredArgsConstructor
public class ReceitaController {

    private final ReceitaService service;

    @PostMapping
    public ResponseEntity<List<ReceitaResponseDTO>> criar(@RequestBody ReceitaRequestDTO dto, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.criar(dto, usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ReceitaResponseDTO>> listarTodos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) UUID categoriaId) {
        return ResponseEntity.ok(service.listarTodos(dataInicio, dataFim, categoriaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<List<ReceitaResponseDTO>> atualizar(@PathVariable UUID id, 
                                                              @RequestBody ReceitaRequestDTO dto, 
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

    @PostMapping("/{id}/receber")
    public ResponseEntity<ReceitaResponseDTO> receber(@PathVariable UUID id, 
                                                      @RequestBody ReceberReceitaDTO dto, 
                                                      @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.receber(id, dto, usuario));
    }

    @PostMapping("/{id}/estornar")
    public ResponseEntity<List<ReceitaResponseDTO>> estornar(@PathVariable UUID id, 
                                                             @RequestBody EstornarReceitaDTO dto, 
                                                             @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.estornar(id, dto, usuario));
    }
}

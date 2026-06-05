package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.ContaBancariaRequestDTO;
import com.projeto.modelo.controller.dto.response.ContaBancariaResponseDTO;
import com.projeto.modelo.service.ContaBancariaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contas-bancarias")
@RequiredArgsConstructor
public class ContaBancariaController {

    private final ContaBancariaService service;

    @PostMapping
    public ResponseEntity<ContaBancariaResponseDTO> criar(@RequestBody ContaBancariaRequestDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaBancariaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ContaBancariaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaBancariaResponseDTO> atualizar(@PathVariable UUID id, @RequestBody ContaBancariaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ajuste-saldo")
    public ResponseEntity<ContaBancariaResponseDTO> ajustarSaldo(
            @PathVariable UUID id,
            @RequestBody AjusteSaldoRequest req) {
        return ResponseEntity.ok(service.ajustarSaldo(id, req.getNovoSaldo(), req.getMotivo()));
    }

    @Data
    public static class AjusteSaldoRequest {
        private BigDecimal novoSaldo;
        private String motivo;
    }
}


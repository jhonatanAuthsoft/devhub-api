package com.projeto.modelo.controller;

import com.projeto.modelo.dto.AssinaturaRequest;
import com.projeto.modelo.dto.AssinaturaResponse;
import com.projeto.modelo.service.AssinaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assinaturas")
@RequiredArgsConstructor
@Tag(name = "Assinaturas", description = "Gerenciamento de Assinaturas de Servidores")
public class AssinaturaController {

    private final AssinaturaService assinaturaService;

    @GetMapping
    @Operation(summary = "Listar todas as assinaturas")
    public ResponseEntity<List<AssinaturaResponse>> findAll() {
        return ResponseEntity.ok(assinaturaService.findAll());
    }

    @PostMapping
    @Operation(summary = "Criar nova assinatura")
    public ResponseEntity<AssinaturaResponse> create(@Valid @RequestBody AssinaturaRequest request) {
        return ResponseEntity.ok(assinaturaService.create(request));
    }
}

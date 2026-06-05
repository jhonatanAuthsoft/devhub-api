package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.CategoriaRequestDTO;
import com.projeto.modelo.controller.dto.response.CategoriaResponseDTO;
import com.projeto.modelo.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.projeto.modelo.model.enums.TipoCategoria;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodos(@RequestParam(required = false) TipoCategoria tipo) {
        return ResponseEntity.ok(service.listarTodos(tipo));
    }

    @GetMapping("/raizes")
    public ResponseEntity<List<CategoriaResponseDTO>> listarRaizes(@RequestParam(required = false) TipoCategoria tipo) {
        return ResponseEntity.ok(service.listarRaizes(tipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable UUID id, @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

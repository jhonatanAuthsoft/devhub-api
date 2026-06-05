package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.CartaoCreditoRequestDTO;
import com.projeto.modelo.controller.dto.response.CartaoCreditoResponseDTO;
import com.projeto.modelo.service.CartaoCreditoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cartoes-credito")
@RequiredArgsConstructor
public class CartaoCreditoController {

    private final CartaoCreditoService cartaoCreditoService;

    @PostMapping
    public ResponseEntity<CartaoCreditoResponseDTO> salvar(@Valid @RequestBody CartaoCreditoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartaoCreditoService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoCreditoResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody CartaoCreditoRequestDTO dto) {
        return ResponseEntity.ok(cartaoCreditoService.atualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaoCreditoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(cartaoCreditoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CartaoCreditoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(cartaoCreditoService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        cartaoCreditoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

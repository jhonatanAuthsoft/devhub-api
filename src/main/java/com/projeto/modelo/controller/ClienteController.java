package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.ClienteRequestDTO;
import com.projeto.modelo.controller.dto.response.ClienteResponseDTO;
import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import com.projeto.modelo.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(@RequestBody ClienteRequestDTO dto) {
        try {
            ClienteResponseDTO response = clienteService.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody ClienteRequestDTO dto) {
        try {
            ClienteResponseDTO response = clienteService.atualizar(id, dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable UUID id) {
        try {
            ClienteResponseDTO response = clienteService.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cpf-cnpj/{cpfCnpj}")
    public ResponseEntity<ClienteResponseDTO> buscarPorCpfCnpj(@PathVariable String cpfCnpj) {
        try {
            ClienteResponseDTO response = clienteService.buscarPorCpfCnpj(cpfCnpj);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos(
            @RequestParam(required = false) ClienteStatus status,
            @RequestParam(required = false) TipoPessoa tipo) {
        
        List<ClienteResponseDTO> response;
        
        if (status != null && tipo != null) {
            // Filtrar por ambos (necessário adicionar método no service)
            response = clienteService.listarTodos();
        } else if (status != null) {
            response = clienteService.listarPorStatus(status);
        } else if (tipo != null) {
            response = clienteService.listarPorTipo(tipo);
        } else {
            response = clienteService.listarTodos();
        }
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        try {
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

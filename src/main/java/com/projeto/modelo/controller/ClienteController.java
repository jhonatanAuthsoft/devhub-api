package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.ClienteRequestDTO;
import com.projeto.modelo.controller.dto.response.ClienteResponseDTO;
import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import com.projeto.modelo.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<ClienteResponseDTO>> listarTodos(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) ClienteStatus status,
            @RequestParam(required = false) TipoPessoa tipo) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCriacao").descending());
        Page<ClienteResponseDTO> response;
        
        if (status != null && tipo != null) {
            response = clienteService.listarTodosPaginado(pageable);
        } else if (status != null) {
            response = clienteService.listarPorStatusPaginado(status, pageable);
        } else if (tipo != null) {
            response = clienteService.listarPorTipoPaginado(tipo, pageable);
        } else {
            response = clienteService.listarTodosPaginado(pageable);
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

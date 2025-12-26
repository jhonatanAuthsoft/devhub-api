package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.PessoaRequestDTO;
import com.projeto.modelo.controller.dto.response.PessoaResponseDTO;
import com.projeto.modelo.model.enums.TipoPessoaVinculo;
import com.projeto.modelo.service.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PessoaController {

    private final PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<PessoaResponseDTO> criar(@RequestBody PessoaRequestDTO dto) {
        try {
            PessoaResponseDTO response = pessoaService.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody PessoaRequestDTO dto) {
        try {
            PessoaResponseDTO response = pessoaService.atualizar(id, dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> buscarPorId(@PathVariable UUID id) {
        try {
            PessoaResponseDTO response = pessoaService.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponseDTO>> listarTodas(
            @RequestParam(required = false) TipoPessoaVinculo tipo) {
        
        List<PessoaResponseDTO> response;
        
        if (tipo != null) {
            response = pessoaService.listarPorTipo(tipo);
        } else {
            response = pessoaService.listarTodas();
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PessoaResponseDTO>> listarPorCliente(@PathVariable UUID clienteId) {
        List<PessoaResponseDTO> response = pessoaService.listarPorCliente(clienteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}/responsaveis")
    public ResponseEntity<List<PessoaResponseDTO>> listarResponsaveisPorCliente(@PathVariable UUID clienteId) {
        List<PessoaResponseDTO> response = pessoaService.listarResponsaveisPorCliente(clienteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}/contatos")
    public ResponseEntity<List<PessoaResponseDTO>> listarContatosPorCliente(@PathVariable UUID clienteId) {
        List<PessoaResponseDTO> response = pessoaService.listarContatosPorCliente(clienteId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        try {
            pessoaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

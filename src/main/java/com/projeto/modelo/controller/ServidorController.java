package com.projeto.modelo.controller;

import com.projeto.modelo.dto.ServidorRequest;
import com.projeto.modelo.dto.ServidorResponse;
import com.projeto.modelo.service.ServidorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/servidores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServidorController {

    private final ServidorService servidorService;

    @GetMapping
    public ResponseEntity<List<ServidorResponse>> findAll() {
        List<ServidorResponse> servidores = servidorService.findAll();
        return ResponseEntity.ok(servidores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServidorResponse> findById(@PathVariable UUID id) {
        ServidorResponse servidor = servidorService.findById(id);
        return ResponseEntity.ok(servidor);
    }

    @PostMapping
    public ResponseEntity<ServidorResponse> create(@Valid @RequestBody ServidorRequest request) {
        ServidorResponse created = servidorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ServidorRequest request) {
        ServidorResponse updated = servidorService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        servidorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.response.AuditLogResponseDTO;
import com.projeto.modelo.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository repository;

    @GetMapping
    public ResponseEntity<List<AuditLogResponseDTO>> listarTodos() {
        return ResponseEntity.ok(repository.findAll().stream()
                .map(AuditLogResponseDTO::fromEntity)
                .collect(Collectors.toList()));
    }
}

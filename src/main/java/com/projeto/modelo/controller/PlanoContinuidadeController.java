package com.projeto.modelo.controller;

import com.projeto.modelo.model.PlanoContinuidade;
import com.projeto.modelo.service.PlanoContinuidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/planos-continuidade")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlanoContinuidadeController {

    private final PlanoContinuidadeService service;

    @GetMapping
    public ResponseEntity<List<PlanoContinuidade>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoContinuidade> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PlanoContinuidade> create(@RequestBody PlanoContinuidade plano) {
        return ResponseEntity.ok(service.create(plano));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoContinuidade> update(@PathVariable UUID id, @RequestBody PlanoContinuidade plano) {
        return ResponseEntity.ok(service.update(id, plano));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

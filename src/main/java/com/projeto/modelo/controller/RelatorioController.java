package com.projeto.modelo.controller;

import com.projeto.modelo.dto.relatorio.RelatorioHorasDTO;
import com.projeto.modelo.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@CrossOrigin(origins = "*") 
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/horas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RelatorioHorasDTO>> getRelatorioHoras() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioHoras());
    }
}

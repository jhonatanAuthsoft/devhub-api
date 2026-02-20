package com.projeto.modelo.controller;

import com.projeto.modelo.model.entity.ContratoTemplate;
import com.projeto.modelo.model.enums.TipoProjeto;
import com.projeto.modelo.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class ContratoController {

    private final ContratoService contratoService;

    @PostMapping(value = "/templates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContratoTemplate> uploadTemplate(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipoProjeto") TipoProjeto tipoProjeto) throws IOException {
        return ResponseEntity.ok(contratoService.salvarTemplate(file, tipoProjeto));
    }

    @GetMapping("/templates")
    public ResponseEntity<List<ContratoTemplate>> listarTemplates() {
        return ResponseEntity.ok(contratoService.listarTemplates());
    }

    @GetMapping("/projetos/{projetoId}/download")
    public ResponseEntity<byte[]> baixarContrato(@PathVariable UUID projetoId) throws IOException {
        byte[] doc = contratoService.gerarContrato(projetoId);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"contrato_" + projetoId + ".docx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(doc);
    }
}

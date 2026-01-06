package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.request.AtualizarHorasDTO;
import com.projeto.modelo.controller.dto.response.AlocacaoHorasResponseDTO;
import com.projeto.modelo.service.EquipeProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/equipe")
@RequiredArgsConstructor
public class EquipeProjetoController {

    private final EquipeProjetoService equipeProjetoService;

    @GetMapping("/projeto/{projetoId}/alocacao")
    public ResponseEntity<List<AlocacaoHorasResponseDTO>> listarAlocacao(@PathVariable UUID projetoId) {
        return ResponseEntity.ok(equipeProjetoService.listarAlocacao(projetoId));
    }

    @PutMapping("/{equipeId}/horas")
    public ResponseEntity<Void> atualizarHoras(@PathVariable UUID equipeId, @RequestBody AtualizarHorasDTO dto) {
        try {
            System.out.println("Recebendo atualização de horas para equipeId: " + equipeId + " com valor: " + dto.horasPrevistas());
            equipeProjetoService.atualizarHoras(equipeId, dto);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

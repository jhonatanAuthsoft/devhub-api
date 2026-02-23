package com.projeto.modelo.controller;

import com.projeto.modelo.controller.dto.response.NotificacaoResponseDTO;
import com.projeto.modelo.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notificacao")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping("/usuario/{usuarioId}/nao-lidas")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarNaoLidasPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarNaoLidasPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarTodasPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarTodasPorUsuario(usuarioId));
    }

    @PutMapping("/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable UUID id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.noContent().build();
    }
}

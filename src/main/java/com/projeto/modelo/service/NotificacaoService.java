package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.response.NotificacaoResponseDTO;
import java.util.List;
import java.util.UUID;

public interface NotificacaoService {
    void criarNotificacao(UUID usuarioId, String mensagem);
    List<NotificacaoResponseDTO> listarNaoLidasPorUsuario(UUID usuarioId);
    List<NotificacaoResponseDTO> listarTodasPorUsuario(UUID usuarioId);
    void marcarComoLida(UUID id);
}

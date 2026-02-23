package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.entity.Notificacao;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificacaoResponseDTO {
    private UUID id;
    private String mensagem;
    private Boolean lida;
    private LocalDateTime dataCriacao;
    private UUID usuarioId;

    public static NotificacaoResponseDTO converte(Notificacao model) {
        return NotificacaoResponseDTO.builder()
                .id(model.getId())
                .mensagem(model.getMensagem())
                .lida(model.getLida())
                .dataCriacao(model.getDataCriacao())
                .usuarioId(model.getUsuario().getId())
                .build();
    }
}

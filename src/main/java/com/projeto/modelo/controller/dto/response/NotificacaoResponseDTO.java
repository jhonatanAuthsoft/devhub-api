package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.entity.Notificacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacaoResponseDTO {
    private UUID id;
    private String mensagem;
    private Boolean lida;
    private LocalDateTime dataCriacao;
    private UUID usuarioId;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public Boolean getLida() { return lida; }
    public void setLida(Boolean lida) { this.lida = lida; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public static NotificacaoResponseDTOBuilder builder() {
        return new NotificacaoResponseDTOBuilder();
    }

    public static class NotificacaoResponseDTOBuilder {
        private UUID id;
        private String mensagem;
        private Boolean lida;
        private LocalDateTime dataCriacao;
        private UUID usuarioId;

        public NotificacaoResponseDTOBuilder id(UUID id) { this.id = id; return this; }
        public NotificacaoResponseDTOBuilder mensagem(String mensagem) { this.mensagem = mensagem; return this; }
        public NotificacaoResponseDTOBuilder lida(Boolean lida) { this.lida = lida; return this; }
        public NotificacaoResponseDTOBuilder dataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; return this; }
        public NotificacaoResponseDTOBuilder usuarioId(UUID usuarioId) { this.usuarioId = usuarioId; return this; }

        public NotificacaoResponseDTO build() {
            NotificacaoResponseDTO dto = new NotificacaoResponseDTO();
            dto.setId(id);
            dto.setMensagem(mensagem);
            dto.setLida(lida);
            dto.setDataCriacao(dataCriacao);
            dto.setUsuarioId(usuarioId);
            return dto;
        }
    }

    public static NotificacaoResponseDTO converte(Notificacao model) {
        return NotificacaoResponseDTO.builder()
                .id(model.getId())
                .mensagem(model.getMensagem())
                .lida(model.getLida())
                .dataCriacao(model.getDataCriacao())
                .usuarioId(model.getUsuario() != null ? model.getUsuario().getId() : null)
                .build();
    }
}

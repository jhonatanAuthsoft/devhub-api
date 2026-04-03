package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.entity.AuditLog;
import com.projeto.modelo.model.enums.AcaoAuditLog;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AuditLogResponseDTO {
    private UUID id;
    private String entidade;
    private UUID entidadeId;
    private AcaoAuditLog acao;
    private UUID usuarioId;
    private String usuarioNome;
    private String dadosAnteriores;
    private String dadosNovos;
    private LocalDateTime criadoEm;

    public static AuditLogResponseDTO fromEntity(AuditLog log) {
        return AuditLogResponseDTO.builder()
                .id(log.getId())
                .entidade(log.getEntidade())
                .entidadeId(log.getEntidadeId())
                .acao(log.getAcao())
                .usuarioId(log.getUsuario() != null ? log.getUsuario().getId() : null)
                .usuarioNome(log.getUsuarioNome())
                .dadosAnteriores(log.getDadosAnteriores())
                .dadosNovos(log.getDadosNovos())
                .criadoEm(log.getDataCriacao())
                .build();
    }
}

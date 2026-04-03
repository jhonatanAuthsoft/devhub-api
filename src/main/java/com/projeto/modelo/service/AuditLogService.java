package com.projeto.modelo.service;

import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.AcaoAuditLog;

import java.util.UUID;

public interface AuditLogService {
    void registrarLog(String entidade, UUID entidadeId, AcaoAuditLog acao, Usuario usuario, String dadosAnteriores, String dadosNovos, String ipOrigem);
}

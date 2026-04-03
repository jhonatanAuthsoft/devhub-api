package com.projeto.modelo.service.imp;

import com.projeto.modelo.model.entity.AuditLog;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.AcaoAuditLog;
import com.projeto.modelo.repository.AuditLogRepository;
import com.projeto.modelo.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImp implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarLog(String entidade, UUID entidadeId, AcaoAuditLog acao, Usuario usuario, String dadosAnteriores, String dadosNovos, String ipOrigem) {
        AuditLog log = AuditLog.builder()
                .entidade(entidade)
                .entidadeId(entidadeId)
                .acao(acao)
                .usuario(usuario)
                .usuarioNome(usuario != null ? usuario.getNome() : "SISTEMA")
                .dadosAnteriores(dadosAnteriores)
                .dadosNovos(dadosNovos)
                .ipOrigem(ipOrigem)
                .build();
        auditLogRepository.save(log);
    }
}

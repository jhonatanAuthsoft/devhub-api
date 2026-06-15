package com.projeto.modelo.dto;

import java.util.UUID;

public record ProjetoNotificacaoTicketDTO(
    UUID id,
    UUID usuarioId,
    Boolean notificarCriacao,
    Boolean notificarAtualizacao
) {}

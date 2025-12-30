package com.projeto.modelo.controller.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MetaResponseDTO(
        UUID id,
        Integer ano,
        String categoria,
        String tipoMeta,
        BigDecimal valorAnual,
        BigDecimal janeiro,
        BigDecimal fevereiro,
        BigDecimal marco,
        BigDecimal abril,
        BigDecimal maio,
        BigDecimal junho,
        BigDecimal julho,
        BigDecimal agosto,
        BigDecimal setembro,
        BigDecimal outubro,
        BigDecimal novembro,
        BigDecimal dezembro,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
}

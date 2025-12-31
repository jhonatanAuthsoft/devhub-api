package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.enums.StatusParcela;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ParcelaProjetoResponseDTO(
    UUID id,
    Integer numero,
    BigDecimal valor,
    LocalDate dataVencimento,
    StatusParcela status
) {}

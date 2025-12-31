package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.model.enums.StatusParcela;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaProjetoRequestDTO(
    Integer numero,
    BigDecimal valor,
    LocalDate dataVencimento,
    StatusParcela status
) {}

package com.projeto.modelo.controller.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ParcelaPersonalizadaDTO {
    private BigDecimal valor;
    private LocalDate dataVencimento;
}

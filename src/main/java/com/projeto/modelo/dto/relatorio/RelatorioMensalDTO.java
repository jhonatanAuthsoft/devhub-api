package com.projeto.modelo.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioMensalDTO {
    private String mesAno; // "2024-01", "2024-02"
    private BigDecimal horas;
}

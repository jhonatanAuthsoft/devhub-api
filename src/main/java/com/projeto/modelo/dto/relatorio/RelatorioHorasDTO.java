package com.projeto.modelo.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioHorasDTO {
    private UUID projetoId;
    private String tituloProjeto;
    private String tipoProjeto; // "Software Sob Medida" ou "Venda de Horas"
    private BigDecimal horasEstimadas;
    private BigDecimal horasGastasTotal;
    private BigDecimal saldoHoras; // Apenas para Venda de Horas (Estimado - Gasto)
    
    @Builder.Default
    private List<RelatorioProfissionalDTO> profissionais = new ArrayList<>();
}

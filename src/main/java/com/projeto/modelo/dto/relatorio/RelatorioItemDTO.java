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
public class RelatorioItemDTO {
    private UUID id;
    private String titulo; // Nome do Pai (Projeto ou Colaborador)
    private String tipo; // "Software Sob Medida", "Venda de Horas" (Opcional, se pai for Projeto)
    private BigDecimal horasEstimadas; // Opcional (se pai for Projeto)
    private BigDecimal totalHoras;
    private BigDecimal saldoHoras; // Opcional (se pai for Projeto)
    
    @Builder.Default
    private List<RelatorioSubItemDTO> subItens = new ArrayList<>();
}

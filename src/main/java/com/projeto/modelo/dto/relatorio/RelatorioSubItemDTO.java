package com.projeto.modelo.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioSubItemDTO {
    private String titulo; // Nome do Projeto (se pai for Colaborador) ou Nome do Colaborador (se pai for Projeto)
    private BigDecimal totalHoras;
    private BigDecimal horasAlocadas; // Opcional (apenas para Colaborador)
    
    @Builder.Default
    private List<RelatorioDetalheDTO> detalhes = new ArrayList<>();
}

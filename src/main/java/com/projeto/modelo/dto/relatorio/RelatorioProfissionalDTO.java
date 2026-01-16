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
public class RelatorioProfissionalDTO {
    private String nomeProfissional;
    private BigDecimal totalHoras;
    
    @Builder.Default
    private List<RelatorioMensalDTO> meses = new ArrayList<>();
}

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
public class RankingResponseDTO {
    private int ano;
    private BigDecimal receitaTotal;
    private BigDecimal lucroTotal;
    private BigDecimal margemMedia; // %
    private BigDecimal valorTotalGlobal;

    @Builder.Default
    private List<RankingTipoDTO> grupos = new ArrayList<>();
}

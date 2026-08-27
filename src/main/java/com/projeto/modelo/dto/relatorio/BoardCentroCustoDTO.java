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
public class BoardCentroCustoDTO {
    private int ano;
    private BigDecimal despesaTotalAno;
    
    @Builder.Default
    private List<TotalMensalDTO> totaisMensais = new ArrayList<>();
    
    @Builder.Default
    private List<GrupoCentroCustoDTO> gruposCentroCusto = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GrupoCentroCustoDTO {
        private UUID categoriaPaiId;
        private String nomeCentroCusto;
        private BigDecimal totalValorCentroCusto;
        private BigDecimal percentualDespesaTipo;
        
        @Builder.Default
        private List<BigDecimal> totaisMensaisCentroCusto = new ArrayList<>(); // 12 posições (Jan..Dez)
        
        @Builder.Default
        private List<LinhaDespesaBoardDTO> despesas = new ArrayList<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinhaDespesaBoardDTO {
        private UUID categoriaId;
        private String nomeCategoria;
        
        @Builder.Default
        private List<BigDecimal> valoresMensais = new ArrayList<>(); // 12 posições (Jan..Dez)
        
        private BigDecimal totalDespesaAno;
        private BigDecimal percentualShare;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalMensalDTO {
        private int mes; // 1 a 12
        private String nomeMes;
        private BigDecimal valorTotal;
        private BigDecimal percentualAno;
    }
}

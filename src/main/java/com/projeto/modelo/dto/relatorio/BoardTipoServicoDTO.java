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
public class BoardTipoServicoDTO {
    private int ano;
    private BigDecimal faturamentoTotalAno;
    
    @Builder.Default
    private List<TotalMensalDTO> totaisMensais = new ArrayList<>();
    
    @Builder.Default
    private List<GrupoTipoServicoDTO> gruposTipo = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GrupoTipoServicoDTO {
        private String tipoKey;
        private String nomeExibicao;
        private BigDecimal totalValorTipo;
        private BigDecimal percentualFaturamentoTipo;
        
        @Builder.Default
        private List<BigDecimal> totaisMensaisTipo = new ArrayList<>(); // 12 posições (Jan..Dez)
        
        @Builder.Default
        private List<LinhaClienteBoardDTO> clientes = new ArrayList<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinhaClienteBoardDTO {
        private UUID clienteId;
        private String nomeCliente;
        
        @Builder.Default
        private List<BigDecimal> valoresMensais = new ArrayList<>(); // 12 posições (Jan..Dez)
        
        private BigDecimal totalClienteAno;
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

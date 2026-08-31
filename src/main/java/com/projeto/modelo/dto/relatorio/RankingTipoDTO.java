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
public class RankingTipoDTO {
    private String tipoKey;
    private String nomeExibicao;
    private BigDecimal receitaTipo;
    private BigDecimal lucroTipo;
    private BigDecimal valorTotalTipo;

    @Builder.Default
    private List<RankingProjetoDTO> projetos = new ArrayList<>();

    @Builder.Default
    private List<RankingClienteDTO> clientes = new ArrayList<>();
}

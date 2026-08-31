package com.projeto.modelo.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingClienteDTO {
    private UUID clienteId;
    private String nomeCliente;
    private String tipoKey;
    private String nomeTipo;
    private BigDecimal receita;
    private BigDecimal lucro;
    private BigDecimal margem; // % (lucro / receita)
    private Integer qtdProjetos;
    private BigDecimal valorTotal;
}

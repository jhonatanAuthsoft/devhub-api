package com.projeto.modelo.controller.dto.response;

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
public class SugestaoPagamentoDTO {
    private UUID id; // Um UUID fake gerado apenas para frontend usar como key na renderizacao
    private String descricao;
    private BigDecimal valorPrevisto;
    private String dataVencimento; // Data base para visualizacao
    private String status; // Sempre "SUGESTAO"
    
    private UUID colaboradorId;
    private String colaboradorNome;
    
    private String mesReferencia;
    
    private BigDecimal valorSalarioFixo;
    private BigDecimal valorHoras;
    private BigDecimal quantidadeHoras;
}

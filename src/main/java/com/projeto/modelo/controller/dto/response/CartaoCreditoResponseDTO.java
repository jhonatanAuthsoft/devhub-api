package com.projeto.modelo.controller.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CartaoCreditoResponseDTO {
    private UUID id;
    private String descricao;
    private BigDecimal limite;
    private Integer diaFechamento;
    private Integer diaVencimento;
    private ContaBancariaResponseDTO conta;
}

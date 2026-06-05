package com.projeto.modelo.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartaoCreditoRequestDTO {
    
    @NotBlank
    private String descricao;
    
    @NotNull
    private BigDecimal limite;
    
    @NotNull
    private Integer diaFechamento;
    
    @NotNull
    private Integer diaVencimento;
    
    @NotNull
    private UUID contaId;
}

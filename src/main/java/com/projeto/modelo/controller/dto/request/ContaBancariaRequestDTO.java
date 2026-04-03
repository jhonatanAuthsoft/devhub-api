package com.projeto.modelo.controller.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ContaBancariaRequestDTO {
    private String nome;
    private BigDecimal saldoInicial;
    private Boolean ativo;
}

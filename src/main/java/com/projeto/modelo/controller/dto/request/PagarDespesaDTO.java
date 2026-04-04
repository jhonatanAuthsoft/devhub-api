package com.projeto.modelo.controller.dto.request;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PagarDespesaDTO {
    private UUID contaBancariaId;
    private BigDecimal valorPago;
    private LocalDate dataPagamento;
}

package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.enums.FuncaoProjeto;
import java.math.BigDecimal;
import java.util.UUID;

public record AlocacaoHorasResponseDTO(
    UUID equipeId,
    String nomeColaborador,
    FuncaoProjeto funcao,
    BigDecimal valorHora,
    BigDecimal porcentagem,
    BigDecimal custoCalculado,
    BigDecimal horasCalculadas,
    BigDecimal horasPrevistas,
    BigDecimal custoPrevisto
) {}

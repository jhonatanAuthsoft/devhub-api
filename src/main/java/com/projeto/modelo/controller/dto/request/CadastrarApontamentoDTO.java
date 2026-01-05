package com.projeto.modelo.controller.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastrarApontamentoDTO(
    UUID projetoId,
    UUID colaboradorId,
    LocalDate dataApontamento,
    BigDecimal horas,
    String descricao
) {}

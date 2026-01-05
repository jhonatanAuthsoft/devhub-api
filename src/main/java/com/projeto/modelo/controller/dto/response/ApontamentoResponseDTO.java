package com.projeto.modelo.controller.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ApontamentoResponseDTO(
    UUID id,
    String projetoTitulo,
    String colaboradorNome,
    LocalDate dataApontamento,
    BigDecimal horas,
    String descricao,
    LocalDateTime createdAt
) {}

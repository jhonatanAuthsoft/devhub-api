package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.enums.FuncaoProjeto;
import java.math.BigDecimal;
import java.util.UUID;

public record EquipeProjetoResponseDTO(
    UUID id,
    UUID colaboradorId,
    String nomeColaborador,
    FuncaoProjeto funcao,
    Boolean usaSalarioFixo,
    BigDecimal porcentagem,
    BigDecimal horasPrevistas,
    BigDecimal custoPrevisto,
    Boolean notificarTicket
) {}

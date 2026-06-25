package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.model.enums.FuncaoProjeto;
import java.math.BigDecimal;
import java.util.UUID;

public record EquipeProjetoRequestDTO(
    UUID colaboradorId,
    FuncaoProjeto funcao,
    Boolean usaSalarioFixo,
    BigDecimal porcentagem,
    Boolean notificarTicket
) {}

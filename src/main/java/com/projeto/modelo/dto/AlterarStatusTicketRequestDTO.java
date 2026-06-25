package com.projeto.modelo.dto;

import com.projeto.modelo.model.enums.StatusTicket;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AlterarStatusTicketRequestDTO {
    @NotNull(message = "O novo status é obrigatório")
    private StatusTicket novoStatus;

    private String observacao;

    private UUID direcionadoParaId;
}

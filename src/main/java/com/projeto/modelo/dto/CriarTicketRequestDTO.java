package com.projeto.modelo.dto;

import com.projeto.modelo.model.enums.PrioridadeTicket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CriarTicketRequestDTO {
    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "O projeto é obrigatório")
    private UUID projetoId;

    @NotNull(message = "A prioridade é obrigatória")
    private PrioridadeTicket prioridade;
}

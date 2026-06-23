package com.projeto.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketComentarioRequestDTO {
    @NotBlank(message = "O texto do comentário não pode ser vazio.")
    private String texto;
}

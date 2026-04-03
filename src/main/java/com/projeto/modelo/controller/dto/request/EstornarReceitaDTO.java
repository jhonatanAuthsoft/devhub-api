package com.projeto.modelo.controller.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EstornarReceitaDTO {
    private String motivo;
    private LocalDate dataEstorno;
    private String escopoEstorno; // APENAS_ESTA, TODAS_JA_RECEBIDAS
}

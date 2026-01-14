package com.projeto.modelo.dto;

import com.projeto.modelo.controller.dto.response.ClienteResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaResponse {

    private UUID id;
    private ClienteResponseDTO cliente; // Reusing existing ClienteResponseDTO if available, or simplified version
    private ServidorResponse servidor;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String tipoPlano;
    private BigDecimal valorMensal;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.projeto.modelo.dto;

import com.projeto.modelo.model.enums.TipoAnexo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TicketAnexoDTO {
    private UUID id;
    private TipoAnexo tipo;
    private String urlArquivo;
    private String nomeArquivo;
    private Long tamanhoBytes;
    private LocalDateTime dataEnvio;
    private UUID enviadoPorId;
    private String enviadoPorTipo;
}

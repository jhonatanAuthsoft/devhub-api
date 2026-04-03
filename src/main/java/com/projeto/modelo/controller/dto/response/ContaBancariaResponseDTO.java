package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.entity.ContaBancaria;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
public class ContaBancariaResponseDTO {
    private UUID id;
    private String nome;
    private BigDecimal saldoAtual;
    private Boolean ativo;
    private Boolean emiteBoleto;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public static ContaBancariaResponseDTO fromEntity(ContaBancaria entity) {
        return ContaBancariaResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .saldoAtual(entity.getSaldoAtual())
                .ativo(entity.getAtivo())
                .emiteBoleto(entity.getEmiteBoleto())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .build();
    }
}

package com.projeto.modelo.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFiltroDTO {
    private UUID projetoId;
    private UUID colaboradorId;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;
    
    private String agrupamento; // "PROJETO" ou "COLABORADOR"

    public UUID getProjetoId() { return projetoId; }
    public void setProjetoId(UUID projetoId) { this.projetoId = projetoId; }

    public UUID getColaboradorId() { return colaboradorId; }
    public void setColaboradorId(UUID colaboradorId) { this.colaboradorId = colaboradorId; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public String getAgrupamento() { return agrupamento; }
    public void setAgrupamento(String agrupamento) { this.agrupamento = agrupamento; }
}

package com.projeto.modelo.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioItemDTO {
    private UUID id;
    private String titulo; // Nome do Pai (Projeto ou Colaborador)
    private String tipo; // "Software Sob Medida", "Venda de Horas" (Opcional, se pai for Projeto)
    private BigDecimal horasEstimadas; // Opcional (se pai for Projeto)
    private BigDecimal totalHoras;
    private BigDecimal saldoHoras; // Opcional (se pai for Projeto)
    
    @Builder.Default
    private List<RelatorioSubItemDTO> subItens = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getHorasEstimadas() { return horasEstimadas; }
    public void setHorasEstimadas(BigDecimal horasEstimadas) { this.horasEstimadas = horasEstimadas; }

    public BigDecimal getTotalHoras() { return totalHoras; }
    public void setTotalHoras(BigDecimal totalHoras) { this.totalHoras = totalHoras; }

    public BigDecimal getSaldoHoras() { return saldoHoras; }
    public void setSaldoHoras(BigDecimal saldoHoras) { this.saldoHoras = saldoHoras; }

    public List<RelatorioSubItemDTO> getSubItens() { return subItens; }
    public void setSubItens(List<RelatorioSubItemDTO> subItens) { this.subItens = subItens; }

    public static RelatorioItemDTOBuilder builder() {
        return new RelatorioItemDTOBuilder();
    }

    public static class RelatorioItemDTOBuilder {
        private UUID id;
        private String titulo;
        private String tipo;
        private BigDecimal horasEstimadas;
        private BigDecimal totalHoras;
        private BigDecimal saldoHoras;
        private List<RelatorioSubItemDTO> subItens = new ArrayList<>();

        public RelatorioItemDTOBuilder id(UUID id) { this.id = id; return this; }
        public RelatorioItemDTOBuilder titulo(String titulo) { this.titulo = titulo; return this; }
        public RelatorioItemDTOBuilder tipo(String tipo) { this.tipo = tipo; return this; }
        public RelatorioItemDTOBuilder horasEstimadas(BigDecimal horasEstimadas) { this.horasEstimadas = horasEstimadas; return this; }
        public RelatorioItemDTOBuilder totalHoras(BigDecimal totalHoras) { this.totalHoras = totalHoras; return this; }
        public RelatorioItemDTOBuilder saldoHoras(BigDecimal saldoHoras) { this.saldoHoras = saldoHoras; return this; }
        public RelatorioItemDTOBuilder subItens(List<RelatorioSubItemDTO> subItens) { this.subItens = subItens; return this; }

        public RelatorioItemDTO build() {
            RelatorioItemDTO dto = new RelatorioItemDTO();
            dto.setId(this.id);
            dto.setTitulo(this.titulo);
            dto.setTipo(this.tipo);
            dto.setHorasEstimadas(this.horasEstimadas);
            dto.setTotalHoras(this.totalHoras);
            dto.setSaldoHoras(this.saldoHoras);
            dto.setSubItens(this.subItens);
            return dto;
        }
    }
}

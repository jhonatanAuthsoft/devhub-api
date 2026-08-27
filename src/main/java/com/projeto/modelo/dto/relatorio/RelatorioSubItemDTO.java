package com.projeto.modelo.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioSubItemDTO {
    private String titulo; // Nome do Projeto (se pai for Colaborador) ou Nome do Colaborador (se pai for Projeto)
    private BigDecimal totalHoras;
    private BigDecimal horasAlocadas; // Opcional (apenas para Colaborador)
    
    @Builder.Default
    private List<RelatorioDetalheDTO> detalhes = new ArrayList<>();

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public BigDecimal getTotalHoras() { return totalHoras; }
    public void setTotalHoras(BigDecimal totalHoras) { this.totalHoras = totalHoras; }

    public BigDecimal getHorasAlocadas() { return horasAlocadas; }
    public void setHorasAlocadas(BigDecimal horasAlocadas) { this.horasAlocadas = horasAlocadas; }

    public List<RelatorioDetalheDTO> getDetalhes() { return detalhes; }
    public void setDetalhes(List<RelatorioDetalheDTO> detalhes) { this.detalhes = detalhes; }

    public static RelatorioSubItemDTOBuilder builder() {
        return new RelatorioSubItemDTOBuilder();
    }

    public static class RelatorioSubItemDTOBuilder {
        private String titulo;
        private BigDecimal totalHoras;
        private BigDecimal horasAlocadas;
        private List<RelatorioDetalheDTO> detalhes = new ArrayList<>();

        public RelatorioSubItemDTOBuilder titulo(String titulo) { this.titulo = titulo; return this; }
        public RelatorioSubItemDTOBuilder totalHoras(BigDecimal totalHoras) { this.totalHoras = totalHoras; return this; }
        public RelatorioSubItemDTOBuilder horasAlocadas(BigDecimal horasAlocadas) { this.horasAlocadas = horasAlocadas; return this; }
        public RelatorioSubItemDTOBuilder detalhes(List<RelatorioDetalheDTO> detalhes) { this.detalhes = detalhes; return this; }

        public RelatorioSubItemDTO build() {
            RelatorioSubItemDTO dto = new RelatorioSubItemDTO();
            dto.setTitulo(this.titulo);
            dto.setTotalHoras(this.totalHoras);
            dto.setHorasAlocadas(this.horasAlocadas);
            dto.setDetalhes(this.detalhes);
            return dto;
        }
    }
}

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
public class RelatorioProfissionalDTO {
    private String nomeProfissional;
    private BigDecimal totalHoras;
    
    @Builder.Default
    private List<RelatorioMensalDTO> meses = new ArrayList<>();

    public String getNomeProfissional() { return nomeProfissional; }
    public void setNomeProfissional(String nomeProfissional) { this.nomeProfissional = nomeProfissional; }

    public BigDecimal getTotalHoras() { return totalHoras; }
    public void setTotalHoras(BigDecimal totalHoras) { this.totalHoras = totalHoras; }

    public List<RelatorioMensalDTO> getMeses() { return meses; }
    public void setMeses(List<RelatorioMensalDTO> meses) { this.meses = meses; }

    public static RelatorioProfissionalDTOBuilder builder() {
        return new RelatorioProfissionalDTOBuilder();
    }

    public static class RelatorioProfissionalDTOBuilder {
        private String nomeProfissional;
        private BigDecimal totalHoras;
        private List<RelatorioMensalDTO> meses = new ArrayList<>();

        public RelatorioProfissionalDTOBuilder nomeProfissional(String nomeProfissional) { this.nomeProfissional = nomeProfissional; return this; }
        public RelatorioProfissionalDTOBuilder totalHoras(BigDecimal totalHoras) { this.totalHoras = totalHoras; return this; }
        public RelatorioProfissionalDTOBuilder meses(List<RelatorioMensalDTO> meses) { this.meses = meses; return this; }

        public RelatorioProfissionalDTO build() {
            RelatorioProfissionalDTO dto = new RelatorioProfissionalDTO();
            dto.setNomeProfissional(this.nomeProfissional);
            dto.setTotalHoras(this.totalHoras);
            dto.setMeses(this.meses);
            return dto;
        }
    }
}

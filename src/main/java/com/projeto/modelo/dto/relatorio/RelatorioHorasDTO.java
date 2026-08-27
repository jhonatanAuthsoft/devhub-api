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
public class RelatorioHorasDTO {
    private UUID projetoId;
    private String tituloProjeto;
    private String tipoProjeto; // "Software Sob Medida" ou "Venda de Horas"
    private BigDecimal horasEstimadas;
    private BigDecimal horasGastasTotal;
    private BigDecimal saldoHoras; // Apenas para Venda de Horas (Estimado - Gasto)
    
    @Builder.Default
    private List<RelatorioProfissionalDTO> profissionais = new ArrayList<>();

    public UUID getProjetoId() { return projetoId; }
    public void setProjetoId(UUID projetoId) { this.projetoId = projetoId; }

    public String getTituloProjeto() { return tituloProjeto; }
    public void setTituloProjeto(String tituloProjeto) { this.tituloProjeto = tituloProjeto; }

    public String getTipoProjeto() { return tipoProjeto; }
    public void setTipoProjeto(String tipoProjeto) { this.tipoProjeto = tipoProjeto; }

    public BigDecimal getHorasEstimadas() { return horasEstimadas; }
    public void setHorasEstimadas(BigDecimal horasEstimadas) { this.horasEstimadas = horasEstimadas; }

    public BigDecimal getHorasGastasTotal() { return horasGastasTotal; }
    public void setHorasGastasTotal(BigDecimal horasGastasTotal) { this.horasGastasTotal = horasGastasTotal; }

    public BigDecimal getSaldoHoras() { return saldoHoras; }
    public void setSaldoHoras(BigDecimal saldoHoras) { this.saldoHoras = saldoHoras; }

    public List<RelatorioProfissionalDTO> getProfissionais() { return profissionais; }
    public void setProfissionais(List<RelatorioProfissionalDTO> profissionais) { this.profissionais = profissionais; }

    public static RelatorioHorasDTOBuilder builder() {
        return new RelatorioHorasDTOBuilder();
    }

    public static class RelatorioHorasDTOBuilder {
        private UUID projetoId;
        private String tituloProjeto;
        private String tipoProjeto;
        private BigDecimal horasEstimadas;
        private BigDecimal horasGastasTotal;
        private BigDecimal saldoHoras;
        private List<RelatorioProfissionalDTO> profissionais = new ArrayList<>();

        public RelatorioHorasDTOBuilder projetoId(UUID projetoId) { this.projetoId = projetoId; return this; }
        public RelatorioHorasDTOBuilder tituloProjeto(String tituloProjeto) { this.tituloProjeto = tituloProjeto; return this; }
        public RelatorioHorasDTOBuilder tipoProjeto(String tipoProjeto) { this.tipoProjeto = tipoProjeto; return this; }
        public RelatorioHorasDTOBuilder horasEstimadas(BigDecimal horasEstimadas) { this.horasEstimadas = horasEstimadas; return this; }
        public RelatorioHorasDTOBuilder horasGastasTotal(BigDecimal horasGastasTotal) { this.horasGastasTotal = horasGastasTotal; return this; }
        public RelatorioHorasDTOBuilder saldoHoras(BigDecimal saldoHoras) { this.saldoHoras = saldoHoras; return this; }
        public RelatorioHorasDTOBuilder profissionais(List<RelatorioProfissionalDTO> profissionais) { this.profissionais = profissionais; return this; }

        public RelatorioHorasDTO build() {
            RelatorioHorasDTO dto = new RelatorioHorasDTO();
            dto.setProjetoId(this.projetoId);
            dto.setTituloProjeto(this.tituloProjeto);
            dto.setTipoProjeto(this.tipoProjeto);
            dto.setHorasEstimadas(this.horasEstimadas);
            dto.setHorasGastasTotal(this.horasGastasTotal);
            dto.setSaldoHoras(this.saldoHoras);
            dto.setProfissionais(this.profissionais);
            return dto;
        }
    }
}

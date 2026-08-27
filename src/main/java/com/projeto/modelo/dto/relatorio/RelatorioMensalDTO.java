package com.projeto.modelo.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioMensalDTO {
    private String mesAno; // "2024-01", "2024-02"
    private BigDecimal horas;

    public String getMesAno() { return mesAno; }
    public void setMesAno(String mesAno) { this.mesAno = mesAno; }

    public BigDecimal getHoras() { return horas; }
    public void setHoras(BigDecimal horas) { this.horas = horas; }

    public static RelatorioMensalDTOBuilder builder() {
        return new RelatorioMensalDTOBuilder();
    }

    public static class RelatorioMensalDTOBuilder {
        private String mesAno;
        private BigDecimal horas;

        public RelatorioMensalDTOBuilder mesAno(String mesAno) { this.mesAno = mesAno; return this; }
        public RelatorioMensalDTOBuilder horas(BigDecimal horas) { this.horas = horas; return this; }

        public RelatorioMensalDTO build() {
            RelatorioMensalDTO dto = new RelatorioMensalDTO();
            dto.setMesAno(this.mesAno);
            dto.setHoras(this.horas);
            return dto;
        }
    }
}

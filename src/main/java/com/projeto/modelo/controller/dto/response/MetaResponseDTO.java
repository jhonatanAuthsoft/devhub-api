package com.projeto.modelo.controller.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MetaResponseDTO(
        UUID id,
        Integer ano,
        String categoria,
        String tipoMeta,
        BigDecimal valorAnual,
        BigDecimal janeiro,
        BigDecimal fevereiro,
        BigDecimal marco,
        BigDecimal abril,
        BigDecimal maio,
        BigDecimal junho,
        BigDecimal julho,
        BigDecimal agosto,
        BigDecimal setembro,
        BigDecimal outubro,
        BigDecimal novembro,
        BigDecimal dezembro,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
    public static MetaResponseDTOBuilder builder() {
        return new MetaResponseDTOBuilder();
    }

    public static class MetaResponseDTOBuilder {
        private UUID id;
        private Integer ano;
        private String categoria;
        private String tipoMeta;
        private BigDecimal valorAnual;
        private BigDecimal janeiro;
        private BigDecimal fevereiro;
        private BigDecimal marco;
        private BigDecimal abril;
        private BigDecimal maio;
        private BigDecimal junho;
        private BigDecimal julho;
        private BigDecimal agosto;
        private BigDecimal setembro;
        private BigDecimal outubro;
        private BigDecimal novembro;
        private BigDecimal dezembro;
        private LocalDateTime dataCriacao;
        private LocalDateTime dataAtualizacao;

        public MetaResponseDTOBuilder id(UUID id) { this.id = id; return this; }
        public MetaResponseDTOBuilder ano(Integer ano) { this.ano = ano; return this; }
        public MetaResponseDTOBuilder categoria(String categoria) { this.categoria = categoria; return this; }
        public MetaResponseDTOBuilder tipoMeta(String tipoMeta) { this.tipoMeta = tipoMeta; return this; }
        public MetaResponseDTOBuilder valorAnual(BigDecimal valorAnual) { this.valorAnual = valorAnual; return this; }
        public MetaResponseDTOBuilder janeiro(BigDecimal janeiro) { this.janeiro = janeiro; return this; }
        public MetaResponseDTOBuilder fevereiro(BigDecimal fevereiro) { this.fevereiro = fevereiro; return this; }
        public MetaResponseDTOBuilder marco(BigDecimal marco) { this.marco = marco; return this; }
        public MetaResponseDTOBuilder abril(BigDecimal abril) { this.abril = abril; return this; }
        public MetaResponseDTOBuilder maio(BigDecimal maio) { this.maio = maio; return this; }
        public MetaResponseDTOBuilder junho(BigDecimal junho) { this.junho = junho; return this; }
        public MetaResponseDTOBuilder julho(BigDecimal julho) { this.julho = julho; return this; }
        public MetaResponseDTOBuilder agosto(BigDecimal agosto) { this.agosto = agosto; return this; }
        public MetaResponseDTOBuilder setembro(BigDecimal setembro) { this.setembro = setembro; return this; }
        public MetaResponseDTOBuilder outubro(BigDecimal outubro) { this.outubro = outubro; return this; }
        public MetaResponseDTOBuilder novembro(BigDecimal novembro) { this.novembro = novembro; return this; }
        public MetaResponseDTOBuilder dezembro(BigDecimal dezembro) { this.dezembro = dezembro; return this; }
        public MetaResponseDTOBuilder dataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; return this; }
        public MetaResponseDTOBuilder dataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; return this; }

        public MetaResponseDTO build() {
            return new MetaResponseDTO(id, ano, categoria, tipoMeta, valorAnual, janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro, dataCriacao, dataAtualizacao);
        }
    }
}

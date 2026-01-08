package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.CategoriaMeta;
import com.projeto.modelo.model.enums.TipoMeta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "meta", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ano", "categoria", "tipo_meta"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriaMeta categoria;

    @Column(name = "tipo_meta", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMeta tipoMeta;

    @Column(name = "valor_anual", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorAnual;

    @Column(name = "janeiro", precision = 15, scale = 2)
    private BigDecimal janeiro;

    @Column(name = "fevereiro", precision = 15, scale = 2)
    private BigDecimal fevereiro;

    @Column(name = "marco", precision = 15, scale = 2)
    private BigDecimal marco;

    @Column(name = "abril", precision = 15, scale = 2)
    private BigDecimal abril;

    @Column(name = "maio", precision = 15, scale = 2)
    private BigDecimal maio;

    @Column(name = "junho", precision = 15, scale = 2)
    private BigDecimal junho;

    @Column(name = "julho", precision = 15, scale = 2)
    private BigDecimal julho;

    @Column(name = "agosto", precision = 15, scale = 2)
    private BigDecimal agosto;

    @Column(name = "setembro", precision = 15, scale = 2)
    private BigDecimal setembro;

    @Column(name = "outubro", precision = 15, scale = 2)
    private BigDecimal outubro;

    @Column(name = "novembro", precision = 15, scale = 2)
    private BigDecimal novembro;

    @Column(name = "dezembro", precision = 15, scale = 2)
    private BigDecimal dezembro;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Manual Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }
    public CategoriaMeta getCategoria() { return categoria; }
    public void setCategoria(CategoriaMeta categoria) { this.categoria = categoria; }
    public TipoMeta getTipoMeta() { return tipoMeta; }
    public void setTipoMeta(TipoMeta tipoMeta) { this.tipoMeta = tipoMeta; }
    public BigDecimal getValorAnual() { return valorAnual; }
    public void setValorAnual(BigDecimal valorAnual) { this.valorAnual = valorAnual; }
    public BigDecimal getJaneiro() { return janeiro; }
    public void setJaneiro(BigDecimal janeiro) { this.janeiro = janeiro; }
    public BigDecimal getFevereiro() { return fevereiro; }
    public void setFevereiro(BigDecimal fevereiro) { this.fevereiro = fevereiro; }
    public BigDecimal getMarco() { return marco; }
    public void setMarco(BigDecimal marco) { this.marco = marco; }
    public BigDecimal getAbril() { return abril; }
    public void setAbril(BigDecimal abril) { this.abril = abril; }
    public BigDecimal getMaio() { return maio; }
    public void setMaio(BigDecimal maio) { this.maio = maio; }
    public BigDecimal getJunho() { return junho; }
    public void setJunho(BigDecimal junho) { this.junho = junho; }
    public BigDecimal getJulho() { return julho; }
    public void setJulho(BigDecimal julho) { this.julho = julho; }
    public BigDecimal getAgosto() { return agosto; }
    public void setAgosto(BigDecimal agosto) { this.agosto = agosto; }
    public BigDecimal getSetembro() { return setembro; }
    public void setSetembro(BigDecimal setembro) { this.setembro = setembro; }
    public BigDecimal getOutubro() { return outubro; }
    public void setOutubro(BigDecimal outubro) { this.outubro = outubro; }
    public BigDecimal getNovembro() { return novembro; }
    public void setNovembro(BigDecimal novembro) { this.novembro = novembro; }
    public BigDecimal getDezembro() { return dezembro; }
    public void setDezembro(BigDecimal dezembro) { this.dezembro = dezembro; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public static MetaBuilder builder() {
        return new MetaBuilder();
    }

    public static class MetaBuilder {
        private UUID id;
        private Integer ano;
        private CategoriaMeta categoria;
        private TipoMeta tipoMeta;
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

        public MetaBuilder id(UUID id) { this.id = id; return this; }
        public MetaBuilder ano(Integer ano) { this.ano = ano; return this; }
        public MetaBuilder categoria(CategoriaMeta categoria) { this.categoria = categoria; return this; }
        public MetaBuilder tipoMeta(TipoMeta tipoMeta) { this.tipoMeta = tipoMeta; return this; }
        public MetaBuilder valorAnual(BigDecimal valorAnual) { this.valorAnual = valorAnual; return this; }
        public MetaBuilder janeiro(BigDecimal janeiro) { this.janeiro = janeiro; return this; }
        public MetaBuilder fevereiro(BigDecimal fevereiro) { this.fevereiro = fevereiro; return this; }
        public MetaBuilder marco(BigDecimal marco) { this.marco = marco; return this; }
        public MetaBuilder abril(BigDecimal abril) { this.abril = abril; return this; }
        public MetaBuilder maio(BigDecimal maio) { this.maio = maio; return this; }
        public MetaBuilder junho(BigDecimal junho) { this.junho = junho; return this; }
        public MetaBuilder julho(BigDecimal julho) { this.julho = julho; return this; }
        public MetaBuilder agosto(BigDecimal agosto) { this.agosto = agosto; return this; }
        public MetaBuilder setembro(BigDecimal setembro) { this.setembro = setembro; return this; }
        public MetaBuilder outubro(BigDecimal outubro) { this.outubro = outubro; return this; }
        public MetaBuilder novembro(BigDecimal novembro) { this.novembro = novembro; return this; }
        public MetaBuilder dezembro(BigDecimal dezembro) { this.dezembro = dezembro; return this; }
        public MetaBuilder dataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; return this; }
        public MetaBuilder dataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; return this; }

        public Meta build() {
            Meta meta = new Meta();
            meta.setId(id);
            meta.setAno(ano);
            meta.setCategoria(categoria);
            meta.setTipoMeta(tipoMeta);
            meta.setValorAnual(valorAnual);
            meta.setJaneiro(janeiro);
            meta.setFevereiro(fevereiro);
            meta.setMarco(marco);
            meta.setAbril(abril);
            meta.setMaio(maio);
            meta.setJunho(junho);
            meta.setJulho(julho);
            meta.setAgosto(agosto);
            meta.setSetembro(setembro);
            meta.setOutubro(outubro);
            meta.setNovembro(novembro);
            meta.setDezembro(dezembro);
            meta.setDataCriacao(dataCriacao);
            meta.setDataAtualizacao(dataAtualizacao);
            return meta;
        }
    }
}

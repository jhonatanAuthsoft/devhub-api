package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.TipoProjeto;
import com.projeto.modelo.model.enums.TipoVenda;
import com.projeto.modelo.model.enums.StatusProjeto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projeto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String requisitos;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim_desenv")
    private LocalDate dataFimDesenv;

    @Column(name = "data_fim_projeto")
    private LocalDate dataFimProjeto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_projeto", nullable = false)
    private TipoProjeto tipoProjeto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_venda")
    private TipoVenda tipoVenda;

    @ManyToOne
    @JoinColumn(name = "projeto_origem_id")
    private Projeto projetoOrigem;

    @Column(name = "nome_indicacao")
    private String nomeIndicacao;

    @Column(name = "emitir_nf")
    @Builder.Default
    private Boolean emitirNf = false;

    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valor_contrato_mensal", precision = 15, scale = 2)
    private BigDecimal valorContratoMensal;

    @Builder.Default
    @Column(name = "imposto_percentual", precision = 5, scale = 2)
    private BigDecimal impostoPercentual = new BigDecimal("15.00");

    @Column(name = "lucro_percentual", precision = 5, scale = 2)
    private BigDecimal lucroPercentual;

    @Column(name = "valor_desenvolvimento", precision = 15, scale = 2)
    private BigDecimal valorDesenvolvimento;

    @Column(name = "horas_estimadas", precision = 15, scale = 2)
    private BigDecimal horasEstimadas;

    @Builder.Default
    @Column(name = "permite_ultrapassar_horas")
    private Boolean permiteUltrapassarHoras = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusProjeto status = StatusProjeto.PRE_VENDA;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LinkProjeto> links = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ParcelaProjeto> parcelas = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EquipeProjeto> equipe = new java.util.ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Manual Getters and Setters and Builder
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Usuario getVendedor() { return vendedor; }
    public void setVendedor(Usuario vendedor) { this.vendedor = vendedor; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFimDesenv() { return dataFimDesenv; }
    public void setDataFimDesenv(LocalDate dataFimDesenv) { this.dataFimDesenv = dataFimDesenv; }
    public LocalDate getDataFimProjeto() { return dataFimProjeto; }
    public void setDataFimProjeto(LocalDate dataFimProjeto) { this.dataFimProjeto = dataFimProjeto; }
    public TipoProjeto getTipoProjeto() { return tipoProjeto; }
    public void setTipoProjeto(TipoProjeto tipoProjeto) { this.tipoProjeto = tipoProjeto; }
    public TipoVenda getTipoVenda() { return tipoVenda; }
    public void setTipoVenda(TipoVenda tipoVenda) { this.tipoVenda = tipoVenda; }
    public Projeto getProjetoOrigem() { return projetoOrigem; }
    public void setProjetoOrigem(Projeto projetoOrigem) { this.projetoOrigem = projetoOrigem; }
    public String getNomeIndicacao() { return nomeIndicacao; }
    public void setNomeIndicacao(String nomeIndicacao) { this.nomeIndicacao = nomeIndicacao; }
    public Boolean getEmitirNf() { return emitirNf; }
    public void setEmitirNf(Boolean emitirNf) { this.emitirNf = emitirNf; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public BigDecimal getValorContratoMensal() { return valorContratoMensal; }
    public void setValorContratoMensal(BigDecimal valorContratoMensal) { this.valorContratoMensal = valorContratoMensal; }
    public BigDecimal getImpostoPercentual() { return impostoPercentual; }
    public void setImpostoPercentual(BigDecimal impostoPercentual) { this.impostoPercentual = impostoPercentual; }
    public BigDecimal getLucroPercentual() { return lucroPercentual; }
    public void setLucroPercentual(BigDecimal lucroPercentual) { this.lucroPercentual = lucroPercentual; }
    public BigDecimal getValorDesenvolvimento() { return valorDesenvolvimento; }
    public void setValorDesenvolvimento(BigDecimal valorDesenvolvimento) { this.valorDesenvolvimento = valorDesenvolvimento; }
    public BigDecimal getHorasEstimadas() { return horasEstimadas; }
    public void setHorasEstimadas(BigDecimal horasEstimadas) { this.horasEstimadas = horasEstimadas; }
    public Boolean getPermiteUltrapassarHoras() { return permiteUltrapassarHoras; }
    public void setPermiteUltrapassarHoras(Boolean permiteUltrapassarHoras) { this.permiteUltrapassarHoras = permiteUltrapassarHoras; }
    public StatusProjeto getStatus() { return status; }
    public void setStatus(StatusProjeto status) { this.status = status; }
    public List<LinkProjeto> getLinks() { return links; }
    public void setLinks(List<LinkProjeto> links) { this.links = links; }
    public List<ParcelaProjeto> getParcelas() { return parcelas; }
    public void setParcelas(List<ParcelaProjeto> parcelas) { this.parcelas = parcelas; }
    public List<EquipeProjeto> getEquipe() { return equipe; }
    public void setEquipe(List<EquipeProjeto> equipe) { this.equipe = equipe; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static ProjetoBuilder builder() {
        return new ProjetoBuilder();
    }

    public static class ProjetoBuilder {
        private UUID id;
        private String titulo;
        private String descricao;
        private String requisitos;
        private Cliente cliente;
        private Usuario vendedor;
        private LocalDate dataInicio;
        private LocalDate dataFimDesenv;
        private LocalDate dataFimProjeto;
        private TipoProjeto tipoProjeto;
        private TipoVenda tipoVenda;
        private Projeto projetoOrigem;
        private String nomeIndicacao;
        private Boolean emitirNf = false;
        private BigDecimal valorTotal;
        private BigDecimal valorContratoMensal;
        private BigDecimal impostoPercentual = new BigDecimal("15.00");
        private BigDecimal lucroPercentual;
        private BigDecimal valorDesenvolvimento;
        private BigDecimal horasEstimadas;
        private Boolean permiteUltrapassarHoras = false;
        private StatusProjeto status = StatusProjeto.PRE_VENDA;
        private List<LinkProjeto> links = new java.util.ArrayList<>();
        private List<ParcelaProjeto> parcelas = new java.util.ArrayList<>();
        private List<EquipeProjeto> equipe = new java.util.ArrayList<>();

        public ProjetoBuilder id(UUID id) { this.id = id; return this; }
        public ProjetoBuilder titulo(String titulo) { this.titulo = titulo; return this; }
        public ProjetoBuilder descricao(String descricao) { this.descricao = descricao; return this; }
        public ProjetoBuilder requisitos(String requisitos) { this.requisitos = requisitos; return this; }
        public ProjetoBuilder cliente(Cliente cliente) { this.cliente = cliente; return this; }
        public ProjetoBuilder vendedor(Usuario vendedor) { this.vendedor = vendedor; return this; }
        public ProjetoBuilder dataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; return this; }
        public ProjetoBuilder dataFimDesenv(LocalDate dataFimDesenv) { this.dataFimDesenv = dataFimDesenv; return this; }
        public ProjetoBuilder dataFimProjeto(LocalDate dataFimProjeto) { this.dataFimProjeto = dataFimProjeto; return this; }
        public ProjetoBuilder tipoProjeto(TipoProjeto tipoProjeto) { this.tipoProjeto = tipoProjeto; return this; }
        public ProjetoBuilder tipoVenda(TipoVenda tipoVenda) { this.tipoVenda = tipoVenda; return this; }
        public ProjetoBuilder projetoOrigem(Projeto projetoOrigem) { this.projetoOrigem = projetoOrigem; return this; }
        public ProjetoBuilder nomeIndicacao(String nomeIndicacao) { this.nomeIndicacao = nomeIndicacao; return this; }
        public ProjetoBuilder emitirNf(Boolean emitirNf) { this.emitirNf = emitirNf; return this; }
        public ProjetoBuilder valorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; return this; }
        public ProjetoBuilder valorContratoMensal(BigDecimal valorContratoMensal) { this.valorContratoMensal = valorContratoMensal; return this; }
        public ProjetoBuilder impostoPercentual(BigDecimal impostoPercentual) { this.impostoPercentual = impostoPercentual; return this; }
        public ProjetoBuilder lucroPercentual(BigDecimal lucroPercentual) { this.lucroPercentual = lucroPercentual; return this; }
        public ProjetoBuilder valorDesenvolvimento(BigDecimal valorDesenvolvimento) { this.valorDesenvolvimento = valorDesenvolvimento; return this; }
        public ProjetoBuilder horasEstimadas(BigDecimal horasEstimadas) { this.horasEstimadas = horasEstimadas; return this; }
        public ProjetoBuilder permiteUltrapassarHoras(Boolean permiteUltrapassarHoras) { this.permiteUltrapassarHoras = permiteUltrapassarHoras; return this; }
        public ProjetoBuilder status(StatusProjeto status) { this.status = status; return this; }
        public ProjetoBuilder links(List<LinkProjeto> links) { this.links = links; return this; }
        public ProjetoBuilder parcelas(List<ParcelaProjeto> parcelas) { this.parcelas = parcelas; return this; }
        public ProjetoBuilder equipe(List<EquipeProjeto> equipe) { this.equipe = equipe; return this; }

        public Projeto build() {
            Projeto projeto = new Projeto();
            projeto.setId(id);
            projeto.setTitulo(titulo);
            projeto.setDescricao(descricao);
            projeto.setRequisitos(requisitos);
            projeto.setCliente(cliente);
            projeto.setVendedor(vendedor);
            projeto.setDataInicio(dataInicio);
            projeto.setDataFimDesenv(dataFimDesenv);
            projeto.setDataFimProjeto(dataFimProjeto);
            projeto.setTipoProjeto(tipoProjeto);
            projeto.setTipoVenda(tipoVenda);
            projeto.setProjetoOrigem(projetoOrigem);
            projeto.setNomeIndicacao(nomeIndicacao);
            projeto.setEmitirNf(emitirNf);
            projeto.setValorTotal(valorTotal);
            projeto.setValorContratoMensal(valorContratoMensal);
            projeto.setImpostoPercentual(impostoPercentual);
            projeto.setLucroPercentual(lucroPercentual);
            projeto.setValorDesenvolvimento(valorDesenvolvimento);
            projeto.setHorasEstimadas(horasEstimadas);
            projeto.setPermiteUltrapassarHoras(permiteUltrapassarHoras);
            projeto.setStatus(status);
            projeto.setLinks(links);
            projeto.setParcelas(parcelas);
            projeto.setEquipe(equipe);
            return projeto;
        }
    }
}

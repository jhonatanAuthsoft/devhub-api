package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.Periodicidade;
import com.projeto.modelo.model.enums.StatusDespesa;
import com.projeto.modelo.model.enums.TipoRecorrencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "despesa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Despesa extends BaseAuditableEntity {

    @Column(nullable = false)
    private String descricao;

    @Column(name = "valor_previsto", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorPrevisto;

    @Column(name = "valor_pago", precision = 15, scale = 2)
    private BigDecimal valorPago;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDespesa status;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private ContaBancaria conta;

    @ManyToOne
    @JoinColumn(name = "cartao_credito_id")
    private CartaoCredito cartaoCredito;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recorrencia", nullable = false)
    private TipoRecorrencia tipoRecorrencia;

    @ManyToOne
    @JoinColumn(name = "recorrencia_pai_id")
    private Despesa recorrenciaPai;

    @Column(name = "parcela_numero")
    private Integer parcelaNumero;

    @Column(name = "parcela_total")
    private Integer parcelaTotal;

    @Enumerated(EnumType.STRING)
    private Periodicidade periodicidade;
    
    // Explicit Getters and Setters
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public BigDecimal getValorPrevisto() { return valorPrevisto; }
    public void setValorPrevisto(BigDecimal valorPrevisto) { this.valorPrevisto = valorPrevisto; }
    
    public BigDecimal getValorPago() { return valorPago; }
    public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }
    
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    
    public StatusDespesa getStatus() { return status; }
    public void setStatus(StatusDespesa status) { this.status = status; }
    
    public ContaBancaria getConta() { return conta; }
    public void setConta(ContaBancaria conta) { this.conta = conta; }
    
    public CartaoCredito getCartaoCredito() { return cartaoCredito; }
    public void setCartaoCredito(CartaoCredito cartaoCredito) { this.cartaoCredito = cartaoCredito; }
    
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    
    public Projeto getProjeto() { return projeto; }
    public void setProjeto(Projeto projeto) { this.projeto = projeto; }
    
    public TipoRecorrencia getTipoRecorrencia() { return tipoRecorrencia; }
    public void setTipoRecorrencia(TipoRecorrencia tipoRecorrencia) { this.tipoRecorrencia = tipoRecorrencia; }
    
    public Despesa getRecorrenciaPai() { return recorrenciaPai; }
    public void setRecorrenciaPai(Despesa recorrenciaPai) { this.recorrenciaPai = recorrenciaPai; }
    
    public Integer getParcelaNumero() { return parcelaNumero; }
    public void setParcelaNumero(Integer parcelaNumero) { this.parcelaNumero = parcelaNumero; }
    
    public Integer getParcelaTotal() { return parcelaTotal; }
    public void setParcelaTotal(Integer parcelaTotal) { this.parcelaTotal = parcelaTotal; }
    
    public Periodicidade getPeriodicidade() { return periodicidade; }
    public void setPeriodicidade(Periodicidade periodicidade) { this.periodicidade = periodicidade; }
}

package com.projeto.modelo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cartao_credito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartaoCredito extends BaseAuditableEntity {

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal limite;

    @Column(name = "dia_fechamento", nullable = false)
    private Integer diaFechamento;

    @Column(name = "dia_vencimento", nullable = false)
    private Integer diaVencimento;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private ContaBancaria conta;

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getLimite() { return limite; }
    public void setLimite(BigDecimal limite) { this.limite = limite; }

    public Integer getDiaFechamento() { return diaFechamento; }
    public void setDiaFechamento(Integer diaFechamento) { this.diaFechamento = diaFechamento; }

    public Integer getDiaVencimento() { return diaVencimento; }
    public void setDiaVencimento(Integer diaVencimento) { this.diaVencimento = diaVencimento; }

    public ContaBancaria getConta() { return conta; }
    public void setConta(ContaBancaria conta) { this.conta = conta; }
}

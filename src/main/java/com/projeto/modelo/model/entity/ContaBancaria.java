package com.projeto.modelo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "conta_bancaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaBancaria extends BaseAuditableEntity {

    @Column(nullable = false)
    private String nome;

    @Builder.Default
    @Column(name = "saldo_atual", precision = 15, scale = 2)
    private BigDecimal saldoAtual = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "ativo")
    private Boolean ativo = true;

    @Builder.Default
    @Column(name = "emite_boleto")
    private Boolean emiteBoleto = false;

    // Getters and Setters extra
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(BigDecimal saldoAtual) { this.saldoAtual = saldoAtual; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Boolean getEmiteBoleto() { return emiteBoleto; }
    public void setEmiteBoleto(Boolean emiteBoleto) { this.emiteBoleto = emiteBoleto; }
}

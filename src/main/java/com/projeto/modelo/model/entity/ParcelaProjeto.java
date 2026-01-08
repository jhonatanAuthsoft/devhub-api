package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.StatusParcela;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parcela_projeto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelaProjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusParcela status = StatusParcela.PENDENTE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Manual Getters and Setters and Builder
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Projeto getProjeto() { return projeto; }
    public void setProjeto(Projeto projeto) { this.projeto = projeto; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public StatusParcela getStatus() { return status; }
    public void setStatus(StatusParcela status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static ParcelaProjetoBuilder builder() {
        return new ParcelaProjetoBuilder();
    }

    public static class ParcelaProjetoBuilder {
        private UUID id;
        private Projeto projeto;
        private Integer numero;
        private BigDecimal valor;
        private LocalDate dataVencimento;
        private StatusParcela status = StatusParcela.PENDENTE;
        private LocalDateTime createdAt;

        public ParcelaProjetoBuilder id(UUID id) { this.id = id; return this; }
        public ParcelaProjetoBuilder projeto(Projeto projeto) { this.projeto = projeto; return this; }
        public ParcelaProjetoBuilder numero(Integer numero) { this.numero = numero; return this; }
        public ParcelaProjetoBuilder valor(BigDecimal valor) { this.valor = valor; return this; }
        public ParcelaProjetoBuilder dataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; return this; }
        public ParcelaProjetoBuilder status(StatusParcela status) { this.status = status; return this; }
        public ParcelaProjetoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ParcelaProjeto build() {
            ParcelaProjeto parcelaProjeto = new ParcelaProjeto();
            parcelaProjeto.setId(id);
            parcelaProjeto.setProjeto(projeto);
            parcelaProjeto.setNumero(numero);
            parcelaProjeto.setValor(valor);
            parcelaProjeto.setDataVencimento(dataVencimento);
            parcelaProjeto.setStatus(status);
            parcelaProjeto.setCreatedAt(createdAt);
            return parcelaProjeto;
        }
    }
}

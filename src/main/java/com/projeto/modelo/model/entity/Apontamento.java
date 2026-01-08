package com.projeto.modelo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "apontamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apontamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Usuario colaborador;

    @Column(name = "data_apontamento", nullable = false)
    private LocalDate dataApontamento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal horas;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Projeto getProjeto() { return projeto; }
    public void setProjeto(Projeto projeto) { this.projeto = projeto; }
    public Usuario getColaborador() { return colaborador; }
    public void setColaborador(Usuario colaborador) { this.colaborador = colaborador; }
    public LocalDate getDataApontamento() { return dataApontamento; }
    public void setDataApontamento(LocalDate dataApontamento) { this.dataApontamento = dataApontamento; }
    public BigDecimal getHoras() { return horas; }
    public void setHoras(BigDecimal horas) { this.horas = horas; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static ApontamentoBuilder builder() {
        return new ApontamentoBuilder();
    }

    public static class ApontamentoBuilder {
        private UUID id;
        private Projeto projeto;
        private Usuario colaborador;
        private LocalDate dataApontamento;
        private BigDecimal horas;
        private String descricao;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ApontamentoBuilder id(UUID id) { this.id = id; return this; }
        public ApontamentoBuilder projeto(Projeto projeto) { this.projeto = projeto; return this; }
        public ApontamentoBuilder colaborador(Usuario colaborador) { this.colaborador = colaborador; return this; }
        public ApontamentoBuilder dataApontamento(LocalDate dataApontamento) { this.dataApontamento = dataApontamento; return this; }
        public ApontamentoBuilder horas(BigDecimal horas) { this.horas = horas; return this; }
        public ApontamentoBuilder descricao(String descricao) { this.descricao = descricao; return this; }
        public ApontamentoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ApontamentoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Apontamento build() {
            Apontamento apontamento = new Apontamento();
            apontamento.setId(id);
            apontamento.setProjeto(projeto);
            apontamento.setColaborador(colaborador);
            apontamento.setDataApontamento(dataApontamento);
            apontamento.setHoras(horas);
            apontamento.setDescricao(descricao);
            apontamento.setCreatedAt(createdAt);
            apontamento.setUpdatedAt(updatedAt);
            return apontamento;
        }
    }
}

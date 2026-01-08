package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.FuncaoProjeto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "equipe_projeto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipeProjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Usuario colaborador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuncaoProjeto funcao;

    @Column(name = "usa_salario_fixo")
    private Boolean usaSalarioFixo;

    @Column(name = "porcentagem", precision = 5, scale = 2)
    private BigDecimal porcentagem;


    @Column(name = "horas_previstas", precision = 10, scale = 2)
    private BigDecimal horasPrevistas;

    @Column(name = "custo_previsto", precision = 15, scale = 2)
    private BigDecimal custoPrevisto;

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
    public Usuario getColaborador() { return colaborador; }
    public void setColaborador(Usuario colaborador) { this.colaborador = colaborador; }
    public FuncaoProjeto getFuncao() { return funcao; }
    public void setFuncao(FuncaoProjeto funcao) { this.funcao = funcao; }
    public Boolean getUsaSalarioFixo() { return usaSalarioFixo; }
    public void setUsaSalarioFixo(Boolean usaSalarioFixo) { this.usaSalarioFixo = usaSalarioFixo; }
    public BigDecimal getPorcentagem() { return porcentagem; }
    public void setPorcentagem(BigDecimal porcentagem) { this.porcentagem = porcentagem; }
    public BigDecimal getHorasPrevistas() { return horasPrevistas; }
    public void setHorasPrevistas(BigDecimal horasPrevistas) { this.horasPrevistas = horasPrevistas; }
    public BigDecimal getCustoPrevisto() { return custoPrevisto; }
    public void setCustoPrevisto(BigDecimal custoPrevisto) { this.custoPrevisto = custoPrevisto; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static EquipeProjetoBuilder builder() {
        return new EquipeProjetoBuilder();
    }

    public static class EquipeProjetoBuilder {
        private UUID id;
        private Projeto projeto;
        private Usuario colaborador;
        private FuncaoProjeto funcao;
        private Boolean usaSalarioFixo;
        private BigDecimal porcentagem;
        private BigDecimal horasPrevistas;
        private BigDecimal custoPrevisto;
        private LocalDateTime createdAt;

        public EquipeProjetoBuilder id(UUID id) { this.id = id; return this; }
        public EquipeProjetoBuilder projeto(Projeto projeto) { this.projeto = projeto; return this; }
        public EquipeProjetoBuilder colaborador(Usuario colaborador) { this.colaborador = colaborador; return this; }
        public EquipeProjetoBuilder funcao(FuncaoProjeto funcao) { this.funcao = funcao; return this; }
        public EquipeProjetoBuilder usaSalarioFixo(Boolean usaSalarioFixo) { this.usaSalarioFixo = usaSalarioFixo; return this; }
        public EquipeProjetoBuilder porcentagem(BigDecimal porcentagem) { this.porcentagem = porcentagem; return this; }
        public EquipeProjetoBuilder horasPrevistas(BigDecimal horasPrevistas) { this.horasPrevistas = horasPrevistas; return this; }
        public EquipeProjetoBuilder custoPrevisto(BigDecimal custoPrevisto) { this.custoPrevisto = custoPrevisto; return this; }
        public EquipeProjetoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public EquipeProjeto build() {
            EquipeProjeto equipeProjeto = new EquipeProjeto();
            equipeProjeto.setId(id);
            equipeProjeto.setProjeto(projeto);
            equipeProjeto.setColaborador(colaborador);
            equipeProjeto.setFuncao(funcao);
            equipeProjeto.setUsaSalarioFixo(usaSalarioFixo);
            equipeProjeto.setPorcentagem(porcentagem);
            equipeProjeto.setHorasPrevistas(horasPrevistas);
            equipeProjeto.setCustoPrevisto(custoPrevisto);
            equipeProjeto.setCreatedAt(createdAt);
            return equipeProjeto;
        }
    }
}

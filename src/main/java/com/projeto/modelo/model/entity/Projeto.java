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
}

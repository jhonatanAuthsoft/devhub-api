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
}

package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.PlanoContinuidade;
import com.projeto.modelo.model.Servidor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assinaturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assinatura {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "servidor_id", nullable = true)
    private Servidor servidor;

    @ManyToOne
    @JoinColumn(name = "plano_continuidade_id", nullable = true)
    private PlanoContinuidade planoContinuidade;

    @Column(name = "projeto_id")
    private UUID projetoId;

    @Column(name = "identificador_servidor")
    private String identificadorServidor;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_plano", nullable = false)
    private TipoPlano tipoPlano;

    @Column(name = "valor_mensal", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMensal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAssinatura status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TipoPlano {
        MENSAL,
        ANUAL,
        BIANUAL
    }

    public enum StatusAssinatura {
        ATIVA,
        CANCELADA,
        SUSPENSA,
        FINALIZADA
    }
}

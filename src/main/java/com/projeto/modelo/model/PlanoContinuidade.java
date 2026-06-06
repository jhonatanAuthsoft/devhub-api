package com.projeto.modelo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "planos_continuidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoContinuidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricaoDestino;

    @Column(nullable = false)
    private Boolean recomendado = false;

    // Benefícios
    @Column(name = "beneficio1_titulo", nullable = false)
    private String beneficio1Titulo;

    @Column(name = "beneficio1_descricao", columnDefinition = "TEXT")
    private String beneficio1Descricao;

    @Column(name = "beneficio2_titulo", nullable = false)
    private String beneficio2Titulo;

    @Column(name = "beneficio2_descricao", columnDefinition = "TEXT")
    private String beneficio2Descricao;

    @Column(name = "beneficio3_titulo", nullable = false)
    private String beneficio3Titulo;

    @Column(name = "beneficio3_descricao", columnDefinition = "TEXT")
    private String beneficio3Descricao;

    // Planos de Pagamento
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoDoisAnos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUmAno;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoSemFidelidade;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

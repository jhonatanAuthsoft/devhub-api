package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.TipoProjeto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "contrato_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratoTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_projeto", nullable = false, unique = true)
    private TipoProjeto tipoProjeto;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Lob
    @Column(nullable = false)
    private byte[] dados;

    @Column(nullable = false)
    private String contentType;

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

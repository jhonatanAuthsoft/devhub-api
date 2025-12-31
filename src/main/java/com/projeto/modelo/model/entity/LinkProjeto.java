package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.TipoAmbiente;
import com.projeto.modelo.model.enums.ClassificacaoLink;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "link_projeto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkProjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(nullable = false, length = 500)
    private String url;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ambiente")
    private TipoAmbiente tipoAmbiente;

    @Enumerated(EnumType.STRING)
    @Column(name = "classificacao")
    private ClassificacaoLink classificacao;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

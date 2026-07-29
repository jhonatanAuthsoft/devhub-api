package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(name = "aberto_por_id", nullable = false)
    private UUID abertoPorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "aberto_por_tipo", nullable = false)
    private TipoAutor abertoPorTipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_atual", nullable = false)
    private StatusTicket statusAtual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeTicket prioridade;

    @Column(name = "responsavel_atual_id")
    private UUID responsavelAtualId;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketHistoricoStatus> historicoStatus;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketAnexo> anexos;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketComentario> comentarios;

    @PrePersist
    public void prePersist() {
        if (this.criadoEm == null) this.criadoEm = LocalDateTime.now();
        if (this.atualizadoEm == null) this.atualizadoEm = LocalDateTime.now();
        if (this.statusAtual == null) this.statusAtual = StatusTicket.ABERTO;
        if (this.prioridade == null) this.prioridade = PrioridadeTicket.MEDIA;
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}

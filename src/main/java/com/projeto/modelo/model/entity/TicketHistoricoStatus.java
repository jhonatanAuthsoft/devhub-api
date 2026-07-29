package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket_historico_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TicketHistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior")
    private StatusTicket statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false)
    private StatusTicket statusNovo;

    @Column(name = "alterado_por_id", nullable = false)
    private UUID alteradoPorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alterado_por_tipo", nullable = false)
    private TipoAutor alteradoPorTipo;

    @Column(name = "data_alteracao", nullable = false, updatable = false)
    private LocalDateTime dataAlteracao;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "direcionado_para_id")
    private UUID direcionadoParaId;

    @PrePersist
    public void prePersist() {
        if (this.dataAlteracao == null) this.dataAlteracao = LocalDateTime.now();
    }
}

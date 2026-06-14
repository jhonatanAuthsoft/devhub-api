package com.projeto.modelo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "projeto_notificacao_ticket")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProjetoNotificacaoTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "notificar_criacao", nullable = false)
    private Boolean notificarCriacao;

    @Column(name = "notificar_atualizacao", nullable = false)
    private Boolean notificarAtualizacao;
    
    @PrePersist
    public void prePersist() {
        if (this.notificarCriacao == null) this.notificarCriacao = true;
        if (this.notificarAtualizacao == null) this.notificarAtualizacao = true;
    }
}

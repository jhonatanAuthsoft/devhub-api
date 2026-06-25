package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket_anexo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TicketAnexo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAnexo tipo;

    @Column(name = "url_arquivo", nullable = false, length = 500)
    private String urlArquivo;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Column(name = "tamanho_bytes", nullable = false)
    private Long tamanhoBytes;

    @Column(name = "enviado_por_id", nullable = false)
    private UUID enviadoPorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "enviado_por_tipo", nullable = false)
    private TipoAutor enviadoPorTipo;

    @Column(name = "data_envio", nullable = false, updatable = false)
    private LocalDateTime dataEnvio;

    @PrePersist
    public void prePersist() {
        if (this.dataEnvio == null) this.dataEnvio = LocalDateTime.now();
    }
}

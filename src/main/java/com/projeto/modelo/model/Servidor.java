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
@Table(name = "servidores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servidor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusServidor status;

    // Infraestrutura
    @Column(nullable = false)
    private Integer cpuNucleos;

    @Column(nullable = false)
    private Integer ramGb;

    @Column(nullable = false)
    private Integer armazenamentoSsdGb;

    @Column(nullable = false)
    private Integer larguraBandaTb;

    // Serviços Gerenciados
    @Column(nullable = false)
    private Boolean monitoramentoProativo;

    @Column(nullable = false)
    private Boolean backupDiario;

    @Column(nullable = false)
    private Boolean gestaoSeguranca;

    @Column(nullable = false)
    private Boolean suporteEspecializado;

    // SLA
    @Column(nullable = false)
    private Integer slaTempoRespostaHoras;

    // Planos de Pagamento
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal planoDoisAnosValor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal planoUmAnoValor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal planoSemFidelidadeValor;

    // Custos
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal custoServidor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal custoBackupDiario;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum StatusServidor {
        ATIVO, INATIVO
    }
}

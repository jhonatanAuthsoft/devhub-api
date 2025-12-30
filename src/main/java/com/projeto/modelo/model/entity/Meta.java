package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.CategoriaMeta;
import com.projeto.modelo.model.enums.TipoMeta;
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
@Table(name = "meta", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ano", "categoria", "tipo_meta"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriaMeta categoria;

    @Column(name = "tipo_meta", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMeta tipoMeta;

    @Column(name = "valor_anual", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorAnual;

    @Column(name = "janeiro", precision = 15, scale = 2)
    private BigDecimal janeiro;

    @Column(name = "fevereiro", precision = 15, scale = 2)
    private BigDecimal fevereiro;

    @Column(name = "marco", precision = 15, scale = 2)
    private BigDecimal marco;

    @Column(name = "abril", precision = 15, scale = 2)
    private BigDecimal abril;

    @Column(name = "maio", precision = 15, scale = 2)
    private BigDecimal maio;

    @Column(name = "junho", precision = 15, scale = 2)
    private BigDecimal junho;

    @Column(name = "julho", precision = 15, scale = 2)
    private BigDecimal julho;

    @Column(name = "agosto", precision = 15, scale = 2)
    private BigDecimal agosto;

    @Column(name = "setembro", precision = 15, scale = 2)
    private BigDecimal setembro;

    @Column(name = "outubro", precision = 15, scale = 2)
    private BigDecimal outubro;

    @Column(name = "novembro", precision = 15, scale = 2)
    private BigDecimal novembro;

    @Column(name = "dezembro", precision = 15, scale = 2)
    private BigDecimal dezembro;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}

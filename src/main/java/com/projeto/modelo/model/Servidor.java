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

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusServidor getStatus() { return status; }
    public void setStatus(StatusServidor status) { this.status = status; }

    public Integer getCpuNucleos() { return cpuNucleos; }
    public void setCpuNucleos(Integer cpuNucleos) { this.cpuNucleos = cpuNucleos; }

    public Integer getRamGb() { return ramGb; }
    public void setRamGb(Integer ramGb) { this.ramGb = ramGb; }

    public Integer getArmazenamentoSsdGb() { return armazenamentoSsdGb; }
    public void setArmazenamentoSsdGb(Integer armazenamentoSsdGb) { this.armazenamentoSsdGb = armazenamentoSsdGb; }

    public Integer getLarguraBandaTb() { return larguraBandaTb; }
    public void setLarguraBandaTb(Integer larguraBandaTb) { this.larguraBandaTb = larguraBandaTb; }

    public Boolean getMonitoramentoProativo() { return monitoramentoProativo; }
    public void setMonitoramentoProativo(Boolean monitoramentoProativo) { this.monitoramentoProativo = monitoramentoProativo; }

    public Boolean getBackupDiario() { return backupDiario; }
    public void setBackupDiario(Boolean backupDiario) { this.backupDiario = backupDiario; }

    public Boolean getGestaoSeguranca() { return gestaoSeguranca; }
    public void setGestaoSeguranca(Boolean gestaoSeguranca) { this.gestaoSeguranca = gestaoSeguranca; }

    public Boolean getSuporteEspecializado() { return suporteEspecializado; }
    public void setSuporteEspecializado(Boolean suporteEspecializado) { this.suporteEspecializado = suporteEspecializado; }

    public Integer getSlaTempoRespostaHoras() { return slaTempoRespostaHoras; }
    public void setSlaTempoRespostaHoras(Integer slaTempoRespostaHoras) { this.slaTempoRespostaHoras = slaTempoRespostaHoras; }

    public BigDecimal getPlanoDoisAnosValor() { return planoDoisAnosValor; }
    public void setPlanoDoisAnosValor(BigDecimal planoDoisAnosValor) { this.planoDoisAnosValor = planoDoisAnosValor; }

    public BigDecimal getPlanoUmAnoValor() { return planoUmAnoValor; }
    public void setPlanoUmAnoValor(BigDecimal planoUmAnoValor) { this.planoUmAnoValor = planoUmAnoValor; }

    public BigDecimal getPlanoSemFidelidadeValor() { return planoSemFidelidadeValor; }
    public void setPlanoSemFidelidadeValor(BigDecimal planoSemFidelidadeValor) { this.planoSemFidelidadeValor = planoSemFidelidadeValor; }

    public BigDecimal getCustoServidor() { return custoServidor; }
    public void setCustoServidor(BigDecimal custoServidor) { this.custoServidor = custoServidor; }

    public BigDecimal getCustoBackupDiario() { return custoBackupDiario; }
    public void setCustoBackupDiario(BigDecimal custoBackupDiario) { this.custoBackupDiario = custoBackupDiario; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

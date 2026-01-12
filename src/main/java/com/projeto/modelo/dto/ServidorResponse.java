package com.projeto.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServidorResponse {

    private UUID id;
    private String nome;
    private String descricao;
    private String status;

    // Infraestrutura
    private Integer cpuNucleos;
    private Integer ramGb;
    private Integer armazenamentoSsdGb;
    private Integer larguraBandaTb;

    // Serviços Gerenciados
    private Boolean monitoramentoProativo;
    private Boolean backupDiario;
    private Boolean gestaoSeguranca;
    private Boolean suporteEspecializado;

    // SLA
    private Integer slaTempoRespostaHoras;

    // Planos de Pagamento
    private BigDecimal planoDoisAnosValor;
    private BigDecimal planoUmAnoValor;
    private BigDecimal planoSemFidelidadeValor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

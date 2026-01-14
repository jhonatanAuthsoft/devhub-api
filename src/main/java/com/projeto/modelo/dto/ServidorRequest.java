package com.projeto.modelo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServidorRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    private String descricao;

    @NotNull(message = "Status é obrigatório")
    private String status;

    // Infraestrutura
    @NotNull(message = "Número de núcleos de CPU é obrigatório")
    @Min(value = 1, message = "Deve ter pelo menos 1 núcleo de CPU")
    private Integer cpuNucleos;

    @NotNull(message = "RAM é obrigatória")
    @Min(value = 1, message = "Deve ter pelo menos 1 GB de RAM")
    private Integer ramGb;

    @NotNull(message = "Armazenamento SSD é obrigatório")
    @Min(value = 1, message = "Deve ter pelo menos 1 GB de SSD")
    private Integer armazenamentoSsdGb;

    @NotNull(message = "Largura de banda é obrigatória")
    @Min(value = 1, message = "Deve ter pelo menos 1 TB de largura de banda")
    private Integer larguraBandaTb;

    // Serviços Gerenciados
    @NotNull(message = "Monitoramento proativo é obrigatório")
    private Boolean monitoramentoProativo;

    @NotNull(message = "Backup diário é obrigatório")
    private Boolean backupDiario;

    @NotNull(message = "Gestão de segurança é obrigatória")
    private Boolean gestaoSeguranca;

    @NotNull(message = "Suporte especializado é obrigatório")
    private Boolean suporteEspecializado;

    // SLA
    @NotNull(message = "Tempo de resposta SLA é obrigatório")
    @Min(value = 1, message = "Tempo de resposta deve ser pelo menos 1 hora")
    private Integer slaTempoRespostaHoras;

    // Planos de Pagamento
    @NotNull(message = "Valor do plano de 2 anos é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal planoDoisAnosValor;

    @NotNull(message = "Valor do plano de 1 ano é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal planoUmAnoValor;

    @NotNull(message = "Valor do plano sem fidelidade é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal planoSemFidelidadeValor;

    @NotNull(message = "Custo do servidor é obrigatório")
    @DecimalMin(value = "0.00", message = "Custo deve ser maior ou igual a zero")
    private BigDecimal custoServidor;

    @NotNull(message = "Custo do backup diário é obrigatório")
    @DecimalMin(value = "0.00", message = "Custo deve ser maior ou igual a zero")
    private BigDecimal custoBackupDiario;
}

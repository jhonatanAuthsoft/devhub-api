package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.model.enums.Periodicidade;
import com.projeto.modelo.model.enums.TipoRecorrencia;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class ReceitaRequestDTO {
    private String descricao;
    private BigDecimal valorPrevisto; // Utilizado em UNICA ou RECORRENTE
    private LocalDate dataVencimento;
    private UUID categoriaId;
    private UUID projetoId;
    private UUID contaBancariaId;
    
    // Configurações de Recorrência/Parcelamento
    private TipoRecorrencia tipoRecorrencia; // UNICA, RECORRENTE, PARCELADA
    private Periodicidade periodicidade; // null se for parcela personalizada ou unica
    
    // Se PARCELADA
    private String modoDistribuicao; // IGUALITARIO, PERSONALIZADO
    private Integer quantidadeParcelas; 
    private BigDecimal valorTotal; // Usado se IGUALITARIO
    private List<ParcelaPersonalizadaDTO> parcelasPersonalizadas; // Usado se PERSONALIZADO
    
    // Para edição em lote
    private String escopoEdicao; // APENAS_ESTA, ESTA_E_PROXIMAS, TODAS
}

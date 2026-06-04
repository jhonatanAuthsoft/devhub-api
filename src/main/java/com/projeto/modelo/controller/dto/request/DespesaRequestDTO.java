package com.projeto.modelo.controller.dto.request;
import com.projeto.modelo.model.enums.Periodicidade;
import com.projeto.modelo.model.enums.TipoRecorrencia;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class DespesaRequestDTO {
    private String descricao;
    private BigDecimal valorPrevisto;
    private LocalDate dataVencimento;
    private UUID categoriaId;
    private UUID projetoId;
    private UUID contaBancariaId;
    private UUID cartaoCreditoId;
    private UUID colaboradorId;
    private String mesReferencia;
    
    private TipoRecorrencia tipoRecorrencia;
    private Periodicidade periodicidade;
    
    private String modoDistribuicao;
    private Integer quantidadeParcelas; 
    private BigDecimal valorTotal;
    private List<ParcelaPersonalizadaDTO> parcelasPersonalizadas;
    
    private String escopoEdicao;
}

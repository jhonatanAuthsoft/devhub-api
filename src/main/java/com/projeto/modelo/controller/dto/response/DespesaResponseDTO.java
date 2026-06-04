package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.entity.Despesa;
import com.projeto.modelo.model.enums.Periodicidade;
import com.projeto.modelo.model.enums.StatusDespesa;
import com.projeto.modelo.model.enums.TipoRecorrencia;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class DespesaResponseDTO {
    private UUID id;
    private String descricao;
    private BigDecimal valorPrevisto;
    private BigDecimal valorPago;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private StatusDespesa status;
    
    private UUID contaBancariaId;
    private String contaBancariaNome;
    
    private UUID cartaoCreditoId;
    private String cartaoCreditoDescricao;
    
    private UUID categoriaId;
    private String categoriaNome;
    
    private UUID projetoId;
    private String projetoNome;
    
    private UUID colaboradorId;
    private String colaboradorNome;
    
    private String mesReferencia;
    
    private TipoRecorrencia tipoRecorrencia;
    private UUID recorrenciaPaiId;
    private Integer parcelaNumero;
    private Integer parcelaTotal;
    private Periodicidade periodicidade;

    public static DespesaResponseDTO fromEntity(Despesa d) {
        return DespesaResponseDTO.builder()
                .id(d.getId())
                .descricao(d.getDescricao())
                .valorPrevisto(d.getValorPrevisto())
                .valorPago(d.getValorPago())
                .dataVencimento(d.getDataVencimento())
                .dataPagamento(d.getDataPagamento())
                .status(d.getStatus())
                .contaBancariaId(d.getConta() != null ? d.getConta().getId() : null)
                .contaBancariaNome(d.getConta() != null ? d.getConta().getNome() : null)
                .cartaoCreditoId(d.getCartaoCredito() != null ? d.getCartaoCredito().getId() : null)
                .cartaoCreditoDescricao(d.getCartaoCredito() != null ? d.getCartaoCredito().getDescricao() : null)
                .categoriaId(d.getCategoria() != null ? d.getCategoria().getId() : null)
                .categoriaNome(d.getCategoria() != null ? d.getCategoria().getNome() : null)
                .projetoId(d.getProjeto() != null ? d.getProjeto().getId() : null)
                .projetoNome(d.getProjeto() != null ? d.getProjeto().getTitulo() : null)
                .colaboradorId(d.getColaborador() != null ? d.getColaborador().getId() : null)
                .colaboradorNome(d.getColaborador() != null ? d.getColaborador().getNome() : null)
                .mesReferencia(d.getMesReferencia())
                .tipoRecorrencia(d.getTipoRecorrencia())
                .recorrenciaPaiId(d.getRecorrenciaPai() != null ? d.getRecorrenciaPai().getId() : null)
                .parcelaNumero(d.getParcelaNumero())
                .parcelaTotal(d.getParcelaTotal())
                .periodicidade(d.getPeriodicidade())
                .build();
    }
}

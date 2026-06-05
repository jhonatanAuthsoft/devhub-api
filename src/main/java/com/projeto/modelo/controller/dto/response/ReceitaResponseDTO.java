package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.entity.Receita;
import com.projeto.modelo.model.enums.Periodicidade;
import com.projeto.modelo.model.enums.StatusReceita;
import com.projeto.modelo.model.enums.TipoRecorrencia;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ReceitaResponseDTO {
    private UUID id;
    private String descricao;
    private BigDecimal valorPrevisto;
    private BigDecimal valorRecebido;
    private LocalDate dataVencimento;
    private LocalDate dataRecebimento;
    private StatusReceita status;
    
    private UUID contaBancariaId;
    private String contaBancariaNome;
    
    private UUID categoriaId;
    private String categoriaNome;
    
    private UUID projetoId;
    private String projetoNome;
    
    private TipoRecorrencia tipoRecorrencia;
    private UUID recorrenciaPaiId;
    private Integer parcelaNumero;
    private Integer parcelaTotal;
    private Periodicidade periodicidade;

    public static ReceitaResponseDTO fromEntity(Receita r) {
        return ReceitaResponseDTO.builder()
                .id(r.getId())
                .descricao(r.getDescricao())
                .valorPrevisto(r.getValorPrevisto())
                .valorRecebido(r.getValorRecebido())
                .dataVencimento(r.getDataVencimento())
                .dataRecebimento(r.getDataRecebimento())
                .status(r.getStatus())
                .contaBancariaId(r.getConta() != null ? r.getConta().getId() : null)
                .contaBancariaNome(r.getConta() != null ? r.getConta().getNome() : null)
                .categoriaId(r.getCategoria() != null ? r.getCategoria().getId() : null)
                .categoriaNome(r.getCategoria() != null ? r.getCategoria().getNome() : null)
                .projetoId(r.getProjeto() != null ? r.getProjeto().getId() : null)
                .projetoNome(r.getProjeto() != null ? r.getProjeto().getTitulo() : null)
                .tipoRecorrencia(r.getTipoRecorrencia())
                .recorrenciaPaiId(r.getRecorrenciaPai() != null ? r.getRecorrenciaPai().getId() : null)
                .parcelaNumero(r.getParcelaNumero())
                .parcelaTotal(r.getParcelaTotal())
                .periodicidade(r.getPeriodicidade())
                .build();
    }
}

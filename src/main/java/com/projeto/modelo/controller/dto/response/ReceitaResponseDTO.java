package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.entity.Receita;
import com.projeto.modelo.model.enums.Periodicidade;
import com.projeto.modelo.model.enums.StatusReceita;
import com.projeto.modelo.model.enums.TipoRecorrencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getValorPrevisto() { return valorPrevisto; }
    public void setValorPrevisto(BigDecimal valorPrevisto) { this.valorPrevisto = valorPrevisto; }
    public BigDecimal getValorRecebido() { return valorRecebido; }
    public void setValorRecebido(BigDecimal valorRecebido) { this.valorRecebido = valorRecebido; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public LocalDate getDataRecebimento() { return dataRecebimento; }
    public void setDataRecebimento(LocalDate dataRecebimento) { this.dataRecebimento = dataRecebimento; }
    public StatusReceita getStatus() { return status; }
    public void setStatus(StatusReceita status) { this.status = status; }
    public UUID getContaBancariaId() { return contaBancariaId; }
    public void setContaBancariaId(UUID contaBancariaId) { this.contaBancariaId = contaBancariaId; }
    public String getContaBancariaNome() { return contaBancariaNome; }
    public void setContaBancariaNome(String contaBancariaNome) { this.contaBancariaNome = contaBancariaNome; }
    public UUID getCategoriaId() { return categoriaId; }
    public void setCategoriaId(UUID categoriaId) { this.categoriaId = categoriaId; }
    public String getCategoriaNome() { return categoriaNome; }
    public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }
    public UUID getProjetoId() { return projetoId; }
    public void setProjetoId(UUID projetoId) { this.projetoId = projetoId; }
    public String getProjetoNome() { return projetoNome; }
    public void setProjetoNome(String projetoNome) { this.projetoNome = projetoNome; }
    public TipoRecorrencia getTipoRecorrencia() { return tipoRecorrencia; }
    public void setTipoRecorrencia(TipoRecorrencia tipoRecorrencia) { this.tipoRecorrencia = tipoRecorrencia; }
    public UUID getRecorrenciaPaiId() { return recorrenciaPaiId; }
    public void setRecorrenciaPaiId(UUID recorrenciaPaiId) { this.recorrenciaPaiId = recorrenciaPaiId; }
    public Integer getParcelaNumero() { return parcelaNumero; }
    public void setParcelaNumero(Integer parcelaNumero) { this.parcelaNumero = parcelaNumero; }
    public Integer getParcelaTotal() { return parcelaTotal; }
    public void setParcelaTotal(Integer parcelaTotal) { this.parcelaTotal = parcelaTotal; }
    public Periodicidade getPeriodicidade() { return periodicidade; }
    public void setPeriodicidade(Periodicidade periodicidade) { this.periodicidade = periodicidade; }

    public static ReceitaResponseDTOBuilder builder() {
        return new ReceitaResponseDTOBuilder();
    }

    public static class ReceitaResponseDTOBuilder {
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

        public ReceitaResponseDTOBuilder id(UUID id) { this.id = id; return this; }
        public ReceitaResponseDTOBuilder descricao(String descricao) { this.descricao = descricao; return this; }
        public ReceitaResponseDTOBuilder valorPrevisto(BigDecimal valorPrevisto) { this.valorPrevisto = valorPrevisto; return this; }
        public ReceitaResponseDTOBuilder valorRecebido(BigDecimal valorRecebido) { this.valorRecebido = valorRecebido; return this; }
        public ReceitaResponseDTOBuilder dataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; return this; }
        public ReceitaResponseDTOBuilder dataRecebimento(LocalDate dataRecebimento) { this.dataRecebimento = dataRecebimento; return this; }
        public ReceitaResponseDTOBuilder status(StatusReceita status) { this.status = status; return this; }
        public ReceitaResponseDTOBuilder contaBancariaId(UUID contaBancariaId) { this.contaBancariaId = contaBancariaId; return this; }
        public ReceitaResponseDTOBuilder contaBancariaNome(String contaBancariaNome) { this.contaBancariaNome = contaBancariaNome; return this; }
        public ReceitaResponseDTOBuilder categoriaId(UUID categoriaId) { this.categoriaId = categoriaId; return this; }
        public ReceitaResponseDTOBuilder categoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; return this; }
        public ReceitaResponseDTOBuilder projetoId(UUID projetoId) { this.projetoId = projetoId; return this; }
        public ReceitaResponseDTOBuilder projetoNome(String projetoNome) { this.projetoNome = projetoNome; return this; }
        public ReceitaResponseDTOBuilder tipoRecorrencia(TipoRecorrencia tipoRecorrencia) { this.tipoRecorrencia = tipoRecorrencia; return this; }
        public ReceitaResponseDTOBuilder recorrenciaPaiId(UUID recorrenciaPaiId) { this.recorrenciaPaiId = recorrenciaPaiId; return this; }
        public ReceitaResponseDTOBuilder parcelaNumero(Integer parcelaNumero) { this.parcelaNumero = parcelaNumero; return this; }
        public ReceitaResponseDTOBuilder parcelaTotal(Integer parcelaTotal) { this.parcelaTotal = parcelaTotal; return this; }
        public ReceitaResponseDTOBuilder periodicidade(Periodicidade periodicidade) { this.periodicidade = periodicidade; return this; }

        public ReceitaResponseDTO build() {
            ReceitaResponseDTO dto = new ReceitaResponseDTO();
            dto.setId(id);
            dto.setDescricao(descricao);
            dto.setValorPrevisto(valorPrevisto);
            dto.setValorRecebido(valorRecebido);
            dto.setDataVencimento(dataVencimento);
            dto.setDataRecebimento(dataRecebimento);
            dto.setStatus(status);
            dto.setContaBancariaId(contaBancariaId);
            dto.setContaBancariaNome(contaBancariaNome);
            dto.setCategoriaId(categoriaId);
            dto.setCategoriaNome(categoriaNome);
            dto.setProjetoId(projetoId);
            dto.setProjetoNome(projetoNome);
            dto.setTipoRecorrencia(tipoRecorrencia);
            dto.setRecorrenciaPaiId(recorrenciaPaiId);
            dto.setParcelaNumero(parcelaNumero);
            dto.setParcelaTotal(parcelaTotal);
            dto.setPeriodicidade(periodicidade);
            return dto;
        }
    }

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

package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.enums.TipoPessoaVinculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponseDTO {
    private UUID id;
    private UUID clienteId;
    private TipoPessoaVinculo tipoPessoa;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    
    // Campos específicos para CONTATO
    private String cargo;
    private Boolean recebeBoleto;
    private Boolean recebeNf;
    private Boolean recebeContrato;
    private Boolean ativo;
    
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getClienteId() { return clienteId; }
    public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }
    public TipoPessoaVinculo getTipoPessoa() { return tipoPessoa; }
    public void setTipoPessoa(TipoPessoaVinculo tipoPessoa) { this.tipoPessoa = tipoPessoa; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public Boolean getRecebeBoleto() { return recebeBoleto; }
    public void setRecebeBoleto(Boolean recebeBoleto) { this.recebeBoleto = recebeBoleto; }
    public Boolean getRecebeNf() { return recebeNf; }
    public void setRecebeNf(Boolean recebeNf) { this.recebeNf = recebeNf; }
    public Boolean getRecebeContrato() { return recebeContrato; }
    public void setRecebeContrato(Boolean recebeContrato) { this.recebeContrato = recebeContrato; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public static PessoaResponseDTOBuilder builder() {
        return new PessoaResponseDTOBuilder();
    }

    public static class PessoaResponseDTOBuilder {
        private UUID id;
        private UUID clienteId;
        private TipoPessoaVinculo tipoPessoa;
        private String nome;
        private String cpf;
        private String email;
        private String telefone;
        private String cargo;
        private Boolean recebeBoleto;
        private Boolean recebeNf;
        private Boolean recebeContrato;
        private Boolean ativo;
        private LocalDateTime dataCriacao;
        private LocalDateTime dataAtualizacao;

        public PessoaResponseDTOBuilder id(UUID id) { this.id = id; return this; }
        public PessoaResponseDTOBuilder clienteId(UUID clienteId) { this.clienteId = clienteId; return this; }
        public PessoaResponseDTOBuilder tipoPessoa(TipoPessoaVinculo tipoPessoa) { this.tipoPessoa = tipoPessoa; return this; }
        public PessoaResponseDTOBuilder nome(String nome) { this.nome = nome; return this; }
        public PessoaResponseDTOBuilder cpf(String cpf) { this.cpf = cpf; return this; }
        public PessoaResponseDTOBuilder email(String email) { this.email = email; return this; }
        public PessoaResponseDTOBuilder telefone(String telefone) { this.telefone = telefone; return this; }
        public PessoaResponseDTOBuilder cargo(String cargo) { this.cargo = cargo; return this; }
        public PessoaResponseDTOBuilder recebeBoleto(Boolean recebeBoleto) { this.recebeBoleto = recebeBoleto; return this; }
        public PessoaResponseDTOBuilder recebeNf(Boolean recebeNf) { this.recebeNf = recebeNf; return this; }
        public PessoaResponseDTOBuilder recebeContrato(Boolean recebeContrato) { this.recebeContrato = recebeContrato; return this; }
        public PessoaResponseDTOBuilder ativo(Boolean ativo) { this.ativo = ativo; return this; }
        public PessoaResponseDTOBuilder dataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; return this; }
        public PessoaResponseDTOBuilder dataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; return this; }

        public PessoaResponseDTO build() {
            PessoaResponseDTO dto = new PessoaResponseDTO();
            dto.setId(id);
            dto.setClienteId(clienteId);
            dto.setTipoPessoa(tipoPessoa);
            dto.setNome(nome);
            dto.setCpf(cpf);
            dto.setEmail(email);
            dto.setTelefone(telefone);
            dto.setCargo(cargo);
            dto.setRecebeBoleto(recebeBoleto);
            dto.setRecebeNf(recebeNf);
            dto.setRecebeContrato(recebeContrato);
            dto.setAtivo(ativo);
            dto.setDataCriacao(dataCriacao);
            dto.setDataAtualizacao(dataAtualizacao);
            return dto;
        }
    }
}

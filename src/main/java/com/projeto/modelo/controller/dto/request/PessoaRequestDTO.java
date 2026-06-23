package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.model.enums.TipoPessoaVinculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequestDTO {
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
    private Boolean podeAbrirTicket;
    private String senha;

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
    public Boolean getPodeAbrirTicket() { return podeAbrirTicket; }
    public void setPodeAbrirTicket(Boolean podeAbrirTicket) { this.podeAbrirTicket = podeAbrirTicket; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

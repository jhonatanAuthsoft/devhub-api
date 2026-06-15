package com.projeto.modelo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projeto.modelo.model.enums.TipoPessoaVinculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pessoa")
public class Pessoa extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    @Column(name = "tipo_pessoa", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private TipoPessoaVinculo tipoPessoa;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    // Campos específicos para CONTATO
    @Column(name = "cargo", length = 100)
    private String cargo;

    @Column(name = "recebe_boleto")
    @Builder.Default
    private Boolean recebeBoleto = false;

    @Column(name = "recebe_nf")
    @Builder.Default
    private Boolean recebeNf = false;

    @Column(name = "recebe_contrato")
    @Builder.Default
    private Boolean recebeContrato = false;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "senha_hash")
    private String senhaHash;

    @Column(name = "pode_abrir_ticket")
    @Builder.Default
    private Boolean podeAbrirTicket = false;

    @Column(name = "email_verificado")
    @Builder.Default
    private Boolean emailVerificado = false;

    @Column(name = "ultimo_acesso")
    private java.time.LocalDateTime ultimoAcesso;

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
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
    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
    public Boolean getPodeAbrirTicket() { return podeAbrirTicket; }
    public void setPodeAbrirTicket(Boolean podeAbrirTicket) { this.podeAbrirTicket = podeAbrirTicket; }
    public Boolean getEmailVerificado() { return emailVerificado; }
    public void setEmailVerificado(Boolean emailVerificado) { this.emailVerificado = emailVerificado; }
    public java.time.LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
    public void setUltimoAcesso(java.time.LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }

    public static PessoaBuilder builder() {
        return new PessoaBuilder();
    }

    public static class PessoaBuilder {
        private Cliente cliente;
        private TipoPessoaVinculo tipoPessoa;
        private String nome;
        private String cpf;
        private String email;
        private String telefone;
        private String cargo;
        private Boolean recebeBoleto = false;
        private Boolean recebeNf = false;
        private Boolean recebeContrato = false;
        private Boolean ativo = true;
        private String senhaHash;
        private Boolean podeAbrirTicket = false;
        private Boolean emailVerificado = false;
        private java.time.LocalDateTime ultimoAcesso;
        // BaseEntity fields if needed for builder? Usually not for creation mostly.
        // But if needed, we can add them. For now, matching standard use.

        public PessoaBuilder cliente(Cliente cliente) { this.cliente = cliente; return this; }
        public PessoaBuilder tipoPessoa(TipoPessoaVinculo tipoPessoa) { this.tipoPessoa = tipoPessoa; return this; }
        public PessoaBuilder nome(String nome) { this.nome = nome; return this; }
        public PessoaBuilder cpf(String cpf) { this.cpf = cpf; return this; }
        public PessoaBuilder email(String email) { this.email = email; return this; }
        public PessoaBuilder telefone(String telefone) { this.telefone = telefone; return this; }
        public PessoaBuilder cargo(String cargo) { this.cargo = cargo; return this; }
        public PessoaBuilder recebeBoleto(Boolean recebeBoleto) { this.recebeBoleto = recebeBoleto; return this; }
        public PessoaBuilder recebeNf(Boolean recebeNf) { this.recebeNf = recebeNf; return this; }
        public PessoaBuilder recebeContrato(Boolean recebeContrato) { this.recebeContrato = recebeContrato; return this; }
        public PessoaBuilder ativo(Boolean ativo) { this.ativo = ativo; return this; }
        public PessoaBuilder senhaHash(String senhaHash) { this.senhaHash = senhaHash; return this; }
        public PessoaBuilder podeAbrirTicket(Boolean podeAbrirTicket) { this.podeAbrirTicket = podeAbrirTicket; return this; }
        public PessoaBuilder emailVerificado(Boolean emailVerificado) { this.emailVerificado = emailVerificado; return this; }
        public PessoaBuilder ultimoAcesso(java.time.LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; return this; }

        public Pessoa build() {
            Pessoa pessoa = new Pessoa();
            pessoa.setCliente(cliente);
            pessoa.setTipoPessoa(tipoPessoa);
            pessoa.setNome(nome);
            pessoa.setCpf(cpf);
            pessoa.setEmail(email);
            pessoa.setTelefone(telefone);
            pessoa.setCargo(cargo);
            pessoa.setRecebeBoleto(recebeBoleto);
            pessoa.setRecebeNf(recebeNf);
            pessoa.setRecebeContrato(recebeContrato);
            pessoa.setAtivo(ativo);
            pessoa.setSenhaHash(senhaHash);
            pessoa.setPodeAbrirTicket(podeAbrirTicket);
            pessoa.setEmailVerificado(emailVerificado);
            pessoa.setUltimoAcesso(ultimoAcesso);
            return pessoa;
        }
    }
}

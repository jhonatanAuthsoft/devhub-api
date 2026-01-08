package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente extends BaseEntity {

    @Column(name = "tipo_pessoa", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa;

    @Column(name = "cpf_cnpj", nullable = false, unique = true, length = 18)
    private String cpfCnpj;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email_principal", nullable = false)
    private String emailPrincipal;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ClienteStatus status;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    // Campos de Endereço
    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "cep", length = 10)
    private String cep;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "pais", length = 100)
    private String pais;

    // Relacionamentos
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Pessoa> pessoas = new ArrayList<>();

    // Métodos auxiliares para gerenciar relacionamento bidirecional
    public void addPessoa(Pessoa pessoa) {
        pessoas.add(pessoa);
        pessoa.setCliente(this);
    }

    public void removePessoa(Pessoa pessoa) {
        pessoas.remove(pessoa);
        pessoa.setCliente(null);
    }

    // Manual Getters and Setters and Builder
    public TipoPessoa getTipoPessoa() { return tipoPessoa; }
    public void setTipoPessoa(TipoPessoa tipoPessoa) { this.tipoPessoa = tipoPessoa; }
    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmailPrincipal() { return emailPrincipal; }
    public void setEmailPrincipal(String emailPrincipal) { this.emailPrincipal = emailPrincipal; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public ClienteStatus getStatus() { return status; }
    public void setStatus(ClienteStatus status) { this.status = status; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public List<Pessoa> getPessoas() { return pessoas; }
    public void setPessoas(List<Pessoa> pessoas) { this.pessoas = pessoas; }

    public static ClienteBuilder builder() {
        return new ClienteBuilder();
    }

    public static class ClienteBuilder {
        private TipoPessoa tipoPessoa;
        private String cpfCnpj;
        private String nome;
        private String emailPrincipal;
        private String telefone;
        private ClienteStatus status;
        private String observacao;
        private String logradouro;
        private String cidade;
        private String estado;
        private String cep;
        private String bairro;
        private String numero;
        private String complemento;
        private String pais;
        private List<Pessoa> pessoas = new ArrayList<>();

        public ClienteBuilder tipoPessoa(TipoPessoa tipoPessoa) { this.tipoPessoa = tipoPessoa; return this; }
        public ClienteBuilder cpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; return this; }
        public ClienteBuilder nome(String nome) { this.nome = nome; return this; }
        public ClienteBuilder emailPrincipal(String emailPrincipal) { this.emailPrincipal = emailPrincipal; return this; }
        public ClienteBuilder telefone(String telefone) { this.telefone = telefone; return this; }
        public ClienteBuilder status(ClienteStatus status) { this.status = status; return this; }
        public ClienteBuilder observacao(String observacao) { this.observacao = observacao; return this; }
        public ClienteBuilder logradouro(String logradouro) { this.logradouro = logradouro; return this; }
        public ClienteBuilder cidade(String cidade) { this.cidade = cidade; return this; }
        public ClienteBuilder estado(String estado) { this.estado = estado; return this; }
        public ClienteBuilder cep(String cep) { this.cep = cep; return this; }
        public ClienteBuilder bairro(String bairro) { this.bairro = bairro; return this; }
        public ClienteBuilder numero(String numero) { this.numero = numero; return this; }
        public ClienteBuilder complemento(String complemento) { this.complemento = complemento; return this; }
        public ClienteBuilder pais(String pais) { this.pais = pais; return this; }
        public ClienteBuilder pessoas(List<Pessoa> pessoas) { this.pessoas = pessoas; return this; }

        public Cliente build() {
            Cliente cliente = new Cliente();
            cliente.setTipoPessoa(tipoPessoa);
            cliente.setCpfCnpj(cpfCnpj);
            cliente.setNome(nome);
            cliente.setEmailPrincipal(emailPrincipal);
            cliente.setTelefone(telefone);
            cliente.setStatus(status);
            cliente.setObservacao(observacao);
            cliente.setLogradouro(logradouro);
            cliente.setCidade(cidade);
            cliente.setEstado(estado);
            cliente.setCep(cep);
            cliente.setBairro(bairro);
            cliente.setNumero(numero);
            cliente.setComplemento(complemento);
            cliente.setPais(pais);
            cliente.setPessoas(pessoas);
            return cliente;
        }
    }
}

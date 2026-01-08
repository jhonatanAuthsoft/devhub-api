package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {
    private TipoPessoa tipoPessoa;
    private String cpfCnpj;
    private String nome;
    private String emailPrincipal;
    private String telefone;
    private ClienteStatus status;
    private String observacao;
    
    // Campos de Endereço
    private String logradouro;
    private String cidade;
    private String estado;
    private String cep;
    private String bairro;
    private String numero;
    private String complemento;
    private String pais;
    
    // Responsáveis (apenas para CNPJ)
    @Builder.Default
    private List<PessoaRequestDTO> responsaveis = new ArrayList<>();

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
    public List<PessoaRequestDTO> getResponsaveis() { return responsaveis; }
    public void setResponsaveis(List<PessoaRequestDTO> responsaveis) { this.responsaveis = responsaveis; }

    public static ClienteRequestDTOBuilder builder() {
        return new ClienteRequestDTOBuilder();
    }

    public static class ClienteRequestDTOBuilder {
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
        private List<PessoaRequestDTO> responsaveis = new ArrayList<>();

        public ClienteRequestDTOBuilder tipoPessoa(TipoPessoa tipoPessoa) { this.tipoPessoa = tipoPessoa; return this; }
        public ClienteRequestDTOBuilder cpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; return this; }
        public ClienteRequestDTOBuilder nome(String nome) { this.nome = nome; return this; }
        public ClienteRequestDTOBuilder emailPrincipal(String emailPrincipal) { this.emailPrincipal = emailPrincipal; return this; }
        public ClienteRequestDTOBuilder telefone(String telefone) { this.telefone = telefone; return this; }
        public ClienteRequestDTOBuilder status(ClienteStatus status) { this.status = status; return this; }
        public ClienteRequestDTOBuilder observacao(String observacao) { this.observacao = observacao; return this; }
        public ClienteRequestDTOBuilder logradouro(String logradouro) { this.logradouro = logradouro; return this; }
        public ClienteRequestDTOBuilder cidade(String cidade) { this.cidade = cidade; return this; }
        public ClienteRequestDTOBuilder estado(String estado) { this.estado = estado; return this; }
        public ClienteRequestDTOBuilder cep(String cep) { this.cep = cep; return this; }
        public ClienteRequestDTOBuilder bairro(String bairro) { this.bairro = bairro; return this; }
        public ClienteRequestDTOBuilder numero(String numero) { this.numero = numero; return this; }
        public ClienteRequestDTOBuilder complemento(String complemento) { this.complemento = complemento; return this; }
        public ClienteRequestDTOBuilder pais(String pais) { this.pais = pais; return this; }
        public ClienteRequestDTOBuilder responsaveis(List<PessoaRequestDTO> responsaveis) { this.responsaveis = responsaveis; return this; }

        public ClienteRequestDTO build() {
            ClienteRequestDTO dto = new ClienteRequestDTO();
            dto.setTipoPessoa(tipoPessoa);
            dto.setCpfCnpj(cpfCnpj);
            dto.setNome(nome);
            dto.setEmailPrincipal(emailPrincipal);
            dto.setTelefone(telefone);
            dto.setStatus(status);
            dto.setObservacao(observacao);
            dto.setLogradouro(logradouro);
            dto.setCidade(cidade);
            dto.setEstado(estado);
            dto.setCep(cep);
            dto.setBairro(bairro);
            dto.setNumero(numero);
            dto.setComplemento(complemento);
            dto.setPais(pais);
            dto.setResponsaveis(responsaveis);
            return dto;
        }
    }
}

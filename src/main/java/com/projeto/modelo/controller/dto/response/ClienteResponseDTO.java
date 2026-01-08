package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private UUID id;
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
    
    // Pessoas vinculadas
    @Builder.Default
    private List<PessoaResponseDTO> pessoas = new ArrayList<>();
    
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Manual Getters and Setters and Builder
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
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
    public List<PessoaResponseDTO> getPessoas() { return pessoas; }
    public void setPessoas(List<PessoaResponseDTO> pessoas) { this.pessoas = pessoas; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public static ClienteResponseDTOBuilder builder() {
        return new ClienteResponseDTOBuilder();
    }

    public static class ClienteResponseDTOBuilder {
        private UUID id;
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
        private List<PessoaResponseDTO> pessoas = new ArrayList<>();
        private LocalDateTime dataCriacao;
        private LocalDateTime dataAtualizacao;

        public ClienteResponseDTOBuilder id(UUID id) { this.id = id; return this; }
        public ClienteResponseDTOBuilder tipoPessoa(TipoPessoa tipoPessoa) { this.tipoPessoa = tipoPessoa; return this; }
        public ClienteResponseDTOBuilder cpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; return this; }
        public ClienteResponseDTOBuilder nome(String nome) { this.nome = nome; return this; }
        public ClienteResponseDTOBuilder emailPrincipal(String emailPrincipal) { this.emailPrincipal = emailPrincipal; return this; }
        public ClienteResponseDTOBuilder telefone(String telefone) { this.telefone = telefone; return this; }
        public ClienteResponseDTOBuilder status(ClienteStatus status) { this.status = status; return this; }
        public ClienteResponseDTOBuilder observacao(String observacao) { this.observacao = observacao; return this; }
        public ClienteResponseDTOBuilder logradouro(String logradouro) { this.logradouro = logradouro; return this; }
        public ClienteResponseDTOBuilder cidade(String cidade) { this.cidade = cidade; return this; }
        public ClienteResponseDTOBuilder estado(String estado) { this.estado = estado; return this; }
        public ClienteResponseDTOBuilder cep(String cep) { this.cep = cep; return this; }
        public ClienteResponseDTOBuilder bairro(String bairro) { this.bairro = bairro; return this; }
        public ClienteResponseDTOBuilder numero(String numero) { this.numero = numero; return this; }
        public ClienteResponseDTOBuilder complemento(String complemento) { this.complemento = complemento; return this; }
        public ClienteResponseDTOBuilder pais(String pais) { this.pais = pais; return this; }
        public ClienteResponseDTOBuilder pessoas(List<PessoaResponseDTO> pessoas) { this.pessoas = pessoas; return this; }
        public ClienteResponseDTOBuilder dataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; return this; }
        public ClienteResponseDTOBuilder dataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; return this; }

        public ClienteResponseDTO build() {
            ClienteResponseDTO dto = new ClienteResponseDTO();
            dto.setId(id);
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
            dto.setPessoas(pessoas);
            dto.setDataCriacao(dataCriacao);
            dto.setDataAtualizacao(dataAtualizacao);
            return dto;
        }
    }
}

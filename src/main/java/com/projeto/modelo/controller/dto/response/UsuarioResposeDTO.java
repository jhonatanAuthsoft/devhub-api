package com.projeto.modelo.controller.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UsuarioResposeDTO(
    UUID id, 
    String email, 
    String nome,
    String cargo,
    String telefone,
    String status,
    String permissao,
    String chavePix,
    String cep,
    String logradouro,
    String bairro,
    String cidade,
    String estado,
    String numero,
    String complemento,
    String pais,
    java.math.BigDecimal valorFixo,
    java.math.BigDecimal valorHora,
    String razaoSocial,
    String cnpj,
    String cpf,
    String emailAuthsoft
) {

    public static UsuarioResposeDTOBuilder builder() {
        return new UsuarioResposeDTOBuilder();
    }

    public static class UsuarioResposeDTOBuilder {
        private UUID id;
        private String email;
        private String nome;
        private String cargo;
        private String telefone;
        private String status;
        private String permissao;
        private String chavePix;
        private String cep;
        private String logradouro;
        private String bairro;
        private String cidade;
        private String estado;
        private String numero;
        private String complemento;
        private String pais;
        private java.math.BigDecimal valorFixo;
        private java.math.BigDecimal valorHora;
        private String razaoSocial;
        private String cnpj;
        private String cpf;
        private String emailAuthsoft;

        public UsuarioResposeDTOBuilder id(UUID id) { this.id = id; return this; }
        public UsuarioResposeDTOBuilder email(String email) { this.email = email; return this; }
        public UsuarioResposeDTOBuilder nome(String nome) { this.nome = nome; return this; }
        public UsuarioResposeDTOBuilder cargo(String cargo) { this.cargo = cargo; return this; }
        public UsuarioResposeDTOBuilder telefone(String telefone) { this.telefone = telefone; return this; }
        public UsuarioResposeDTOBuilder status(String status) { this.status = status; return this; }
        public UsuarioResposeDTOBuilder permissao(String permissao) { this.permissao = permissao; return this; }
        public UsuarioResposeDTOBuilder chavePix(String chavePix) { this.chavePix = chavePix; return this; }
        public UsuarioResposeDTOBuilder cep(String cep) { this.cep = cep; return this; }
        public UsuarioResposeDTOBuilder logradouro(String logradouro) { this.logradouro = logradouro; return this; }
        public UsuarioResposeDTOBuilder bairro(String bairro) { this.bairro = bairro; return this; }
        public UsuarioResposeDTOBuilder cidade(String cidade) { this.cidade = cidade; return this; }
        public UsuarioResposeDTOBuilder estado(String estado) { this.estado = estado; return this; }
        public UsuarioResposeDTOBuilder numero(String numero) { this.numero = numero; return this; }
        public UsuarioResposeDTOBuilder complemento(String complemento) { this.complemento = complemento; return this; }
        public UsuarioResposeDTOBuilder pais(String pais) { this.pais = pais; return this; }
        public UsuarioResposeDTOBuilder valorFixo(java.math.BigDecimal valorFixo) { this.valorFixo = valorFixo; return this; }
        public UsuarioResposeDTOBuilder valorHora(java.math.BigDecimal valorHora) { this.valorHora = valorHora; return this; }
        public UsuarioResposeDTOBuilder razaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; return this; }
        public UsuarioResposeDTOBuilder cnpj(String cnpj) { this.cnpj = cnpj; return this; }
        public UsuarioResposeDTOBuilder cpf(String cpf) { this.cpf = cpf; return this; }
        public UsuarioResposeDTOBuilder emailAuthsoft(String emailAuthsoft) { this.emailAuthsoft = emailAuthsoft; return this; }

        public UsuarioResposeDTO build() {
            return new UsuarioResposeDTO(
                id, email, nome, cargo, telefone, status, permissao, chavePix, cep, logradouro, bairro,
                cidade, estado, numero, complemento, pais, valorFixo, valorHora, razaoSocial, cnpj, cpf, emailAuthsoft
            );
        }
    }
}

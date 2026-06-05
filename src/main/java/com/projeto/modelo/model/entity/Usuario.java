package com.projeto.modelo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projeto.modelo.model.enums.PermissaoStatus;
import com.projeto.modelo.model.enums.UsuarioStatus;
import com.projeto.modelo.model.enums.TipoContratacao;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario extends BaseEntity  implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UsuarioStatus status;

    @Column(name = "codigo_troca_senha")
    private Integer codigoTrocaSenha;

    @Column(name = "permissao")
    @Enumerated(EnumType.STRING)
    private PermissaoStatus permissao;

    @Column(name = "tipo_contratacao")
    @Enumerated(EnumType.STRING)
    private TipoContratacao tipoContratacao = TipoContratacao.FREELANCER;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "chave_pix")
    private String chavePix;

    @Column(name = "cep")
    private String cep;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "email_authsoft")
    private String emailAuthsoft;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "numero")
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "pais")
    private String pais;

    @Column(name = "valor_fixo")
    private java.math.BigDecimal valorFixo;

    @Column(name = "valor_hora")
    private java.math.BigDecimal valorHora;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + permissao.toString()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return emailAuthsoft != null ? emailAuthsoft : email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Manual Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public UsuarioStatus getStatus() { return status; }
    public void setStatus(UsuarioStatus status) { this.status = status; }
    public Integer getCodigoTrocaSenha() { return codigoTrocaSenha; }
    public void setCodigoTrocaSenha(Integer codigoTrocaSenha) { this.codigoTrocaSenha = codigoTrocaSenha; }
    public PermissaoStatus getPermissao() { return permissao; }
    public void setPermissao(PermissaoStatus permissao) { this.permissao = permissao; }
    public TipoContratacao getTipoContratacao() { return tipoContratacao; }
    public void setTipoContratacao(TipoContratacao tipoContratacao) { this.tipoContratacao = tipoContratacao; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getChavePix() { return chavePix; }
    public void setChavePix(String chavePix) { this.chavePix = chavePix; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmailAuthsoft() { return emailAuthsoft; }
    public void setEmailAuthsoft(String emailAuthsoft) { this.emailAuthsoft = emailAuthsoft; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public java.math.BigDecimal getValorFixo() { return valorFixo; }
    public void setValorFixo(java.math.BigDecimal valorFixo) { this.valorFixo = valorFixo; }
    public java.math.BigDecimal getValorHora() { return valorHora; }
    public void setValorHora(java.math.BigDecimal valorHora) { this.valorHora = valorHora; }

    public static UsuarioBuilder builder() {
        return new UsuarioBuilder();
    }

    public static class UsuarioBuilder {
        private String email;
        private String senha;
        private UsuarioStatus status;
        private Integer codigoTrocaSenha;
        private PermissaoStatus permissao;
        private TipoContratacao tipoContratacao;
        private String nome;
        private String cargo;
        private String telefone;
        private String chavePix;
        private String cep;
        private String logradouro;
        private String bairro;
        private String razaoSocial;
        private String cnpj;
        private String cpf;
        private String emailAuthsoft;
        private String cidade;
        private String estado;
        private String numero;
        private String complemento;
        private String pais;
        private java.math.BigDecimal valorFixo;
        private java.math.BigDecimal valorHora;

        public UsuarioBuilder email(String email) { this.email = email; return this; }
        public UsuarioBuilder senha(String senha) { this.senha = senha; return this; }
        public UsuarioBuilder status(UsuarioStatus status) { this.status = status; return this; }
        public UsuarioBuilder codigoTrocaSenha(Integer codigoTrocaSenha) { this.codigoTrocaSenha = codigoTrocaSenha; return this; }
        public UsuarioBuilder permissao(PermissaoStatus permissao) { this.permissao = permissao; return this; }
        public UsuarioBuilder tipoContratacao(TipoContratacao tipoContratacao) { this.tipoContratacao = tipoContratacao; return this; }
        public UsuarioBuilder nome(String nome) { this.nome = nome; return this; }
        public UsuarioBuilder cargo(String cargo) { this.cargo = cargo; return this; }
        public UsuarioBuilder telefone(String telefone) { this.telefone = telefone; return this; }
        public UsuarioBuilder chavePix(String chavePix) { this.chavePix = chavePix; return this; }
        public UsuarioBuilder cep(String cep) { this.cep = cep; return this; }
        public UsuarioBuilder logradouro(String logradouro) { this.logradouro = logradouro; return this; }
        public UsuarioBuilder bairro(String bairro) { this.bairro = bairro; return this; }
        public UsuarioBuilder razaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; return this; }
        public UsuarioBuilder cnpj(String cnpj) { this.cnpj = cnpj; return this; }
        public UsuarioBuilder cpf(String cpf) { this.cpf = cpf; return this; }
        public UsuarioBuilder emailAuthsoft(String emailAuthsoft) { this.emailAuthsoft = emailAuthsoft; return this; }
        public UsuarioBuilder cidade(String cidade) { this.cidade = cidade; return this; }
        public UsuarioBuilder estado(String estado) { this.estado = estado; return this; }
        public UsuarioBuilder numero(String numero) { this.numero = numero; return this; }
        public UsuarioBuilder complemento(String complemento) { this.complemento = complemento; return this; }
        public UsuarioBuilder pais(String pais) { this.pais = pais; return this; }
        public UsuarioBuilder valorFixo(java.math.BigDecimal valorFixo) { this.valorFixo = valorFixo; return this; }
        public UsuarioBuilder valorHora(java.math.BigDecimal valorHora) { this.valorHora = valorHora; return this; }

        public Usuario build() {
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setSenha(senha);
            usuario.setStatus(status);
            usuario.setCodigoTrocaSenha(codigoTrocaSenha);
            usuario.setPermissao(permissao);
            usuario.setTipoContratacao(tipoContratacao != null ? tipoContratacao : TipoContratacao.FREELANCER);
            usuario.setNome(nome);
            usuario.setCargo(cargo);
            usuario.setTelefone(telefone);
            usuario.setChavePix(chavePix);
            usuario.setCep(cep);
            usuario.setLogradouro(logradouro);
            usuario.setBairro(bairro);
            usuario.setRazaoSocial(razaoSocial);
            usuario.setCnpj(cnpj);
            usuario.setCpf(cpf);
            usuario.setEmailAuthsoft(emailAuthsoft);
            usuario.setCidade(cidade);
            usuario.setEstado(estado);
            usuario.setNumero(numero);
            usuario.setComplemento(complemento);
            usuario.setPais(pais);
            usuario.setValorFixo(valorFixo);
            usuario.setValorHora(valorHora);
            return usuario;
        }
    }
}

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
}

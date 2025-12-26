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

    @Column(name = "email")
    private String email;

    @Column(name = "telefone", nullable = false, length = 20)
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
}

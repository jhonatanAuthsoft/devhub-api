package com.projeto.modelo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import com.projeto.modelo.model.enums.TipoCategoria;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria extends BaseAuditableEntity {

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "pai_id")
    private Categoria pai;

    @OneToMany(mappedBy = "pai")
    @Builder.Default
    private List<Categoria> filhas = new ArrayList<>();

    @Builder.Default
    @Column(name = "pre_configurada")
    private Boolean preConfigurada = false;

    @Builder.Default
    @Column(name = "ativo")
    private Boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TipoCategoria tipo = TipoCategoria.AMBOS;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Categoria getPai() { return pai; }
    public void setPai(Categoria pai) { this.pai = pai; }
    public List<Categoria> getFilhas() { return filhas; }
    public void setFilhas(List<Categoria> filhas) { this.filhas = filhas; }
    public Boolean getPreConfigurada() { return preConfigurada; }
    public void setPreConfigurada(Boolean preConfigurada) { this.preConfigurada = preConfigurada; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public TipoCategoria getTipo() { return tipo; }
    public void setTipo(TipoCategoria tipo) { this.tipo = tipo; }
}

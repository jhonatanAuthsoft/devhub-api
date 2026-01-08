package com.projeto.modelo.model.entity;

import com.projeto.modelo.model.enums.TipoAmbiente;
import com.projeto.modelo.model.enums.ClassificacaoLink;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "link_projeto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkProjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(nullable = false, length = 500)
    private String url;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ambiente")
    private TipoAmbiente tipoAmbiente;

    @Enumerated(EnumType.STRING)
    @Column(name = "classificacao")
    private ClassificacaoLink classificacao;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Manual Getters and Setters and Builder
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Projeto getProjeto() { return projeto; }
    public void setProjeto(Projeto projeto) { this.projeto = projeto; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public TipoAmbiente getTipoAmbiente() { return tipoAmbiente; }
    public void setTipoAmbiente(TipoAmbiente tipoAmbiente) { this.tipoAmbiente = tipoAmbiente; }
    public ClassificacaoLink getClassificacao() { return classificacao; }
    public void setClassificacao(ClassificacaoLink classificacao) { this.classificacao = classificacao; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static LinkProjetoBuilder builder() {
        return new LinkProjetoBuilder();
    }

    public static class LinkProjetoBuilder {
        private UUID id;
        private Projeto projeto;
        private String url;
        private String descricao;
        private TipoAmbiente tipoAmbiente;
        private ClassificacaoLink classificacao;
        private String observacao;
        private LocalDateTime createdAt;

        public LinkProjetoBuilder id(UUID id) { this.id = id; return this; }
        public LinkProjetoBuilder projeto(Projeto projeto) { this.projeto = projeto; return this; }
        public LinkProjetoBuilder url(String url) { this.url = url; return this; }
        public LinkProjetoBuilder descricao(String descricao) { this.descricao = descricao; return this; }
        public LinkProjetoBuilder tipoAmbiente(TipoAmbiente tipoAmbiente) { this.tipoAmbiente = tipoAmbiente; return this; }
        public LinkProjetoBuilder classificacao(ClassificacaoLink classificacao) { this.classificacao = classificacao; return this; }
        public LinkProjetoBuilder observacao(String observacao) { this.observacao = observacao; return this; }
        public LinkProjetoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public LinkProjeto build() {
            LinkProjeto linkProjeto = new LinkProjeto();
            linkProjeto.setId(id);
            linkProjeto.setProjeto(projeto);
            linkProjeto.setUrl(url);
            linkProjeto.setDescricao(descricao);
            linkProjeto.setTipoAmbiente(tipoAmbiente);
            linkProjeto.setClassificacao(classificacao);
            linkProjeto.setObservacao(observacao);
            linkProjeto.setCreatedAt(createdAt);
            return linkProjeto;
        }
    }
}

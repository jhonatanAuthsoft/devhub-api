package com.projeto.modelo.model.enums;

public enum ClassificacaoLink {
    BACKEND("Back-end"),
    FRONTEND("Front-end"),
    IA("Inteligência Artificial"),
    SERVIDOR("Servidor"),
    DOCUMENTO("Documento"),
    VIDEO("Vídeo"),
    PROPOSTA("Proposta");

    private String descricao;

    ClassificacaoLink(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

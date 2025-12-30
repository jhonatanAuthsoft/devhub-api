package com.projeto.modelo.model.enums;

public enum CategoriaMeta {
    RECEITA("Receita R$"),
    LUCRO("Lucro R$"),
    VENDA("Venda R$"),
    PROJETO_NOVO("Projeto Novo (QTD)"),
    SERVIDOR("Servidor R$");

    private final String descricao;

    CategoriaMeta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

package com.projeto.modelo.model.enums;

public enum StatusProjeto {
    PRE_VENDA("Pré-venda"),
    EM_ANDAMENTO("Em Andamento"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");

    private String descricao;

    StatusProjeto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

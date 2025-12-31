package com.projeto.modelo.model.enums;

public enum StatusLink {
    ATIVO("Ativo"),
    INATIVO("Inativo");

    private String descricao;

    StatusLink(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

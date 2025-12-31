package com.projeto.modelo.model.enums;

public enum StatusParcela {
    PENDENTE("Pendente"),
    PAGO("Pago"),
    ATRASADO("Atrasado");

    private String descricao;

    StatusParcela(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

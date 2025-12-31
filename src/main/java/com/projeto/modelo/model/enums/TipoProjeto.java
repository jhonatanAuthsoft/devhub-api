package com.projeto.modelo.model.enums;

public enum TipoProjeto {
    SOB_MEDIDA("Sob Medida"),
    ALOCACAO("Alocação"),
    HORAS_AVULSA("Horas Avulsa");

    private String descricao;

    TipoProjeto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

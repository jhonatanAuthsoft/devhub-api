package com.projeto.modelo.model.enums;

public enum TipoProjeto {
    SOB_MEDIDA("Sob Medida"),
    ALOCACAO("Alocação"),
    HORAS_AVULSA("Horas Avulsa"),
    REPASSE_DE_DEMANDA("Repasse de Demanda");

    private String descricao;

    TipoProjeto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

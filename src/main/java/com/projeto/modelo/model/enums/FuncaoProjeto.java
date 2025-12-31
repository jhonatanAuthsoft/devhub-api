package com.projeto.modelo.model.enums;

public enum FuncaoProjeto {
    DESENVOLVEDOR("Desenvolvedor", 0.70),
    GESTOR_PROJETO("Gestor de Projeto", 0.10),
    DESIGNER("Designer", 0.20);

    private String descricao;
    private double percentualDistribuicao;

    FuncaoProjeto(String descricao, double percentualDistribuicao) {
        this.descricao = descricao;
        this.percentualDistribuicao = percentualDistribuicao;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPercentualDistribuicao() {
        return percentualDistribuicao;
    }
}

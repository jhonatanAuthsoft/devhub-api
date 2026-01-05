package com.projeto.modelo.model.enums;

public enum StatusProjeto {
    PRE_VENDA("Pré-venda"),
    EM_ANDAMENTO("Em Andamento"),
    FINALIZADO("Finalizado"),
    AGUARDANDO_ASSINATURA("Aguardando Assinatura do Contrato"),
    EM_GARANTIA("Em Garantia"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private String descricao;

    StatusProjeto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

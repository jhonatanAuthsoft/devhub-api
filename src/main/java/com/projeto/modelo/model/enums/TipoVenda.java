package com.projeto.modelo.model.enums;

public enum TipoVenda {
    UPSELL("Upsell"),
    WORKANA("Workana"),
    GOOGLE("Google"),
    BNI("BNI"),
    INDICACAO("Indicação"),
    META_ADS("Meta ADS"),
    LINKEDIN("LinkedIn");

    private String descricao;

    TipoVenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

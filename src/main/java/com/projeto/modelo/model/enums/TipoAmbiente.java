package com.projeto.modelo.model.enums;

public enum TipoAmbiente {
    GIT("Git"),
    STAGING("Staging"),
    HOMOLOGACAO("Homologação"),
    PRODUCAO("Produção"),
    DOCUMENTACAO("Documentação");

    private String descricao;

    TipoAmbiente(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

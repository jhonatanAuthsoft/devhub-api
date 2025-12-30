package com.projeto.modelo.model.enums;

public enum TipoMeta {
    META_NORMAL("Meta Normal"),
    SUPER_META("SuperMeta"),
    HIPER_META("HiperMeta");

    private final String descricao;

    TipoMeta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

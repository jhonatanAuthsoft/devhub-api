package com.projeto.modelo.model.enums;

public enum TipoProjeto {
    SOB_MEDIDA("Sob Medida"),
    ALOCACAO("Alocação"),
    HORAS_AVULSA("Horas Avulsa"),
    REPASSE_DE_DEMANDA("Repasse de Demanda"),
    SUSTENTACAO("Sustentação"),
    SERVIDOR("Servidor"),
    VENDA_HORA("Venda de Hora"),
    SOFTWARE_SOB_MEDIDA("Software Sob Medida"),
    TERMO_ENTREGA_PRODUCAO("Termo de Entrega em Produção"),
    TERMO_FIM_DESENVOLVIMENTO("Termo de Fim de Desenvolvimento"),
    TERMO_VALIDACAO_REQUSITOS("Termo de Validação de Requisitos");

    private String descricao;

    TipoProjeto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

ALTER TABLE categoria ADD COLUMN IF NOT EXISTS tipo VARCHAR(255);

CREATE TABLE IF NOT EXISTS despesa (
    id UUID PRIMARY KEY,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por_id UUID,
    atualizado_por_id UUID,
    excluido_em TIMESTAMP,
    descricao VARCHAR(255) NOT NULL,
    valor_previsto NUMERIC(15,2) NOT NULL,
    valor_pago NUMERIC(15,2),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    status VARCHAR(255) NOT NULL,
    conta_id UUID,
    categoria_id UUID NOT NULL,
    projeto_id UUID,
    tipo_recorrencia VARCHAR(255) NOT NULL,
    recorrencia_pai_id UUID,
    parcela_numero INTEGER,
    parcela_total INTEGER,
    periodicidade VARCHAR(255),
    CONSTRAINT fk_despesa_conta FOREIGN KEY (conta_id) REFERENCES conta_bancaria (id),
    CONSTRAINT fk_despesa_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id),
    CONSTRAINT fk_despesa_projeto FOREIGN KEY (projeto_id) REFERENCES projeto (id),
    CONSTRAINT fk_despesa_recorrencia_pai FOREIGN KEY (recorrencia_pai_id) REFERENCES despesa (id)
);

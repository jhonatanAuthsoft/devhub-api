CREATE TABLE cartao_credito (
    id UUID PRIMARY KEY,
    conta_id UUID NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    limite DECIMAL(15,2) NOT NULL,
    dia_fechamento INT NOT NULL,
    dia_vencimento INT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    CONSTRAINT fk_cartao_conta FOREIGN KEY (conta_id) REFERENCES conta_bancaria (id)
);

ALTER TABLE despesa ADD COLUMN cartao_credito_id UUID;
ALTER TABLE despesa ADD CONSTRAINT fk_despesa_cartao FOREIGN KEY (cartao_credito_id) REFERENCES cartao_credito (id);

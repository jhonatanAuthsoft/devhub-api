CREATE TABLE pessoa (
    id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    cliente_id UUID NOT NULL,
    tipo_pessoa VARCHAR(15) NOT NULL CHECK (tipo_pessoa IN ('RESPONSAVEL', 'CONTATO')),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    telefone VARCHAR(20) NOT NULL,
    
    -- Campos específicos para CONTATO
    cargo VARCHAR(100),
    recebe_boleto BOOLEAN DEFAULT FALSE,
    recebe_nf BOOLEAN DEFAULT FALSE,
    
    -- Auditoria
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    
    -- Foreign Key
    CONSTRAINT fk_pessoa_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
);

CREATE INDEX idx_pessoa_cliente_id ON pessoa(cliente_id);
CREATE INDEX idx_pessoa_tipo ON pessoa(tipo_pessoa);
CREATE INDEX idx_pessoa_recebe_boleto ON pessoa(recebe_boleto) WHERE recebe_boleto = TRUE;
CREATE INDEX idx_pessoa_recebe_nf ON pessoa(recebe_nf) WHERE recebe_nf = TRUE;

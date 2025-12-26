CREATE TABLE cliente (
    id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    tipo_pessoa VARCHAR(10) NOT NULL CHECK (tipo_pessoa IN ('CPF', 'CNPJ')),
    cpf_cnpj VARCHAR(18) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    email_principal VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    observacao TEXT,
    
    -- Campos de Endereço
    logradouro VARCHAR(255),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    
    -- Auditoria
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP
);

CREATE INDEX idx_cliente_cpf_cnpj ON cliente(cpf_cnpj);
CREATE INDEX idx_cliente_status ON cliente(status);
CREATE INDEX idx_cliente_tipo_pessoa ON cliente(tipo_pessoa);

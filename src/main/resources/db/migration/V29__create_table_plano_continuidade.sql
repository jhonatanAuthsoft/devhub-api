CREATE TABLE planos_continuidade (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao_destino TEXT,
    recomendado BOOLEAN NOT NULL DEFAULT FALSE,
    beneficio1_titulo VARCHAR(255) NOT NULL,
    beneficio1_descricao TEXT,
    beneficio2_titulo VARCHAR(255) NOT NULL,
    beneficio2_descricao TEXT,
    beneficio3_titulo VARCHAR(255) NOT NULL,
    beneficio3_descricao TEXT,
    preco_dois_anos NUMERIC(10,2) NOT NULL,
    preco_um_ano NUMERIC(10,2) NOT NULL,
    preco_sem_fidelidade NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

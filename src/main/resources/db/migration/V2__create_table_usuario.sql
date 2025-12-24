CREATE TABLE usuario (
                         id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
                         email VARCHAR(255) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         data_atualizacao TIMESTAMP,
                         codigo_troca_senha INTEGER,
                         permissao VARCHAR(255),
                         enabled BOOLEAN NOT NULL DEFAULT true
);





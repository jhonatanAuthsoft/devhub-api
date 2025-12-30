-- Criar tabela meta para armazenar metas anuais e mensais
CREATE TABLE meta (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ano INTEGER NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    tipo_meta VARCHAR(50) NOT NULL,
    valor_anual DECIMAL(15, 2) NOT NULL,
    janeiro DECIMAL(15, 2),
    fevereiro DECIMAL(15, 2),
    marco DECIMAL(15, 2),
    abril DECIMAL(15, 2),
    maio DECIMAL(15, 2),
    junho DECIMAL(15, 2),
    julho DECIMAL(15, 2),
    agosto DECIMAL(15, 2),
    setembro DECIMAL(15, 2),
    outubro DECIMAL(15, 2),
    novembro DECIMAL(15, 2),
    dezembro DECIMAL(15, 2),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    CONSTRAINT meta_unique_ano_categoria_tipo UNIQUE (ano, categoria, tipo_meta)
);

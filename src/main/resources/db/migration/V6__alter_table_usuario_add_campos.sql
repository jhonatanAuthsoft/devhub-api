-- Adicionar campos de informações pessoais e endereço na tabela usuario
ALTER TABLE usuario
ADD COLUMN nome VARCHAR(255),
ADD COLUMN cargo VARCHAR(100),
ADD COLUMN telefone VARCHAR(20),
ADD COLUMN chave_pix VARCHAR(255),
ADD COLUMN cep VARCHAR(10),
ADD COLUMN logradouro VARCHAR(255),
ADD COLUMN cidade VARCHAR(100),
ADD COLUMN estado VARCHAR(2);

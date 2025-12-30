-- Adicionar campos de valores (fixo e por hora) na tabela usuario
ALTER TABLE usuario
ADD COLUMN valor_fixo DECIMAL(10, 2),
ADD COLUMN valor_hora DECIMAL(10, 2);

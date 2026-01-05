-- Adiciona coluna permite_ultrapassar_horas na tabela projeto
ALTER TABLE projeto ADD COLUMN permite_ultrapassar_horas BOOLEAN DEFAULT FALSE;

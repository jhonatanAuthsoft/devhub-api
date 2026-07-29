-- Remover FK de direcionado_para_id e responsavel_atual_id para permitir apontar para Usuario ou Pessoa (Contato)
ALTER TABLE ticket_historico_status DROP CONSTRAINT IF EXISTS fk_historico_direcionado;
ALTER TABLE ticket DROP CONSTRAINT IF EXISTS fk_ticket_responsavel;

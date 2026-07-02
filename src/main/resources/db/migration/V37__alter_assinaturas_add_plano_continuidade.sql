ALTER TABLE assinaturas ALTER COLUMN servidor_id DROP NOT NULL;
ALTER TABLE assinaturas ADD COLUMN IF NOT EXISTS plano_continuidade_id UUID;
ALTER TABLE assinaturas ADD COLUMN IF NOT EXISTS projeto_id UUID;
ALTER TABLE assinaturas ADD CONSTRAINT fk_assinatura_plano_cont FOREIGN KEY (plano_continuidade_id) REFERENCES planos_continuidade(id);
ALTER TABLE assinaturas ADD CONSTRAINT fk_assinatura_projeto FOREIGN KEY (projeto_id) REFERENCES projeto(id);
ALTER TABLE assinaturas ADD CONSTRAINT chk_assinatura_serv_ou_plano CHECK ( (servidor_id IS NOT NULL AND plano_continuidade_id IS NULL) OR (servidor_id IS NULL AND plano_continuidade_id IS NOT NULL) );

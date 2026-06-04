ALTER TABLE despesa ADD COLUMN colaborador_id UUID;
ALTER TABLE despesa ADD COLUMN mes_referencia VARCHAR(7);

ALTER TABLE despesa ADD CONSTRAINT fk_despesa_colaborador FOREIGN KEY (colaborador_id) REFERENCES usuario(id);

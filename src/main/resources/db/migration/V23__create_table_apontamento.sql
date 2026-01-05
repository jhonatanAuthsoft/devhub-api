CREATE TABLE apontamento (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    projeto_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    data_apontamento DATE NOT NULL,
    horas DECIMAL(10, 2) NOT NULL,
    descricao TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_apontamento_projeto FOREIGN KEY (projeto_id) REFERENCES projeto(id),
    CONSTRAINT fk_apontamento_usuario FOREIGN KEY (colaborador_id) REFERENCES usuario(id)
);

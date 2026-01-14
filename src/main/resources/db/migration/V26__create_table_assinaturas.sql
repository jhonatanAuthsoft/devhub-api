CREATE TABLE assinaturas (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    servidor_id UUID NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    tipo_plano VARCHAR(20) NOT NULL,
    valor_mensal NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    
    CONSTRAINT fk_assinatura_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT fk_assinatura_servidor FOREIGN KEY (servidor_id) REFERENCES servidores(id)
);

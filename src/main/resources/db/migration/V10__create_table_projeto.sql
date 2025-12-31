CREATE TABLE projeto (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    requisitos TEXT,
    cliente_id UUID NOT NULL,
    vendedor_id UUID,
    data_inicio DATE,
    data_fim_desenv DATE,
    data_fim_projeto DATE,
    tipo_projeto VARCHAR(50) NOT NULL,
    tipo_venda VARCHAR(50),
    projeto_origem_id UUID,
    nome_indicacao VARCHAR(255),
    valor_total DECIMAL(15, 2),
    valor_contrato_mensal DECIMAL(15, 2),
    imposto_percentual DECIMAL(5, 2) DEFAULT 15.00,
    lucro_percentual DECIMAL(5, 2),
    valor_desenvolvimento DECIMAL(15, 2),
    status VARCHAR(50) DEFAULT 'PRE_VENDA',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES pessoa(id),
    FOREIGN KEY (vendedor_id) REFERENCES usuario(id),
    FOREIGN KEY (projeto_origem_id) REFERENCES projeto(id)
);

CREATE TABLE link_projeto (
    id UUID PRIMARY KEY,
    projeto_id UUID NOT NULL,
    url VARCHAR(500) NOT NULL,
    descricao VARCHAR(255),
    tipo_ambiente VARCHAR(50),
    classificacao VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (projeto_id) REFERENCES projeto(id)
);

CREATE TABLE parcela_projeto (
    id UUID PRIMARY KEY,
    projeto_id UUID NOT NULL,
    numero INT NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    data_vencimento DATE,
    status VARCHAR(50) DEFAULT 'PENDENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (projeto_id) REFERENCES projeto(id)
);

CREATE TABLE equipe_projeto (
    id UUID PRIMARY KEY,
    projeto_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    funcao VARCHAR(50) NOT NULL,
    usa_salario_fixo BOOLEAN DEFAULT FALSE,
    horas_previstas DECIMAL(10, 2),
    custo_previsto DECIMAL(15, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (projeto_id) REFERENCES projeto(id),
    FOREIGN KEY (colaborador_id) REFERENCES usuario(id) -- Assumindo que colaboradores sao usuarios por enquanto
);

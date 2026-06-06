CREATE TABLE IF NOT EXISTS categoria (
    id UUID PRIMARY KEY,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por_id UUID,
    atualizado_por_id UUID,
    excluido_em TIMESTAMP,
    nome VARCHAR(255) NOT NULL,
    pai_id UUID,
    pre_configurada BOOLEAN DEFAULT FALSE,
    ativo BOOLEAN DEFAULT TRUE,
    tipo VARCHAR(255) DEFAULT 'AMBOS',
    CONSTRAINT fk_categoria_pai FOREIGN KEY (pai_id) REFERENCES categoria (id)
);

CREATE TABLE IF NOT EXISTS conta_bancaria (
    id UUID PRIMARY KEY,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por_id UUID,
    atualizado_por_id UUID,
    excluido_em TIMESTAMP,
    nome VARCHAR(255) NOT NULL,
    saldo_atual NUMERIC(15,2) DEFAULT 0,
    ativo BOOLEAN DEFAULT TRUE,
    emite_boleto BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS receita (
    id UUID PRIMARY KEY,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por_id UUID,
    atualizado_por_id UUID,
    excluido_em TIMESTAMP,
    descricao VARCHAR(255) NOT NULL,
    valor_previsto NUMERIC(15,2) NOT NULL,
    valor_recebido NUMERIC(15,2),
    data_vencimento DATE NOT NULL,
    data_recebimento DATE,
    status VARCHAR(255) NOT NULL,
    conta_id UUID,
    categoria_id UUID NOT NULL,
    projeto_id UUID,
    cliente_id UUID,
    tipo_recorrencia VARCHAR(255) NOT NULL,
    recorrencia_pai_id UUID,
    parcela_numero INTEGER,
    parcela_total INTEGER,
    periodicidade VARCHAR(255),
    CONSTRAINT fk_receita_conta FOREIGN KEY (conta_id) REFERENCES conta_bancaria (id),
    CONSTRAINT fk_receita_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id),
    CONSTRAINT fk_receita_projeto FOREIGN KEY (projeto_id) REFERENCES projeto (id),
    CONSTRAINT fk_receita_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id),
    CONSTRAINT fk_receita_recorrencia_pai FOREIGN KEY (recorrencia_pai_id) REFERENCES receita (id)
);

CREATE TABLE IF NOT EXISTS audit_log (
    id UUID PRIMARY KEY,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por_id UUID,
    atualizado_por_id UUID,
    excluido_em TIMESTAMP,
    entidade VARCHAR(255) NOT NULL,
    entidade_id UUID NOT NULL,
    acao VARCHAR(255) NOT NULL,
    usuario_id UUID NOT NULL,
    detalhes TEXT
);

CREATE TABLE IF NOT EXISTS contrato_template (
    id UUID PRIMARY KEY,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por_id UUID,
    atualizado_por_id UUID,
    excluido_em TIMESTAMP,
    nome VARCHAR(255) NOT NULL,
    conteudo TEXT NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS notificacao (
    id UUID PRIMARY KEY,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por_id UUID,
    atualizado_por_id UUID,
    excluido_em TIMESTAMP,
    usuario_id UUID NOT NULL,
    mensagem TEXT NOT NULL,
    lida BOOLEAN DEFAULT FALSE,
    tipo VARCHAR(255),
    entidade_id UUID,
    CONSTRAINT fk_notificacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

-- Alteração na tabela de contatos do cliente (pessoa)
ALTER TABLE pessoa 
    ADD COLUMN senha_hash VARCHAR(255) NULL,
    ADD COLUMN pode_abrir_ticket BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN email_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN ultimo_acesso TIMESTAMP NULL;

-- Criar tabela "ticket"
CREATE TABLE ticket (
    id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    projeto_id UUID NOT NULL,
    aberto_por_id UUID NOT NULL,
    aberto_por_tipo VARCHAR(20) NOT NULL CHECK (aberto_por_tipo IN ('CONTATO_CLIENTE', 'EQUIPE_TECNICA')),
    status_atual VARCHAR(30) NOT NULL DEFAULT 'ABERTO' CHECK (status_atual IN ('ABERTO', 'EM_ANDAMENTO', 'BLOQUEADO', 'EM_TESTE_INTERNO', 'EM_TESTE_CLIENTE', 'APROVADO_CLIENTE', 'FINALIZADO')),
    prioridade VARCHAR(10) NOT NULL DEFAULT 'MEDIA',
    responsavel_atual_id UUID NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ticket_projeto FOREIGN KEY (projeto_id) REFERENCES projeto(id),
    CONSTRAINT fk_ticket_responsavel FOREIGN KEY (responsavel_atual_id) REFERENCES usuario(id)
);

-- Criar tabela "ticket_historico_status"
CREATE TABLE ticket_historico_status (
    id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    ticket_id UUID NOT NULL,
    status_anterior VARCHAR(30) NULL,
    status_novo VARCHAR(30) NOT NULL,
    alterado_por_id UUID NOT NULL,
    alterado_por_tipo VARCHAR(20) NOT NULL,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacao TEXT NULL,
    direcionado_para_id UUID NULL,
    CONSTRAINT fk_historico_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE,
    CONSTRAINT fk_historico_direcionado FOREIGN KEY (direcionado_para_id) REFERENCES usuario(id)
);

-- Criar tabela "ticket_anexo"
CREATE TABLE ticket_anexo (
    id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    ticket_id UUID NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('FOTO', 'VIDEO')),
    url_arquivo VARCHAR(500) NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    tamanho_bytes BIGINT NOT NULL,
    enviado_por_id UUID NOT NULL,
    enviado_por_tipo VARCHAR(20) NOT NULL,
    data_envio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_anexo_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE
);

-- Criar tabela "ticket_comentario"
CREATE TABLE ticket_comentario (
    id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    ticket_id UUID NOT NULL,
    autor_id UUID NOT NULL,
    autor_tipo VARCHAR(20) NOT NULL,
    texto TEXT NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comentario_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE
);

-- Criar tabela "projeto_notificacao_ticket"
CREATE TABLE projeto_notificacao_ticket (
    id UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    projeto_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    notificar_criacao BOOLEAN NOT NULL DEFAULT TRUE,
    notificar_atualizacao BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_notificacao_projeto FOREIGN KEY (projeto_id) REFERENCES projeto(id) ON DELETE CASCADE,
    CONSTRAINT fk_notificacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT uk_projeto_usuario UNIQUE (projeto_id, usuario_id)
);

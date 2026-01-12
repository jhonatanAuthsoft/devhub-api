-- Criação da tabela de servidores
CREATE TABLE servidores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    
    -- Infraestrutura
    cpu_nucleos INTEGER NOT NULL CHECK (cpu_nucleos >= 1),
    ram_gb INTEGER NOT NULL CHECK (ram_gb >= 1),
    armazenamento_ssd_gb INTEGER NOT NULL CHECK (armazenamento_ssd_gb >= 1),
    largura_banda_tb INTEGER NOT NULL CHECK (largura_banda_tb >= 1),
    
    -- Serviços Gerenciados
    monitoramento_proativo BOOLEAN NOT NULL DEFAULT FALSE,
    backup_diario BOOLEAN NOT NULL DEFAULT FALSE,
    gestao_seguranca BOOLEAN NOT NULL DEFAULT FALSE,
    suporte_especializado BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- SLA
    sla_tempo_resposta_horas INTEGER NOT NULL CHECK (sla_tempo_resposta_horas >= 1),
    
    -- Planos de Pagamento
    plano_dois_anos_valor DECIMAL(10, 2) NOT NULL CHECK (plano_dois_anos_valor > 0),
    plano_um_ano_valor DECIMAL(10, 2) NOT NULL CHECK (plano_um_ano_valor > 0),
    plano_sem_fidelidade_valor DECIMAL(10, 2) NOT NULL CHECK (plano_sem_fidelidade_valor > 0),
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhorar performance
CREATE INDEX idx_servidores_status ON servidores(status);
CREATE INDEX idx_servidores_nome ON servidores(nome);

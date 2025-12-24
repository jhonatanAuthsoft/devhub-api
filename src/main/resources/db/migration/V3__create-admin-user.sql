INSERT INTO usuario (email,
                     senha,
                     status,
                     data_criacao,
                     data_atualizacao,
                     permissao)
VALUES ('jhonatan.kramer@authsoftsolutions.com',
        '$2a$10$tIjT8HVHlREPZvyardEUi.aRdW2V5ONg.sQ.wSlEZzh2DzhytdVRS',
        'ATIVO',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        'ADMIN') ON CONFLICT (email) DO NOTHING;

-- senha 123456
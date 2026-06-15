package com.projeto.modelo.configuracao.exeption;

import org.springframework.http.HttpStatus;

public class AcessoNaoAutorizadoException extends ExcecoesCustomizada {
    public AcessoNaoAutorizadoException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

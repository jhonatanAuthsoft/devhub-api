package com.projeto.modelo.configuracao.exeption;

import org.springframework.http.HttpStatus;

public class TransicaoNaoPermitidaException extends ExcecoesCustomizada {
    public TransicaoNaoPermitidaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

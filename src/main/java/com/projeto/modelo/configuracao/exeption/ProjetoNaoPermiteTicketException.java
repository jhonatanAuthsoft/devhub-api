package com.projeto.modelo.configuracao.exeption;

import org.springframework.http.HttpStatus;

public class ProjetoNaoPermiteTicketException extends ExcecoesCustomizada {
    public ProjetoNaoPermiteTicketException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

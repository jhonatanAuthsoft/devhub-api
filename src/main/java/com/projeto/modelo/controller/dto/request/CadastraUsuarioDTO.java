package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.configuracao.exeption.ExcecoesCustomizada;
import com.projeto.modelo.util.StringUtils;
import org.springframework.http.HttpStatus;

public record CadastraUsuarioDTO(
        String email, 
        String nome, 
        String senha,
        String cargo,
        String telefone,
        String chavePix,
        String cep,
        String logradouro,
        String cidade,
        String estado,
        java.math.BigDecimal valorFixo,
        java.math.BigDecimal valorHora
) {
    public CadastraUsuarioDTO {
        if (StringUtils.isNullOrEmpty(email)) {
            throw new ExcecoesCustomizada("email não pode ficar em branco", HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isNullOrEmpty(nome)) {
            throw new ExcecoesCustomizada("nome não pode ficar em branco", HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isNullOrEmpty(senha)) {
            throw new ExcecoesCustomizada("senha não pode ficar em branco", HttpStatus.BAD_REQUEST);
        }
    }
}


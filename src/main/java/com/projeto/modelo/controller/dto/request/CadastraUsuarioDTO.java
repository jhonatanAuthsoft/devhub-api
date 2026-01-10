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
        String permissao,
        String chavePix,
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String estado,
        String numero,
        String complemento,
        String pais,
        java.math.BigDecimal valorFixo,
        java.math.BigDecimal valorHora,
        String razaoSocial,
        String cnpj,
        String cpf,
        String emailAuthsoft
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


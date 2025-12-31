package com.projeto.modelo.controller.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UsuarioResposeDTO(
    UUID id, 
    String email, 
    String nome,
    String cargo,
    String telefone,
    String status,
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
    java.math.BigDecimal valorHora
) {

}

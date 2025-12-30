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
    java.math.BigDecimal valorFixo,
    java.math.BigDecimal valorHora
) {

}

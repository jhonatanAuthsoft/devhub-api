package com.projeto.modelo.controller.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UsuarioResposeDTO(UUID id, String email, String permissao) {

}

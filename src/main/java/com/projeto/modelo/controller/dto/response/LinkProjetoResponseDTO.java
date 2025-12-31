package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.enums.ClassificacaoLink;
import com.projeto.modelo.model.enums.TipoAmbiente;
import java.util.UUID;

public record LinkProjetoResponseDTO(
    UUID id,
    String url,
    String descricao,
    TipoAmbiente tipoAmbiente,
    ClassificacaoLink classificacao,
    String observacao
) {}

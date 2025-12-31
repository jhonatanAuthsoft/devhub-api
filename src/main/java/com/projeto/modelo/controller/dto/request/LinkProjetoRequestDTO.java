package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.model.enums.ClassificacaoLink;
import com.projeto.modelo.model.enums.TipoAmbiente;

public record LinkProjetoRequestDTO(
    String url,
    String descricao,
    TipoAmbiente tipoAmbiente,
    ClassificacaoLink classificacao,
    String observacao
) {}

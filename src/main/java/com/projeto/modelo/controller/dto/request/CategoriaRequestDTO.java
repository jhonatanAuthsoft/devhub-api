package com.projeto.modelo.controller.dto.request;

import lombok.Data;
import java.util.UUID;
import com.projeto.modelo.model.enums.TipoCategoria;

@Data
public class CategoriaRequestDTO {
    private String nome;
    private UUID paiId;
    private Boolean ativo;
    private TipoCategoria tipo;
}

package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.entity.Categoria;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CategoriaResponseDTO {
    private UUID id;
    private String nome;
    private UUID paiId;
    private String paiNome;
    private Boolean preConfigurada;
    private Boolean ativo;
    private List<CategoriaResponseDTO> filhas;

    public static CategoriaResponseDTO fromEntity(Categoria entity) {
        return CategoriaResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .paiId(entity.getPai() != null ? entity.getPai().getId() : null)
                .paiNome(entity.getPai() != null ? entity.getPai().getNome() : null)
                .preConfigurada(entity.getPreConfigurada())
                .ativo(entity.getAtivo())
                .filhas((entity.getFilhas() != null && !entity.getFilhas().isEmpty()) ? 
                        entity.getFilhas().stream().map(CategoriaResponseDTO::fromEntitySemFilhas).collect(Collectors.toList()) : null)
                .build();
    }
    
    public static CategoriaResponseDTO fromEntitySemFilhas(Categoria entity) {
        return CategoriaResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .paiId(entity.getPai() != null ? entity.getPai().getId() : null)
                .paiNome(entity.getPai() != null ? entity.getPai().getNome() : null)
                .preConfigurada(entity.getPreConfigurada())
                .ativo(entity.getAtivo())
                .build();
    }
}

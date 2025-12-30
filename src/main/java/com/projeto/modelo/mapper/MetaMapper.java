package com.projeto.modelo.mapper;

import com.projeto.modelo.controller.dto.response.MetaResponseDTO;
import com.projeto.modelo.model.entity.Meta;
import org.springframework.stereotype.Component;

@Component
public class MetaMapper {

    public MetaResponseDTO toResponseDTO(Meta meta) {
        return MetaResponseDTO.builder()
                .id(meta.getId())
                .ano(meta.getAno())
                .categoria(meta.getCategoria() != null ? meta.getCategoria().name() : null)
                .tipoMeta(meta.getTipoMeta() != null ? meta.getTipoMeta().name() : null)
                .valorAnual(meta.getValorAnual())
                .janeiro(meta.getJaneiro())
                .fevereiro(meta.getFevereiro())
                .marco(meta.getMarco())
                .abril(meta.getAbril())
                .maio(meta.getMaio())
                .junho(meta.getJunho())
                .julho(meta.getJulho())
                .agosto(meta.getAgosto())
                .setembro(meta.getSetembro())
                .outubro(meta.getOutubro())
                .novembro(meta.getNovembro())
                .dezembro(meta.getDezembro())
                .dataCriacao(meta.getDataCriacao())
                .dataAtualizacao(meta.getDataAtualizacao())
                .build();
    }
}

package com.projeto.modelo.dto;

import com.projeto.modelo.model.entity.TicketComentario;
import com.projeto.modelo.model.enums.TipoAutor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TicketComentarioResponseDTO {
    private UUID id;
    private UUID ticketId;
    private UUID autorId;
    private TipoAutor autorTipo;
    private String nomeAutor;
    private String texto;
    private LocalDateTime criadoEm;

    public static TicketComentarioResponseDTO fromEntity(TicketComentario comentario) {
        return TicketComentarioResponseDTO.builder()
                .id(comentario.getId())
                .ticketId(comentario.getTicket().getId())
                .autorId(comentario.getAutorId())
                .autorTipo(comentario.getAutorTipo())
                // .nomeAutor will be set by the service using Pessoa/Usuario repository
                .texto(comentario.getTexto())
                .criadoEm(comentario.getCriadoEm())
                .build();
    }
}

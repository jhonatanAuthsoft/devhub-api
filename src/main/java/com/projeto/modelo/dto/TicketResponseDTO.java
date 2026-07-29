package com.projeto.modelo.dto;

import com.projeto.modelo.model.entity.Ticket;
import com.projeto.modelo.model.enums.PrioridadeTicket;
import com.projeto.modelo.model.enums.StatusTicket;
import com.projeto.modelo.model.enums.TipoAutor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TicketResponseDTO {
    private UUID id;
    private String titulo;
    private String descricao;
    private UUID projetoId;
    private String nomeProjeto;
    private UUID abertoPorId;
    private TipoAutor abertoPorTipo;
    private String nomeAbertura;
    private StatusTicket statusAtual;
    private PrioridadeTicket prioridade;
    private UUID responsavelAtualId;
    private String nomeResponsavelAtual;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public static TicketResponseDTO fromEntity(Ticket ticket) {
        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .titulo(ticket.getTitulo())
                .descricao(ticket.getDescricao())
                .projetoId(ticket.getProjeto().getId())
                .nomeProjeto(ticket.getProjeto().getTitulo())
                .abertoPorId(ticket.getAbertoPorId())
                .abertoPorTipo(ticket.getAbertoPorTipo())
                .statusAtual(ticket.getStatusAtual())
                .prioridade(ticket.getPrioridade())
                .responsavelAtualId(ticket.getResponsavelAtualId())
                .nomeResponsavelAtual(null)
                .criadoEm(ticket.getCriadoEm())
                .atualizadoEm(ticket.getAtualizadoEm())
                .build();
    }
}

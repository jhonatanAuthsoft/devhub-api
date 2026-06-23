package com.projeto.modelo.dto;

import com.projeto.modelo.model.entity.TicketHistoricoStatus;
import com.projeto.modelo.model.enums.StatusTicket;
import com.projeto.modelo.model.enums.TipoAutor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TicketHistoricoStatusResponseDTO {
    private UUID id;
    private UUID ticketId;
    private StatusTicket statusAnterior;
    private StatusTicket statusNovo;
    private UUID alteradoPorId;
    private TipoAutor alteradoPorTipo;
    private String nomeAlteradoPor;
    private LocalDateTime dataAlteracao;
    private String observacao;
    private UUID direcionadoParaId;
    private String nomeDirecionadoPara;

    public static TicketHistoricoStatusResponseDTO fromEntity(TicketHistoricoStatus historico) {
        return TicketHistoricoStatusResponseDTO.builder()
                .id(historico.getId())
                .ticketId(historico.getTicket().getId())
                .statusAnterior(historico.getStatusAnterior())
                .statusNovo(historico.getStatusNovo())
                .alteradoPorId(historico.getAlteradoPorId())
                .alteradoPorTipo(historico.getAlteradoPorTipo())
                // .nomeAlteradoPor will be set by the service using Pessoa/Usuario repository
                .dataAlteracao(historico.getDataAlteracao())
                .observacao(historico.getObservacao())
                .direcionadoParaId(historico.getDirecionadoPara() != null ? historico.getDirecionadoPara().getId() : null)
                .nomeDirecionadoPara(historico.getDirecionadoPara() != null ? historico.getDirecionadoPara().getNome() : null)
                .build();
    }
}

package com.projeto.modelo.controller;

import com.projeto.modelo.dto.AlterarStatusTicketRequestDTO;
import com.projeto.modelo.dto.CriarTicketRequestDTO;
import com.projeto.modelo.dto.TicketHistoricoStatusResponseDTO;
import com.projeto.modelo.dto.TicketResponseDTO;
import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Ticket;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.TipoAutor;
import com.projeto.modelo.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMIN','GESTOR','COLABORADOR','CLIENTE')")
public class TicketController {

    private final TicketService ticketService;

    private TipoAutor extrairTipoAutor(UserDetails userDetails) {
        if (userDetails instanceof Pessoa) return TipoAutor.CONTATO_CLIENTE;
        return TipoAutor.EQUIPE_TECNICA;
    }

    private UUID extrairAutorId(UserDetails userDetails) {
        if (userDetails instanceof Pessoa) return ((Pessoa) userDetails).getId();
        if (userDetails instanceof Usuario) return ((Usuario) userDetails).getId();
        throw new IllegalStateException("Tipo de usuário desconhecido na sessão.");
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> criarTicket(
            @RequestBody @Valid CriarTicketRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Ticket ticket = new Ticket();
        ticket.setTitulo(dto.getTitulo());
        ticket.setDescricao(dto.getDescricao());
        ticket.setPrioridade(dto.getPrioridade());
        
        Projeto projeto = new Projeto();
        projeto.setId(dto.getProjetoId());
        ticket.setProjeto(projeto);

        Ticket salvo = ticketService.criarTicket(ticket, extrairAutorId(userDetails), extrairTipoAutor(userDetails));
        TicketResponseDTO responseDto = TicketResponseDTO.fromEntity(salvo);
        responseDto.setNomeAbertura(ticketService.getNomeAutor(salvo.getAbertoPorId(), salvo.getAbertoPorTipo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> listarTickets(
            @RequestParam(required = false) UUID projetoId,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<Ticket> tickets = ticketService.listarTicketsVisiveis(
                extrairAutorId(userDetails),
                extrairTipoAutor(userDetails),
                projetoId
        );

        List<TicketResponseDTO> dtos = tickets.stream()
                .map(ticket -> {
                    TicketResponseDTO dto = TicketResponseDTO.fromEntity(ticket);
                    dto.setNomeAbertura(ticketService.getNomeAutor(ticket.getAbertoPorId(), ticket.getAbertoPorTipo()));
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> buscarTicket(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Ticket ticket = ticketService.buscarPorId(
                id,
                extrairAutorId(userDetails),
                extrairTipoAutor(userDetails)
        );

        TicketResponseDTO dto = TicketResponseDTO.fromEntity(ticket);
        dto.setNomeAbertura(ticketService.getNomeAutor(ticket.getAbertoPorId(), ticket.getAbertoPorTipo()));

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TicketResponseDTO> alterarStatus(
            @PathVariable UUID id,
            @RequestBody @Valid AlterarStatusTicketRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Ticket ticket = ticketService.alterarStatus(
                id,
                dto.getNovoStatus(),
                extrairAutorId(userDetails),
                extrairTipoAutor(userDetails),
                dto.getObservacao(),
                dto.getDirecionadoParaId()
        );

        TicketResponseDTO responseDto = TicketResponseDTO.fromEntity(ticket);
        responseDto.setNomeAbertura(ticketService.getNomeAutor(ticket.getAbertoPorId(), ticket.getAbertoPorTipo()));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<TicketHistoricoStatusResponseDTO>> listarHistorico(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<TicketHistoricoStatusResponseDTO> historico = ticketService.listarHistorico(
                        id,
                        extrairAutorId(userDetails),
                        extrairTipoAutor(userDetails)
                ).stream()
                .map(h -> {
                    TicketHistoricoStatusResponseDTO dto = TicketHistoricoStatusResponseDTO.fromEntity(h);
                    dto.setNomeAlteradoPor(ticketService.getNomeAutor(h.getAlteradoPorId(), h.getAlteradoPorTipo()));
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(historico);
    }
}

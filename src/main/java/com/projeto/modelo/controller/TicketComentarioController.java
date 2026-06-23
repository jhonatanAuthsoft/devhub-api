package com.projeto.modelo.controller;

import com.projeto.modelo.dto.TicketComentarioRequestDTO;
import com.projeto.modelo.dto.TicketComentarioResponseDTO;
import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.entity.TicketComentario;
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
public class TicketComentarioController {

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

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<TicketComentarioResponseDTO> adicionarComentario(
            @PathVariable UUID id,
            @RequestBody @Valid TicketComentarioRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        TicketComentario comentario = new TicketComentario();
        comentario.setTexto(dto.getTexto());

        TicketComentario salvo = ticketService.adicionarComentario(
                id,
                comentario,
                extrairAutorId(userDetails),
                extrairTipoAutor(userDetails)
        );

        TicketComentarioResponseDTO responseDTO = TicketComentarioResponseDTO.fromEntity(salvo);
        responseDTO.setNomeAutor(ticketService.getNomeAutor(salvo.getAutorId(), salvo.getAutorTipo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<TicketComentarioResponseDTO>> listarComentarios(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<TicketComentarioResponseDTO> comentarios = ticketService.listarComentarios(
                        id,
                        extrairAutorId(userDetails),
                        extrairTipoAutor(userDetails)
                ).stream()
                .map(c -> {
                    TicketComentarioResponseDTO dto = TicketComentarioResponseDTO.fromEntity(c);
                    dto.setNomeAutor(ticketService.getNomeAutor(c.getAutorId(), c.getAutorTipo()));
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(comentarios);
    }
}

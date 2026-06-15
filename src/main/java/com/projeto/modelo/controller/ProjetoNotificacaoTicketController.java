package com.projeto.modelo.controller;

import com.projeto.modelo.dto.ProjetoNotificacaoTicketDTO;
import com.projeto.modelo.model.entity.ProjetoNotificacaoTicket;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.repository.ProjetoNotificacaoTicketRepository;
import com.projeto.modelo.repository.ProjetoRepository;
import com.projeto.modelo.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/projetos/{projetoId}/notificacoes-ticket")
@PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
public class ProjetoNotificacaoTicketController {

    private final ProjetoNotificacaoTicketRepository repository;
    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProjetoNotificacaoTicketController(ProjetoNotificacaoTicketRepository repository,
                                              ProjetoRepository projetoRepository,
                                              UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.projetoRepository = projetoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProjetoNotificacaoTicketDTO>> listar(@PathVariable UUID projetoId) {
        List<ProjetoNotificacaoTicket> configs = repository.findByProjetoId(projetoId);
        List<ProjetoNotificacaoTicketDTO> dtos = configs.stream().map(c -> new ProjetoNotificacaoTicketDTO(
                c.getId(),
                c.getUsuario() != null ? c.getUsuario().getId() : null,
                c.getNotificarCriacao(),
                c.getNotificarAtualizacao()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Void> atualizar(@PathVariable UUID projetoId, @RequestBody List<ProjetoNotificacaoTicketDTO> dtos) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        repository.deleteByProjetoId(projetoId);

        if (dtos != null) {
            for (ProjetoNotificacaoTicketDTO dto : dtos) {
                Usuario usuario = null;
                if (dto.usuarioId() != null) {
                    usuario = usuarioRepository.findById(dto.usuarioId())
                            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + dto.usuarioId()));
                }
                ProjetoNotificacaoTicket config = new ProjetoNotificacaoTicket();
                config.setProjeto(projeto);
                config.setUsuario(usuario);
                config.setNotificarCriacao(dto.notificarCriacao());
                config.setNotificarAtualizacao(dto.notificarAtualizacao());
                repository.save(config);
            }
        }

        return ResponseEntity.ok().build();
    }
}

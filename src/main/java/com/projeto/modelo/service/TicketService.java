package com.projeto.modelo.service;

import com.projeto.modelo.configuracao.exeption.AcessoNaoAutorizadoException;
import com.projeto.modelo.configuracao.exeption.ProjetoNaoPermiteTicketException;
import com.projeto.modelo.configuracao.exeption.TransicaoNaoPermitidaException;
import com.projeto.modelo.model.entity.*;
import com.projeto.modelo.model.enums.*;
import com.projeto.modelo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProjetoRepository projetoRepository;
    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TicketHistoricoStatusRepository historicoRepository;

    @Transactional
    public Ticket criarTicket(Ticket ticketDados, UUID autorId, TipoAutor autorTipo) {
        Projeto projeto = projetoRepository.findById(ticketDados.getProjeto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado."));

        if (projeto.getStatus() != StatusProjeto.EM_ANDAMENTO && projeto.getStatus() != StatusProjeto.EM_GARANTIA) {
            throw new ProjetoNaoPermiteTicketException("O projeto deve estar em andamento ou garantia para abertura de tickets.");
        }

        if (autorTipo == TipoAutor.CONTATO_CLIENTE) {
            Pessoa contato = pessoaRepository.findById(autorId)
                    .orElseThrow(() -> new IllegalArgumentException("Contato não encontrado."));

            if (!Boolean.TRUE.equals(contato.getPodeAbrirTicket())) {
                throw new AcessoNaoAutorizadoException("Contato não possui permissão para abrir tickets.");
            }

            if (!projeto.getCliente().getId().equals(contato.getCliente().getId())) {
                throw new AcessoNaoAutorizadoException("O contato não está vinculado ao projeto informado.");
            }
        }

        ticketDados.setAbertoPorId(autorId);
        ticketDados.setAbertoPorTipo(autorTipo);
        ticketDados.setStatusAtual(StatusTicket.ABERTO);

        Ticket salvo = ticketRepository.save(ticketDados);

        TicketHistoricoStatus historico = TicketHistoricoStatus.builder()
                .ticket(salvo)
                .statusAnterior(null)
                .statusNovo(StatusTicket.ABERTO)
                .alteradoPorId(autorId)
                .alteradoPorTipo(autorTipo)
                .build();
        historicoRepository.save(historico);

        return salvo;
    }

    @Transactional
    public Ticket alterarStatus(UUID ticketId, StatusTicket novoStatus, UUID autorId, TipoAutor autorTipo, String observacao, UUID direcionadoParaId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado."));

        StatusTicket statusAnterior = ticket.getStatusAtual();

        if (autorTipo == TipoAutor.CONTATO_CLIENTE) {
            if (statusAnterior != StatusTicket.EM_TESTE_CLIENTE || novoStatus != StatusTicket.APROVADO_CLIENTE) {
                throw new TransicaoNaoPermitidaException("Contato do cliente só pode alterar o status de 'Em Teste Cliente' para 'Aprovado Cliente'.");
            }
        }

        Usuario direcionadoPara = null;
        if (novoStatus == StatusTicket.BLOQUEADO) {
            if (observacao == null || observacao.trim().isEmpty()) {
                throw new IllegalArgumentException("Observação é obrigatória ao bloquear um ticket.");
            }
            if (direcionadoParaId == null) {
                throw new IllegalArgumentException("Usuário de direcionamento é obrigatório ao bloquear um ticket.");
            }
            direcionadoPara = usuarioRepository.findById(direcionadoParaId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário de direcionamento não encontrado."));
            
            ticket.setResponsavelAtual(direcionadoPara);
        }

        ticket.setStatusAtual(novoStatus);
        ticket = ticketRepository.save(ticket);

        TicketHistoricoStatus historico = TicketHistoricoStatus.builder()
                .ticket(ticket)
                .statusAnterior(statusAnterior)
                .statusNovo(novoStatus)
                .alteradoPorId(autorId)
                .alteradoPorTipo(autorTipo)
                .observacao(observacao)
                .direcionadoPara(direcionadoPara)
                .build();
        historicoRepository.save(historico);

        return ticket;
    }

    public List<Ticket> listarTicketsVisiveis(UUID usuarioId, TipoAutor usuarioTipo, UUID projetoIdFiltro) {
        if (usuarioTipo == TipoAutor.EQUIPE_TECNICA) {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
            
            if (projetoIdFiltro != null) {
                if (usuario.getPermissao() != PermissaoStatus.ADMIN) {
                    List<Projeto> projetosColaborador = projetoRepository.findDistinctByEquipeColaboradorId(usuarioId);
                    boolean temAcesso = projetosColaborador.stream().anyMatch(p -> p.getId().equals(projetoIdFiltro));
                    if (!temAcesso) {
                        throw new AcessoNaoAutorizadoException("Usuário não está alocado neste projeto.");
                    }
                }
                return ticketRepository.findByProjetoId(projetoIdFiltro);
            } else {
                if (usuario.getPermissao() == PermissaoStatus.ADMIN) {
                    return ticketRepository.findAll();
                } else {
                    List<Projeto> projetos = projetoRepository.findDistinctByEquipeColaboradorId(usuarioId);
                    List<UUID> projetoIds = projetos.stream().map(Projeto::getId).collect(Collectors.toList());
                    if (projetoIds.isEmpty()) return List.of();
                    return ticketRepository.findByProjetoIdIn(projetoIds);
                }
            }
        } else {
            Pessoa contato = pessoaRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Contato não encontrado."));
            
            if (projetoIdFiltro != null) {
                Projeto projeto = projetoRepository.findById(projetoIdFiltro)
                        .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado."));
                if (!projeto.getCliente().getId().equals(contato.getCliente().getId())) {
                    throw new AcessoNaoAutorizadoException("O contato não está vinculado ao projeto informado.");
                }
                return ticketRepository.findByProjetoId(projetoIdFiltro);
            } else {
                return ticketRepository.findByProjetoClienteId(contato.getCliente().getId());
            }
        }
    }
}

package com.projeto.modelo.service;

import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.entity.ProjetoNotificacaoTicket;
import com.projeto.modelo.model.entity.Ticket;
import com.projeto.modelo.model.entity.TicketHistoricoStatus;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.StatusTicket;
import com.projeto.modelo.repository.EmailService;
import com.projeto.modelo.repository.PessoaRepository;
import com.projeto.modelo.repository.ProjetoNotificacaoTicketRepository;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.util.TemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
public class NotificacaoTicketService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoTicketService.class);

    private final EmailService emailService;
    private final ProjetoNotificacaoTicketRepository projetoNotificacaoTicketRepository;
    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${app.url.base:http://localhost:3000}")
    private String appUrlBase;

    private static final String TICKET_CRIADO_TEMPLATE = "templates/ticketCriado.html";
    private static final String TICKET_ATUALIZADO_TEMPLATE = "templates/ticketStatusAtualizado.html";
    private static final String TICKET_BLOQUEADO_TEMPLATE = "templates/ticketBloqueado.html";

    public NotificacaoTicketService(EmailService emailService,
                                    ProjetoNotificacaoTicketRepository projetoNotificacaoTicketRepository,
                                    PessoaRepository pessoaRepository,
                                    UsuarioRepository usuarioRepository) {
        this.emailService = emailService;
        this.projetoNotificacaoTicketRepository = projetoNotificacaoTicketRepository;
        this.pessoaRepository = pessoaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void notificarCriacaoTicket(Ticket ticket) {
        String linkTicket = appUrlBase + "/tickets/" + ticket.getId();
        String nomeProjeto = ticket.getProjeto().getTitulo();
        String tituloTicket = ticket.getTitulo();
        String nomeAbertura = "Desconhecido";
        
        if ("EQUIPE_TECNICA".equals(ticket.getAbertoPorTipo().name())) {
            nomeAbertura = usuarioRepository.findById(ticket.getAbertoPorId())
                .map(Usuario::getNome).orElse("Desconhecido");
        } else if ("CONTATO_CLIENTE".equals(ticket.getAbertoPorTipo().name())) {
            nomeAbertura = pessoaRepository.findById(ticket.getAbertoPorId())
                .map(Pessoa::getNome).orElse("Desconhecido");
        }

        try {
            String html = TemplateUtils.htmlToString(TICKET_CRIADO_TEMPLATE)
                    .replace("#tituloTicket#", tituloTicket)
                    .replace("#nomeProjeto#", nomeProjeto)
                    .replace("#nomeAbertura#", nomeAbertura)
                    .replace("#linkTicket#", linkTicket);

            String assunto = "DevHub - Novo Ticket Criado: " + tituloTicket;

            // (a) todos Usuario com notificar_criacao=true no projeto
            List<ProjetoNotificacaoTicket> configs = projetoNotificacaoTicketRepository.findByProjetoId(ticket.getProjeto().getId());
            for (ProjetoNotificacaoTicket config : configs) {
                if (Boolean.TRUE.equals(config.getNotificarCriacao()) && config.getUsuario() != null) {
                    emailService.enviarEmailHtml(config.getUsuario().getEmail(), html, assunto);
                }
            }

            // (b) todas Pessoa com pode_abrir_ticket=true vinculadas ao projeto
            List<Pessoa> pessoas = pessoaRepository.findByClienteIdAndPodeAbrirTicketTrueAndAtivoTrue(ticket.getProjeto().getCliente().getId());
            for (Pessoa pessoa : pessoas) {
                if (pessoa.getEmail() != null && !pessoa.getEmail().isEmpty()) {
                    emailService.enviarEmailHtml(pessoa.getEmail(), html, assunto);
                }
            }
        } catch (IOException e) {
            log.error("Erro ao processar template de notificação de criação de ticket", e);
        }
    }

    public void notificarMudancaStatusTicket(Ticket ticket, StatusTicket statusAnterior, StatusTicket statusNovo) {
        String linkTicket = appUrlBase + "/tickets/" + ticket.getId();
        String nomeProjeto = ticket.getProjeto().getTitulo();
        String tituloTicket = ticket.getTitulo();

        try {
            String html = TemplateUtils.htmlToString(TICKET_ATUALIZADO_TEMPLATE)
                    .replace("#tituloTicket#", tituloTicket)
                    .replace("#nomeProjeto#", nomeProjeto)
                    .replace("#statusAnterior#", statusAnterior != null ? statusAnterior.name() : "N/A")
                    .replace("#statusNovo#", statusNovo.name())
                    .replace("#linkTicket#", linkTicket);

            String assunto = "DevHub - Ticket Atualizado: " + tituloTicket;

            // Notificar usuarios com notificarAtualizacao=true
            List<ProjetoNotificacaoTicket> configs = projetoNotificacaoTicketRepository.findByProjetoId(ticket.getProjeto().getId());
            for (ProjetoNotificacaoTicket config : configs) {
                if (Boolean.TRUE.equals(config.getNotificarAtualizacao()) && config.getUsuario() != null) {
                    emailService.enviarEmailHtml(config.getUsuario().getEmail(), html, assunto);
                }
            }

            // Notificar Pessoas vinculadas ao projeto que podem abrir tickets
            List<Pessoa> pessoas = pessoaRepository.findByClienteIdAndPodeAbrirTicketTrueAndAtivoTrue(ticket.getProjeto().getCliente().getId());
            for (Pessoa pessoa : pessoas) {
                if (pessoa.getEmail() != null && !pessoa.getEmail().isEmpty()) {
                    emailService.enviarEmailHtml(pessoa.getEmail(), html, assunto);
                }
            }

            // Se statusNovo==BLOQUEADO, buscar o TicketHistoricoStatus mais recente com direcionado_para_id não nulo
            if (statusNovo == StatusTicket.BLOQUEADO && ticket.getHistoricoStatus() != null && !ticket.getHistoricoStatus().isEmpty()) {
                TicketHistoricoStatus historicoMaisRecente = ticket.getHistoricoStatus().stream()
                        .filter(h -> h.getDirecionadoPara() != null)
                        .max(Comparator.comparing(TicketHistoricoStatus::getDataAlteracao))
                        .orElse(null);

                if (historicoMaisRecente != null) {
                    String htmlBloqueado = TemplateUtils.htmlToString(TICKET_BLOQUEADO_TEMPLATE)
                            .replace("#tituloTicket#", tituloTicket)
                            .replace("#nomeProjeto#", nomeProjeto)
                            .replace("#motivoBloqueio#", historicoMaisRecente.getObservacao() != null ? historicoMaisRecente.getObservacao() : "Sem observação")
                            .replace("#nomeResponsavel#", historicoMaisRecente.getDirecionadoPara().getNome())
                            .replace("#linkTicket#", linkTicket);

                    emailService.enviarEmailHtml(historicoMaisRecente.getDirecionadoPara().getEmail(), htmlBloqueado, "DevHub - Atenção: Ticket Bloqueado");
                }
            }

        } catch (IOException e) {
            log.error("Erro ao processar template de notificação de atualização de ticket", e);
        }
    }
}

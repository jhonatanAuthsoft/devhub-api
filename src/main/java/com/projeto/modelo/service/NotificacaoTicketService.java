package com.projeto.modelo.service;

import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.entity.EquipeProjeto;
import com.projeto.modelo.model.entity.Ticket;
import com.projeto.modelo.model.entity.TicketHistoricoStatus;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.StatusTicket;
import com.projeto.modelo.repository.EmailService;
import com.projeto.modelo.repository.PessoaRepository;
import com.projeto.modelo.repository.EquipeProjetoRepository;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.util.TemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class NotificacaoTicketService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoTicketService.class);

    private final EmailService emailService;
    private final EquipeProjetoRepository equipeProjetoRepository;
    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${app.url.base:http://localhost:3000}")
    private String appUrlBase;

    private static final String TICKET_CRIADO_TEMPLATE = "templates/ticketCriado.html";
    private static final String TICKET_ATUALIZADO_TEMPLATE = "templates/ticketStatusAtualizado.html";
    private static final String TICKET_BLOQUEADO_TEMPLATE = "templates/ticketBloqueado.html";

    public NotificacaoTicketService(EmailService emailService,
                                    EquipeProjetoRepository equipeProjetoRepository,
                                    PessoaRepository pessoaRepository,
                                    UsuarioRepository usuarioRepository) {
        this.emailService = emailService;
        this.equipeProjetoRepository = equipeProjetoRepository;
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

            // (a) todos Usuario com notificarTicket=true no projeto
            List<EquipeProjeto> equipe = equipeProjetoRepository.findByProjetoId(ticket.getProjeto().getId());
            for (EquipeProjeto membro : equipe) {
                if (Boolean.TRUE.equals(membro.getNotificarTicket()) && membro.getColaborador() != null) {
                    emailService.enviarEmailHtml(membro.getColaborador().getEmail(), html, assunto);
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

            // Notificar usuarios com notificarTicket=true
            List<EquipeProjeto> equipe = equipeProjetoRepository.findByProjetoId(ticket.getProjeto().getId());
            for (EquipeProjeto membro : equipe) {
                if (Boolean.TRUE.equals(membro.getNotificarTicket()) && membro.getColaborador() != null) {
                    emailService.enviarEmailHtml(membro.getColaborador().getEmail(), html, assunto);
                }
            }

            // Notificar Pessoas vinculadas ao projeto que podem abrir tickets
            List<Pessoa> pessoas = pessoaRepository.findByClienteIdAndPodeAbrirTicketTrueAndAtivoTrue(ticket.getProjeto().getCliente().getId());
            for (Pessoa pessoa : pessoas) {
                if (pessoa.getEmail() != null && !pessoa.getEmail().isEmpty()) {
                    emailService.enviarEmailHtml(pessoa.getEmail(), html, assunto);
                }
            }

            // Se statusNovo==BLOQUEADO ou REPROVADO, buscar o TicketHistoricoStatus mais recente com direcionado_para_id não nulo
            if ((statusNovo == StatusTicket.BLOQUEADO || statusNovo == StatusTicket.REPROVADO) && ticket.getHistoricoStatus() != null && !ticket.getHistoricoStatus().isEmpty()) {
                TicketHistoricoStatus historicoMaisRecente = ticket.getHistoricoStatus().stream()
                        .filter(h -> h.getDirecionadoParaId() != null)
                        .max(Comparator.comparing(TicketHistoricoStatus::getDataAlteracao))
                        .orElse(null);

                if (historicoMaisRecente != null) {
                    UUID dirId = historicoMaisRecente.getDirecionadoParaId();
                    String nomeDir = usuarioRepository.findById(dirId).map(Usuario::getNome)
                            .orElseGet(() -> pessoaRepository.findById(dirId).map(Pessoa::getNome).orElse("Responsável"));
                    String emailDir = usuarioRepository.findById(dirId).map(Usuario::getEmail)
                            .orElseGet(() -> pessoaRepository.findById(dirId).map(Pessoa::getEmail).orElse(null));

                    if (emailDir != null && !emailDir.isEmpty()) {
                        String htmlBloqueado = TemplateUtils.htmlToString(TICKET_BLOQUEADO_TEMPLATE)
                                .replace("#tituloTicket#", tituloTicket)
                                .replace("#nomeProjeto#", nomeProjeto)
                                .replace("#motivoBloqueio#", historicoMaisRecente.getObservacao() != null ? historicoMaisRecente.getObservacao() : "Sem observação")
                                .replace("#nomeResponsavel#", nomeDir)
                                .replace("#linkTicket#", linkTicket);

                        String assuntoStatus = statusNovo == StatusTicket.REPROVADO ? "DevHub - Atenção: Ticket Reprovado" : "DevHub - Atenção: Ticket Bloqueado";
                        emailService.enviarEmailHtml(emailDir, htmlBloqueado, assuntoStatus);
                    }
                }
            }

        } catch (IOException e) {
            log.error("Erro ao processar template de notificação de atualização de ticket", e);
        }
    }

    public void notificarNovoComentarioTicket(Ticket ticket, com.projeto.modelo.model.entity.TicketComentario comentario) {
        // Implementação simplificada (mesma lógica de notificarAtualizacao)
        String assunto = "DevHub - Novo Comentário no Ticket: " + ticket.getTitulo();
        String html = "<html><body><h3>Novo comentário no ticket '" + ticket.getTitulo() + "'</h3><p>" + comentario.getTexto() + "</p></body></html>";
        
        List<EquipeProjeto> equipe = equipeProjetoRepository.findByProjetoId(ticket.getProjeto().getId());
        for (EquipeProjeto membro : equipe) {
            if (Boolean.TRUE.equals(membro.getNotificarTicket()) && membro.getColaborador() != null) {
                emailService.enviarEmailHtml(membro.getColaborador().getEmail(), html, assunto);
            }
        }
    }
}

package com.projeto.modelo.service;

import com.projeto.modelo.model.entity.*;
import com.projeto.modelo.model.enums.StatusTicket;
import com.projeto.modelo.model.enums.TipoAutor;
import com.projeto.modelo.repository.EmailService;
import com.projeto.modelo.repository.PessoaRepository;
import com.projeto.modelo.repository.ProjetoNotificacaoTicketRepository;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.util.TemplateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoTicketServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private ProjetoNotificacaoTicketRepository projetoNotificacaoTicketRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private NotificacaoTicketService notificacaoTicketService;

    private Ticket ticket;
    private Projeto projeto;
    private Cliente cliente;
    private Usuario usuarioApp;
    private Pessoa pessoaContato;
    private UUID projetoId = UUID.randomUUID();
    private UUID clienteId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificacaoTicketService, "appUrlBase", "http://localhost:3000");

        cliente = new Cliente();
        cliente.setId(clienteId);

        projeto = new Projeto();
        projeto.setId(projetoId);
        projeto.setTitulo("Projeto X");
        projeto.setCliente(cliente);

        usuarioApp = new Usuario();
        usuarioApp.setId(UUID.randomUUID());
        usuarioApp.setNome("Usuário Teste");
        usuarioApp.setEmail("usuarioteste@email.com");

        pessoaContato = new Pessoa();
        pessoaContato.setId(UUID.randomUUID());
        pessoaContato.setNome("Contato Teste");
        pessoaContato.setEmail("contatoteste@email.com");
        pessoaContato.setPodeAbrirTicket(true);

        ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setTitulo("Erro no sistema");
        ticket.setProjeto(projeto);
        ticket.setAbertoPorTipo(TipoAutor.EQUIPE_TECNICA);
        ticket.setAbertoPorId(usuarioApp.getId());
        ticket.setStatusAtual(StatusTicket.ABERTO);
    }

    @Test
    void notificarCriacaoTicket_DeveChamarEmailServiceParaUsuarioEPessoa() {
        // Arrange
        ProjetoNotificacaoTicket configUsuario = new ProjetoNotificacaoTicket();
        configUsuario.setUsuario(usuarioApp);
        configUsuario.setNotificarCriacao(true);

        when(projetoNotificacaoTicketRepository.findByProjetoId(projetoId))
                .thenReturn(Collections.singletonList(configUsuario));

        when(pessoaRepository.findByClienteIdAndPodeAbrirTicketTrueAndAtivoTrue(clienteId))
                .thenReturn(Collections.singletonList(pessoaContato));

        // Act
        notificacaoTicketService.notificarCriacaoTicket(ticket);

        // Assert
        verify(emailService, times(1)).enviarEmailHtml(eq(usuarioApp.getEmail()), anyString(), contains("Novo Ticket Criado"));
        verify(emailService, times(1)).enviarEmailHtml(eq(pessoaContato.getEmail()), anyString(), contains("Novo Ticket Criado"));
    }

    @Test
    void notificarMudancaStatusTicket_DeveChamarEmailServiceParaUsuarioEPessoa() {
        // Arrange
        ProjetoNotificacaoTicket configUsuario = new ProjetoNotificacaoTicket();
        configUsuario.setUsuario(usuarioApp);
        configUsuario.setNotificarAtualizacao(true);

        when(projetoNotificacaoTicketRepository.findByProjetoId(projetoId))
                .thenReturn(Collections.singletonList(configUsuario));

        when(pessoaRepository.findByClienteIdAndPodeAbrirTicketTrueAndAtivoTrue(clienteId))
                .thenReturn(Collections.singletonList(pessoaContato));

        // Act
        notificacaoTicketService.notificarMudancaStatusTicket(ticket, StatusTicket.ABERTO, StatusTicket.EM_ANDAMENTO);

        // Assert
        verify(emailService, times(1)).enviarEmailHtml(eq(usuarioApp.getEmail()), anyString(), contains("Ticket Atualizado"));
        verify(emailService, times(1)).enviarEmailHtml(eq(pessoaContato.getEmail()), anyString(), contains("Ticket Atualizado"));
    }

    @Test
    void notificarMudancaStatusTicket_DeveEnviarEmailParaDirecionadoSeStatusForBloqueado() {
        // Arrange
        ProjetoNotificacaoTicket configUsuario = new ProjetoNotificacaoTicket();
        configUsuario.setUsuario(usuarioApp);
        configUsuario.setNotificarAtualizacao(false); // não notificar no fluxo padrão

        when(projetoNotificacaoTicketRepository.findByProjetoId(projetoId))
                .thenReturn(Collections.singletonList(configUsuario));

        when(pessoaRepository.findByClienteIdAndPodeAbrirTicketTrueAndAtivoTrue(clienteId))
                .thenReturn(Collections.emptyList());

        Usuario usuarioDirecionado = new Usuario();
        usuarioDirecionado.setNome("Responsavel Bloqueio");
        usuarioDirecionado.setEmail("responsavel@email.com");

        TicketHistoricoStatus historico = TicketHistoricoStatus.builder()
                .ticket(ticket)
                .statusAnterior(StatusTicket.EM_ANDAMENTO)
                .statusNovo(StatusTicket.BLOQUEADO)
                .observacao("Falta documentação")
                .direcionadoPara(usuarioDirecionado)
                .build();
        
        ticket.setHistoricoStatus(Collections.singletonList(historico));

        // Act
        notificacaoTicketService.notificarMudancaStatusTicket(ticket, StatusTicket.EM_ANDAMENTO, StatusTicket.BLOQUEADO);

        // Assert
        // Nenhuma configUsuario/pessoa deve receber o email padrao pois estao com flags falsas/vazias
        verify(emailService, never()).enviarEmailHtml(eq(usuarioApp.getEmail()), anyString(), contains("Ticket Atualizado"));
        
        // Direcionado deve receber o email de bloqueio
        verify(emailService, times(1)).enviarEmailHtml(eq(usuarioDirecionado.getEmail()), anyString(), contains("Atenção: Ticket Bloqueado"));
    }

    @Test
    void notificarMudancaStatusTicket_DeveEnviarEmail_ParaPessoaComPodeAbrirTicket() {
        // Arrange
        when(projetoNotificacaoTicketRepository.findByProjetoId(projetoId))
                .thenReturn(Collections.emptyList());

        when(pessoaRepository.findByClienteIdAndPodeAbrirTicketTrueAndAtivoTrue(clienteId))
                .thenReturn(Collections.singletonList(pessoaContato));

        // Act
        notificacaoTicketService.notificarMudancaStatusTicket(ticket, StatusTicket.ABERTO, StatusTicket.EM_ANDAMENTO);

        // Assert
        verify(emailService, times(1)).enviarEmailHtml(eq(pessoaContato.getEmail()), anyString(), contains("Ticket Atualizado"));
    }

    @Test
    void notificarMudancaStatusTicket_QuandoBloqueado_DeveEnviarEmailAdicional_ParaDirecionadoPara_MesmoSemEstarNaConfiguracao() {
        // Arrange
        when(projetoNotificacaoTicketRepository.findByProjetoId(projetoId))
                .thenReturn(Collections.emptyList());

        when(pessoaRepository.findByClienteIdAndPodeAbrirTicketTrueAndAtivoTrue(clienteId))
                .thenReturn(Collections.emptyList());

        Usuario usuarioDirecionado = new Usuario();
        usuarioDirecionado.setNome("Responsavel Bloqueio");
        usuarioDirecionado.setEmail("responsavel@email.com");

        TicketHistoricoStatus historico = TicketHistoricoStatus.builder()
                .ticket(ticket)
                .statusAnterior(StatusTicket.EM_ANDAMENTO)
                .statusNovo(StatusTicket.BLOQUEADO)
                .observacao("Falta documentação")
                .direcionadoPara(usuarioDirecionado)
                .build();
        
        ticket.setHistoricoStatus(Collections.singletonList(historico));

        // Act
        notificacaoTicketService.notificarMudancaStatusTicket(ticket, StatusTicket.EM_ANDAMENTO, StatusTicket.BLOQUEADO);

        // Assert
        verify(emailService, times(1)).enviarEmailHtml(eq(usuarioDirecionado.getEmail()), anyString(), contains("Atenção: Ticket Bloqueado"));
    }
}

package com.projeto.modelo.service;

import com.projeto.modelo.configuracao.exeption.AcessoNaoAutorizadoException;
import com.projeto.modelo.configuracao.exeption.ProjetoNaoPermiteTicketException;
import com.projeto.modelo.configuracao.exeption.TransicaoNaoPermitidaException;
import com.projeto.modelo.model.entity.*;
import com.projeto.modelo.model.enums.*;
import com.projeto.modelo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ProjetoRepository projetoRepository;
    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TicketHistoricoStatusRepository historicoRepository;

    @InjectMocks
    private TicketService ticketService;

    private Projeto projeto;
    private Cliente cliente;
    private Pessoa pessoa;
    private Usuario tecnico;
    private Ticket ticket;
    private UUID projetoId;
    private UUID pessoaId;
    private UUID tecnicoId;
    private UUID clienteId;
    private UUID ticketId;

    @BeforeEach
    void setUp() {
        projetoId = UUID.randomUUID();
        pessoaId = UUID.randomUUID();
        tecnicoId = UUID.randomUUID();
        clienteId = UUID.randomUUID();
        ticketId = UUID.randomUUID();

        cliente = new Cliente();
        cliente.setId(clienteId);

        projeto = new Projeto();
        projeto.setId(projetoId);
        projeto.setCliente(cliente);
        projeto.setStatus(StatusProjeto.EM_ANDAMENTO);

        pessoa = new Pessoa();
        pessoa.setId(pessoaId);
        pessoa.setCliente(cliente);
        pessoa.setPodeAbrirTicket(true);

        tecnico = new Usuario();
        tecnico.setId(tecnicoId);

        ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setProjeto(projeto);
        ticket.setStatusAtual(StatusTicket.ABERTO);
    }

    @Test
    void criarTicket_DeveLancarExcecao_SeProjetoNaoEmAndamentoOuGarantia() {
        projeto.setStatus(StatusProjeto.PRE_VENDA);
        when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

        assertThrows(ProjetoNaoPermiteTicketException.class, () -> {
            ticketService.criarTicket(ticket, pessoaId, TipoAutor.CONTATO_CLIENTE);
        });

        assertThrows(ProjetoNaoPermiteTicketException.class, () -> {
            ticketService.criarTicket(ticket, tecnicoId, TipoAutor.EQUIPE_TECNICA);
        });
    }

    @Test
    void criarTicket_DeveLancarExcecao_SeContatoNaoPodeAbrirTicket() {
        pessoa.setPodeAbrirTicket(false);
        when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoa));

        assertThrows(AcessoNaoAutorizadoException.class, () -> {
            ticketService.criarTicket(ticket, pessoaId, TipoAutor.CONTATO_CLIENTE);
        });
    }

    @Test
    void criarTicket_DeveLancarExcecao_SeContatoNaoVinculadoAoProjeto() {
        Cliente outroCliente = new Cliente();
        outroCliente.setId(UUID.randomUUID());
        pessoa.setCliente(outroCliente);

        when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoa));

        assertThrows(AcessoNaoAutorizadoException.class, () -> {
            ticketService.criarTicket(ticket, pessoaId, TipoAutor.CONTATO_CLIENTE);
        });
    }

    @Test
    void alterarStatus_TransicaoValida_ContatoCliente_DevePermitir() {
        ticket.setStatusAtual(StatusTicket.EM_TESTE_CLIENTE);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket atualizado = ticketService.alterarStatus(ticketId, StatusTicket.APROVADO_CLIENTE, pessoaId, TipoAutor.CONTATO_CLIENTE, null, null);

        assertEquals(StatusTicket.APROVADO_CLIENTE, atualizado.getStatusAtual());
        verify(historicoRepository, times(1)).save(any(TicketHistoricoStatus.class));
    }

    @Test
    void alterarStatus_TransicaoInvalida_ContatoCliente_DeveLancarExcecao() {
        ticket.setStatusAtual(StatusTicket.ABERTO);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        assertThrows(TransicaoNaoPermitidaException.class, () -> {
            ticketService.alterarStatus(ticketId, StatusTicket.EM_ANDAMENTO, pessoaId, TipoAutor.CONTATO_CLIENTE, null, null);
        });
    }

    @Test
    void alterarStatus_ParaBloqueadoSemObservacao_DeveLancarExcecao() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.alterarStatus(ticketId, StatusTicket.BLOQUEADO, tecnicoId, TipoAutor.EQUIPE_TECNICA, "", tecnicoId);
        });
    }

    @Test
    void alterarStatus_ParaBloqueadoSemDirecionadoPara_DeveLancarExcecao() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.alterarStatus(ticketId, StatusTicket.BLOQUEADO, tecnicoId, TipoAutor.EQUIPE_TECNICA, "Motivo bloqueio", null);
        });
    }

    @Test
    void alterarStatus_ParaBloqueadoValido_DeveAtualizarResponsavel() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(usuarioRepository.findById(tecnicoId)).thenReturn(Optional.of(tecnico));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket atualizado = ticketService.alterarStatus(ticketId, StatusTicket.BLOQUEADO, tecnicoId, TipoAutor.EQUIPE_TECNICA, "Motivo bloqueio", tecnicoId);

        assertEquals(StatusTicket.BLOQUEADO, atualizado.getStatusAtual());
        assertEquals(tecnico, atualizado.getResponsavelAtual());
        verify(historicoRepository, times(1)).save(any(TicketHistoricoStatus.class));
    }

    @Test
    void listarTicketsVisiveis_ContatoCliente_DeveRetornarPorClienteId() {
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoa));
        when(ticketRepository.findByProjetoClienteId(clienteId)).thenReturn(List.of(ticket));

        List<Ticket> result = ticketService.listarTicketsVisiveis(pessoaId, TipoAutor.CONTATO_CLIENTE, null);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(ticketRepository, times(1)).findByProjetoClienteId(clienteId);
    }
}

package com.projeto.modelo.controller;

import com.projeto.modelo.configuracao.exeption.AcessoNaoAutorizadoException;
import com.projeto.modelo.dto.TicketAnexoDTO;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Ticket;
import com.projeto.modelo.model.entity.TicketAnexo;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.TipoAnexo;
import com.projeto.modelo.model.enums.TipoAutor;
import com.projeto.modelo.repository.TicketAnexoRepository;
import com.projeto.modelo.repository.TicketRepository;
import com.projeto.modelo.service.S3Service;
import com.projeto.modelo.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketAnexoControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private S3Service s3Service;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketAnexoRepository anexoRepository;

    @InjectMocks
    private TicketAnexoController controller;

    private UUID ticketId;
    private Usuario usuario;
    private Ticket ticket;
    private Projeto projeto;

    @BeforeEach
    void setUp() {
        ticketId = UUID.randomUUID();
        usuario = new Usuario();
        usuario.setId(UUID.randomUUID());

        projeto = new Projeto();
        projeto.setId(UUID.randomUUID());

        ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setProjeto(projeto);
    }

    @Test
    void uploadAnexo_AcimaDe30MB_DeveRetornar400SemChamarS3() {
        byte[] content = new byte[31 * 1024 * 1024]; // 31MB
        MockMultipartFile file = new MockMultipartFile("arquivo", "teste.png", "image/png", content);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketService.listarTicketsVisiveis(usuario.getId(), TipoAutor.EQUIPE_TECNICA, projeto.getId()))
                .thenReturn(Collections.singletonList(ticket));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            controller.uploadAnexo(ticketId, file, TipoAnexo.FOTO, usuario)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(s3Service, never()).uploadArquivo(any(), any());
    }

    @Test
    void uploadAnexo_MimeTypeInvalidoParaFoto_DeveRetornar400() {
        MockMultipartFile file = new MockMultipartFile("arquivo", "teste.txt", "text/plain", "texto".getBytes());

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketService.listarTicketsVisiveis(usuario.getId(), TipoAutor.EQUIPE_TECNICA, projeto.getId()))
                .thenReturn(Collections.singletonList(ticket));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            controller.uploadAnexo(ticketId, file, TipoAnexo.FOTO, usuario)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(s3Service, never()).uploadArquivo(any(), any());
    }

    @Test
    void uploadAnexo_UsuarioSemAcesso_DeveRetornar403() {
        MockMultipartFile file = new MockMultipartFile("arquivo", "teste.png", "image/png", "img".getBytes());

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketService.listarTicketsVisiveis(usuario.getId(), TipoAutor.EQUIPE_TECNICA, projeto.getId()))
                .thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            controller.uploadAnexo(ticketId, file, TipoAnexo.FOTO, usuario)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verify(s3Service, never()).uploadArquivo(any(), any());
    }

    @Test
    void uploadAnexo_Valido_DeveChamarS3EPersistir() {
        MockMultipartFile file = new MockMultipartFile("arquivo", "teste.png", "image/png", "img".getBytes());

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketService.listarTicketsVisiveis(usuario.getId(), TipoAutor.EQUIPE_TECNICA, projeto.getId()))
                .thenReturn(Collections.singletonList(ticket));
        when(s3Service.uploadArquivo(eq(file), anyString())).thenReturn("http://s3.url/teste.png");

        TicketAnexo anexoSalvo = new TicketAnexo();
        anexoSalvo.setId(UUID.randomUUID());
        anexoSalvo.setUrlArquivo("http://s3.url/teste.png");
        anexoSalvo.setEnviadoPorTipo(TipoAutor.EQUIPE_TECNICA);
        when(anexoRepository.save(any())).thenReturn(anexoSalvo);

        ResponseEntity<TicketAnexoDTO> response = controller.uploadAnexo(ticketId, file, TipoAnexo.FOTO, usuario);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("http://s3.url/teste.png", response.getBody().getUrlArquivo());

        verify(s3Service, times(1)).uploadArquivo(eq(file), anyString());
        verify(anexoRepository, times(1)).save(any());
    }
}

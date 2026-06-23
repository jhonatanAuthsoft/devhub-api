package com.projeto.modelo.controller;

import com.projeto.modelo.dto.TicketAnexoDTO;
import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.entity.Ticket;
import com.projeto.modelo.model.entity.TicketAnexo;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.TipoAnexo;
import com.projeto.modelo.model.enums.TipoAutor;
import com.projeto.modelo.repository.TicketAnexoRepository;
import com.projeto.modelo.repository.TicketRepository;
import com.projeto.modelo.service.S3Service;
import com.projeto.modelo.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMIN','GESTOR','COLABORADOR','CLIENTE')")
public class TicketAnexoController {

    private final TicketService ticketService;
    private final S3Service s3Service;
    private final TicketRepository ticketRepository;
    private final TicketAnexoRepository anexoRepository;

    private static final long MAX_FILE_SIZE = 30 * 1024 * 1024; // 30MB

    private TipoAutor extrairTipoAutor(UserDetails userDetails) {
        if (userDetails instanceof Pessoa) return TipoAutor.CONTATO_CLIENTE;
        return TipoAutor.EQUIPE_TECNICA;
    }

    private UUID extrairAutorId(UserDetails userDetails) {
        if (userDetails instanceof Pessoa) return ((Pessoa) userDetails).getId();
        if (userDetails instanceof Usuario) return ((Usuario) userDetails).getId();
        throw new IllegalStateException("Tipo de usuário desconhecido na sessão.");
    }

    @PostMapping("/{ticketId}/anexos")
    public ResponseEntity<TicketAnexoDTO> uploadAnexo(
            @PathVariable UUID ticketId,
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("tipo") TipoAnexo tipo,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Validar acesso ao ticket
        ticketService.buscarPorId(ticketId, extrairAutorId(userDetails), extrairTipoAutor(userDetails));

        if (arquivo.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tamanho do arquivo não pode exceder 30MB.");
        }

        String mimeType = arquivo.getContentType();
        if (tipo == TipoAnexo.FOTO) {
            if (mimeType == null || (!mimeType.equals("image/jpeg") && !mimeType.equals("image/png") && !mimeType.equals("image/webp"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo MIME inválido para FOTO. Aceitos: image/jpeg, image/png, image/webp");
            }
        } else if (tipo == TipoAnexo.VIDEO) {
            if (mimeType == null || (!mimeType.equals("video/mp4") && !mimeType.equals("video/quicktime") && !mimeType.equals("video/webm"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo MIME inválido para VIDEO. Aceitos: video/mp4, video/quicktime, video/webm");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de anexo inválido.");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));

        String extensao = "";
        if (arquivo.getOriginalFilename() != null && arquivo.getOriginalFilename().contains(".")) {
            extensao = arquivo.getOriginalFilename().substring(arquivo.getOriginalFilename().lastIndexOf("."));
        }

        String path = String.format("tickets/%s/%s/%s%s",
                ticket.getProjeto().getId(),
                ticketId,
                UUID.randomUUID(),
                extensao);

        String url = s3Service.uploadArquivo(arquivo, path);

        TicketAnexo anexo = TicketAnexo.builder()
                .ticket(ticket)
                .tipo(tipo)
                .urlArquivo(url)
                .nomeArquivo(arquivo.getOriginalFilename() != null ? arquivo.getOriginalFilename() : "arquivo_sem_nome")
                .tamanhoBytes(arquivo.getSize())
                .enviadoPorId(extrairAutorId(userDetails))
                .enviadoPorTipo(extrairTipoAutor(userDetails))
                .build();

        anexo = anexoRepository.save(anexo);

        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(anexo));
    }

    @GetMapping("/{ticketId}/anexos")
    public ResponseEntity<List<TicketAnexoDTO>> listarAnexos(
            @PathVariable UUID ticketId,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Validar acesso ao ticket
        ticketService.buscarPorId(ticketId, extrairAutorId(userDetails), extrairTipoAutor(userDetails));

        List<TicketAnexo> anexos = anexoRepository.findByTicketId(ticketId);
        List<TicketAnexoDTO> dtos = anexos.stream().map(this::toDTO).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    private TicketAnexoDTO toDTO(TicketAnexo anexo) {
        return TicketAnexoDTO.builder()
                .id(anexo.getId())
                .tipo(anexo.getTipo())
                .urlArquivo(anexo.getUrlArquivo())
                .nomeArquivo(anexo.getNomeArquivo())
                .tamanhoBytes(anexo.getTamanhoBytes())
                .dataEnvio(anexo.getDataEnvio())
                .enviadoPorId(anexo.getEnviadoPorId())
                .enviadoPorTipo(anexo.getEnviadoPorTipo().name())
                .build();
    }
}

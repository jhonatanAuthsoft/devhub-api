package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.response.NotificacaoResponseDTO;
import com.projeto.modelo.model.entity.Notificacao;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.repository.NotificacaoRepository;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacaoServiceImp implements NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void criarNotificacao(UUID usuarioId, String mensagem) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));

        Notificacao notificacao = Notificacao.builder()
                .usuario(usuario)
                .mensagem(mensagem)
                .lida(false)
                .build();

        notificacaoRepository.save(notificacao);
    }

    @Override
    public List<NotificacaoResponseDTO> listarNaoLidasPorUsuario(UUID usuarioId) {
        return notificacaoRepository.findByUsuarioIdAndLidaOrderByDataCriacaoDesc(usuarioId, false)
                .stream()
                .map(NotificacaoResponseDTO::converte)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificacaoResponseDTO> listarTodasPorUsuario(UUID usuarioId) {
        return notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId)
                .stream()
                .map(NotificacaoResponseDTO::converte)
                .collect(Collectors.toList());
    }

    @Override
    public void marcarComoLida(UUID id) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacao não encontrada"));
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }
}

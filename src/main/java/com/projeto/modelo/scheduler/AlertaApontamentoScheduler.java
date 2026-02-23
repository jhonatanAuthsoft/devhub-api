package com.projeto.modelo.scheduler;

import com.projeto.modelo.model.entity.EquipeProjeto;
import com.projeto.modelo.repository.EmailService;
import com.projeto.modelo.repository.EquipeProjetoRepository;
import com.projeto.modelo.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertaApontamentoScheduler {

    private final EquipeProjetoRepository equipeProjetoRepository;
    private final EmailService emailService;
    private final NotificacaoService notificacaoService;

    // Roda todos os dias as 20h00, horário de Brasília
    @Scheduled(cron = "0 0 20 * * *", zone = "America/Sao_Paulo")
    @Transactional
    public void verificarApontamentosPendentes() {
        log.info("[AlertaApontamentoScheduler] Iniciando verificação de horas pendentes...");

        // Definindo a data limite como 2 dias atrás
        LocalDate dataLimite = LocalDate.now().minusDays(2);

        List<EquipeProjeto> colaboradoresComPendencia = equipeProjetoRepository.findColaboradoresSemApontamentoRecente(dataLimite);

        if (colaboradoresComPendencia.isEmpty()) {
            log.info("[AlertaApontamentoScheduler] Nenhuma pendência encontrada.");
            return;
        }

        log.info("[AlertaApontamentoScheduler] Encontradas {} pendências de horas.", colaboradoresComPendencia.size());

        for (EquipeProjeto ep : colaboradoresComPendencia) {
            String nomeColaborador = ep.getColaborador().getNome();
            String emailColaborador = ep.getColaborador().getEmail();
            String nomeProjeto = ep.getProjeto().getTitulo();

            // 1. Enviar Email
            log.info("Enviando e-mail de aviso para: {}", emailColaborador);
            emailService.enviarAlertaHorasPendentes(emailColaborador, nomeColaborador, nomeProjeto);

            // 2. Salvar Notificação no Banco de Dados
            String mensagemNotificacao = String.format("Aviso: Houve uma ausência de lançamento de horas da sua parte no projeto '%s' nos últimos 2 dias.", nomeProjeto);
            notificacaoService.criarNotificacao(ep.getColaborador().getId(), mensagemNotificacao);
        }

        log.info("[AlertaApontamentoScheduler] Fim da verificação de horas pendentes.");
    }
}

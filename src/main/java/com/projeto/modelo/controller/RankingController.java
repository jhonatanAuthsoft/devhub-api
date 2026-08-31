package com.projeto.modelo.controller;

import com.projeto.modelo.dto.relatorio.RankingResponseDTO;
import com.projeto.modelo.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/ranking")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    /**
     * Dados consolidados do Dashboard de Ranking.
     * @param ano ano de referencia (default: ano corrente)
     * @param somenteRealizados quando true, considera apenas Receitas RECEBIDAS e Despesas PAGAS
     */
    @GetMapping("/dashboard")
    public ResponseEntity<RankingResponseDTO> getRankingDashboard(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false, defaultValue = "false") Boolean somenteRealizados) {

        int anoFinal = ano != null ? ano : LocalDate.now().getYear();
        return ResponseEntity.ok(rankingService.gerarRanking(anoFinal, somenteRealizados));
    }
}

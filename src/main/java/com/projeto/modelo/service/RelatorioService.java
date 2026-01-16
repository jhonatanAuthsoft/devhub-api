package com.projeto.modelo.service;

import com.projeto.modelo.dto.relatorio.RelatorioHorasDTO;
import com.projeto.modelo.dto.relatorio.RelatorioMensalDTO;
import com.projeto.modelo.dto.relatorio.RelatorioProfissionalDTO;
import com.projeto.modelo.model.entity.Apontamento;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.enums.TipoProjeto;
import com.projeto.modelo.repository.ApontamentoRepository;
import com.projeto.modelo.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ApontamentoRepository apontamentoRepository;

    public List<RelatorioHorasDTO> gerarRelatorioHoras() {
        // Filtrar apenas projetos SOB_MEDIDA e HORAS_AVULSA
        List<TipoProjeto> tiposPermitidos = Arrays.asList(TipoProjeto.SOB_MEDIDA, TipoProjeto.HORAS_AVULSA);
        List<Projeto> projetos = projetoRepository.findByTipoProjetoIn(tiposPermitidos);

        List<RelatorioHorasDTO> relatorio = new ArrayList<>();

        for (Projeto projeto : projetos) {
            List<Apontamento> apontamentos = apontamentoRepository.findByProjetoId(projeto.getId());

            // Agrupar apontamentos por Colaborador
            Map<String, List<Apontamento>> apontamentosPorColaborador = apontamentos.stream()
                    .collect(Collectors.groupingBy(a -> a.getColaborador().getNome()));

            List<RelatorioProfissionalDTO> profissionaisDTO = new ArrayList<>();

            for (Map.Entry<String, List<Apontamento>> entry : apontamentosPorColaborador.entrySet()) {
                String nomeProfissional = entry.getKey();
                List<Apontamento> apsColaborador = entry.getValue();

                // Calcular total de horas do profissional no projeto
                BigDecimal totalHorasProfissional = apsColaborador.stream()
                        .map(Apontamento::getHoras)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Agrupar por Mês (yyyy-MM)
                Map<String, BigDecimal> horasPorMes = apsColaborador.stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getDataApontamento().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                                Collectors.reducing(BigDecimal.ZERO, Apontamento::getHoras, BigDecimal::add)
                        ));

                List<RelatorioMensalDTO> mesesDTO = horasPorMes.entrySet().stream()
                        .map(mesEntry -> RelatorioMensalDTO.builder()
                                .mesAno(mesEntry.getKey())
                                .horas(mesEntry.getValue())
                                .build())
                        .sorted(Comparator.comparing(RelatorioMensalDTO::getMesAno))
                        .collect(Collectors.toList());

                profissionaisDTO.add(RelatorioProfissionalDTO.builder()
                        .nomeProfissional(nomeProfissional)
                        .totalHoras(totalHorasProfissional)
                        .meses(mesesDTO)
                        .build());
            }

            // Calcular totais do projeto
            BigDecimal horasGastasTotal = apontamentos.stream()
                    .map(Apontamento::getHoras)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal saldoHoras = null;
            if (projeto.getTipoProjeto() == TipoProjeto.HORAS_AVULSA && projeto.getHorasEstimadas() != null) {
                saldoHoras = projeto.getHorasEstimadas().subtract(horasGastasTotal);
            }

            relatorio.add(RelatorioHorasDTO.builder()
                    .projetoId(projeto.getId())
                    .tituloProjeto(projeto.getTitulo())
                    .tipoProjeto(projeto.getTipoProjeto().getDescricao())
                    .horasEstimadas(projeto.getHorasEstimadas())
                    .horasGastasTotal(horasGastasTotal)
                    .saldoHoras(saldoHoras)
                    .profissionais(profissionaisDTO)
                    .build());
        }

        return relatorio;
    }
}

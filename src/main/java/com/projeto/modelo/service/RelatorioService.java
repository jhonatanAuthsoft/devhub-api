package com.projeto.modelo.service;

import com.projeto.modelo.dto.relatorio.*;
import com.projeto.modelo.model.entity.Apontamento;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Usuario;
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

    public List<RelatorioItemDTO> gerarRelatorioAvancado(RelatorioFiltroDTO filtro) {
        List<Apontamento> apontamentos = apontamentoRepository.findPorFiltros(
                filtro.getProjetoId(),
                filtro.getColaboradorId(),
                filtro.getDataInicio(),
                filtro.getDataFim()
        );

        boolean porColaborador = "COLABORADOR".equalsIgnoreCase(filtro.getAgrupamento());
        List<RelatorioItemDTO> resultado = new ArrayList<>();

        if (porColaborador) {
            // Agrupar por Colaborador -> Projeto -> Detalhes
            Map<Usuario, List<Apontamento>> porColab = apontamentos.stream()
                    .collect(Collectors.groupingBy(Apontamento::getColaborador));

            for (Map.Entry<Usuario, List<Apontamento>> entry : porColab.entrySet()) {
                Usuario colab = entry.getKey();
                List<Apontamento> apsColab = entry.getValue();

                BigDecimal totalHorasColab = apsColab.stream().map(Apontamento::getHoras).reduce(BigDecimal.ZERO, BigDecimal::add);

                List<RelatorioSubItemDTO> projetosSub = new ArrayList<>();
                Map<Projeto, List<Apontamento>> porProjeto = apsColab.stream().collect(Collectors.groupingBy(Apontamento::getProjeto));

                for (Map.Entry<Projeto, List<Apontamento>> projEntry : porProjeto.entrySet()) {
                    Projeto proj = projEntry.getKey();
                    List<Apontamento> apsProj = projEntry.getValue();
                    
                    BigDecimal totalHorasProj = apsProj.stream().map(Apontamento::getHoras).reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    // Horas alocadas (Previstas na equipe)
                    BigDecimal horasPrevistas = apontamentoRepository.findHorasPrevistasByProjetoAndColaborador(proj.getId(), colab.getId());

                    List<RelatorioDetalheDTO> detalhes = apsProj.stream()
                            .sorted(Comparator.comparing(Apontamento::getDataApontamento).reversed())
                            .map(a -> new RelatorioDetalheDTO(a.getDataApontamento(), a.getHoras(), a.getDescricao()))
                            .collect(Collectors.toList());

                    projetosSub.add(RelatorioSubItemDTO.builder()
                            .titulo(proj.getTitulo())
                            .totalHoras(totalHorasProj)
                            .horasAlocadas(horasPrevistas)
                            .detalhes(detalhes)
                            .build());
                }

                resultado.add(RelatorioItemDTO.builder()
                        .id(colab.getId())
                        .titulo(colab.getNome())
                        .totalHoras(totalHorasColab)
                        .subItens(projetosSub)
                        .build());
            }
        } else {
            // Default: Agrupar por Projeto -> Colaborador -> Detalhes
            Map<Projeto, List<Apontamento>> porProjeto = apontamentos.stream()
                    .collect(Collectors.groupingBy(Apontamento::getProjeto));

            for (Map.Entry<Projeto, List<Apontamento>> entry : porProjeto.entrySet()) {
                Projeto proj = entry.getKey();
                List<Apontamento> apsProj = entry.getValue();

                BigDecimal totalHorasProj = apsProj.stream().map(Apontamento::getHoras).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal saldo = null;
                if (proj.getTipoProjeto() == TipoProjeto.HORAS_AVULSA && proj.getHorasEstimadas() != null) {
                    saldo = proj.getHorasEstimadas().subtract(totalHorasProj);
                }

                List<RelatorioSubItemDTO> colaboradoresSub = new ArrayList<>();
                Map<Usuario, List<Apontamento>> porColab = apsProj.stream().collect(Collectors.groupingBy(Apontamento::getColaborador));

                for (Map.Entry<Usuario, List<Apontamento>> colabEntry : porColab.entrySet()) {
                    Usuario colab = colabEntry.getKey();
                    List<Apontamento> apsColab = colabEntry.getValue();
                    
                    BigDecimal totalHorasColab = apsColab.stream().map(Apontamento::getHoras).reduce(BigDecimal.ZERO, BigDecimal::add);

                    List<RelatorioDetalheDTO> detalhes = apsColab.stream()
                            .sorted(Comparator.comparing(Apontamento::getDataApontamento).reversed())
                            .map(a -> new RelatorioDetalheDTO(a.getDataApontamento(), a.getHoras(), a.getDescricao()))
                            .collect(Collectors.toList());

                    colaboradoresSub.add(RelatorioSubItemDTO.builder()
                            .titulo(colab.getNome())
                            .totalHoras(totalHorasColab)
                            .detalhes(detalhes)
                            .build());
                }

                resultado.add(RelatorioItemDTO.builder()
                        .id(proj.getId())
                        .titulo(proj.getTitulo())
                        .tipo(proj.getTipoProjeto().getDescricao())
                        .horasEstimadas(proj.getHorasEstimadas())
                        .totalHoras(totalHorasProj)
                        .saldoHoras(saldo)
                        .subItens(colaboradoresSub)
                        .build());
            }
        }
        
        // Ordenar alfabeticamente pelo título
        resultado.sort(Comparator.comparing(RelatorioItemDTO::getTitulo));
        return resultado;
    }
}

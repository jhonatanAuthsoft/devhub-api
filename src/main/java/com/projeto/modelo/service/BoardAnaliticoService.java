package com.projeto.modelo.service;

import com.projeto.modelo.dto.relatorio.BoardTipoServicoDTO;
import com.projeto.modelo.model.entity.Cliente;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Receita;
import com.projeto.modelo.model.enums.StatusReceita;
import com.projeto.modelo.model.enums.TipoProjeto;
import com.projeto.modelo.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardAnaliticoService {

    private final ReceitaRepository receitaRepository;

    private static final String[] NOMES_MESES = {
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    public BoardTipoServicoDTO gerarBoardPorTipoServico(int ano, boolean somenteRecebidos) {
        LocalDate dataInicio = LocalDate.of(ano, 1, 1);
        LocalDate dataFim = LocalDate.of(ano, 12, 31);

        List<Receita> receitas = receitaRepository.findByDataVencimentoBetween(dataInicio, dataFim);

        // Filtrar estornadas e, se solicitado, manter apenas RECEBIDO
        List<Receita> receitasValidas = receitas.stream()
                .filter(r -> r.getStatus() != StatusReceita.ESTORNADO)
                .filter(r -> !somenteRecebidos || r.getStatus() == StatusReceita.RECEBIDO)
                .collect(Collectors.toList());

        // Estrutura de agrupamento: TipoKey -> (ClienteId -> array de 12 meses)
        Map<String, String> tipoKeyToNome = new LinkedHashMap<>();
        // Garantir ordem preferencial: ALOCACAO, SOB_MEDIDA, HORAS, ROYALTIES
        tipoKeyToNome.put("ALOCACAO", "Alocação");
        tipoKeyToNome.put("SOB_MEDIDA", "Sob Medida");
        tipoKeyToNome.put("HORAS", "Venda de Horas");
        tipoKeyToNome.put("ROYALTIES", "Royalties");

        // Map: TipoKey -> Map<ClienteId, LinhaAcumuladora>
        Map<String, Map<UUID, LinhaClienteAcumuladora>> matriz = new LinkedHashMap<>();

        BigDecimal faturamentoTotalAno = BigDecimal.ZERO;
        BigDecimal[] totaisMensaisAno = new BigDecimal[12];
        Arrays.fill(totaisMensaisAno, BigDecimal.ZERO);

        for (Receita receita : receitasValidas) {
            // Obter Cliente
            Cliente cliente = null;
            if (receita.getProjeto() != null && receita.getProjeto().getCliente() != null) {
                cliente = receita.getProjeto().getCliente();
            }
            UUID clienteId = cliente != null ? cliente.getId() : UUID.nameUUIDFromBytes("Outros".getBytes());
            String nomeCliente = cliente != null ? cliente.getNome() : "Outros Clientes";

            // Determinar Tipo de Serviço
            String tipoKey = resolverTipoKey(receita);
            String nomeExibicao = resolverNomeExibicao(tipoKey, receita);
            tipoKeyToNome.putIfAbsent(tipoKey, nomeExibicao);

            // Valor da receita
            BigDecimal valor = BigDecimal.ZERO;
            if (receita.getStatus() == StatusReceita.RECEBIDO && receita.getValorRecebido() != null) {
                valor = receita.getValorRecebido();
            } else if (receita.getValorPrevisto() != null) {
                valor = receita.getValorPrevisto();
            }

            // Mês (0 a 11)
            int mesIndex = receita.getDataVencimento().getMonthValue() - 1;
            if (mesIndex >= 0 && mesIndex < 12) {
                totaisMensaisAno[mesIndex] = totaisMensaisAno[mesIndex].add(valor);
            }
            faturamentoTotalAno = faturamentoTotalAno.add(valor);

            // Acumular na matriz
            matriz.computeIfAbsent(tipoKey, k -> new LinkedHashMap<>());
            LinhaClienteAcumuladora linha = matriz.get(tipoKey).computeIfAbsent(clienteId, k -> new LinhaClienteAcumuladora(clienteId, nomeCliente));
            if (mesIndex >= 0 && mesIndex < 12) {
                linha.valoresMensais[mesIndex] = linha.valoresMensais[mesIndex].add(valor);
            }
            linha.totalAno = linha.totalAno.add(valor);
        }

        // Construir Grupos no DTO
        List<BoardTipoServicoDTO.GrupoTipoServicoDTO> gruposDTO = new ArrayList<>();

        for (Map.Entry<String, String> entry : tipoKeyToNome.entrySet()) {
            String tKey = entry.getKey();
            String tNome = entry.getValue();

            Map<UUID, LinhaClienteAcumuladora> clientesDoTipo = matriz.get(tKey);
            if (clientesDoTipo == null || clientesDoTipo.isEmpty()) {
                continue; // Não exibe se zerado para manter limpo, ou inclui com 0 se preferir
            }

            BigDecimal totalValorTipo = BigDecimal.ZERO;
            BigDecimal[] mensaisTipo = new BigDecimal[12];
            Arrays.fill(mensaisTipo, BigDecimal.ZERO);
            List<BoardTipoServicoDTO.LinhaClienteBoardDTO> linhasClientesDTO = new ArrayList<>();

            for (LinhaClienteAcumuladora acum : clientesDoTipo.values()) {
                totalValorTipo = totalValorTipo.add(acum.totalAno);
                for (int m = 0; m < 12; m++) {
                    mensaisTipo[m] = mensaisTipo[m].add(acum.valoresMensais[m]);
                }

                BigDecimal pctShareCliente = calcularPorcentagem(acum.totalAno, faturamentoTotalAno);

                linhasClientesDTO.add(BoardTipoServicoDTO.LinhaClienteBoardDTO.builder()
                        .clienteId(acum.clienteId)
                        .nomeCliente(acum.nomeCliente)
                        .valoresMensais(Arrays.asList(acum.valoresMensais))
                        .totalClienteAno(acum.totalAno)
                        .percentualShare(pctShareCliente)
                        .build());
            }

            // Ordenar clientes por nome
            linhasClientesDTO.sort(Comparator.comparing(BoardTipoServicoDTO.LinhaClienteBoardDTO::getNomeCliente));

            BigDecimal pctTipo = calcularPorcentagem(totalValorTipo, faturamentoTotalAno);

            gruposDTO.add(BoardTipoServicoDTO.GrupoTipoServicoDTO.builder()
                    .tipoKey(tKey)
                    .nomeExibicao(tNome)
                    .totalValorTipo(totalValorTipo)
                    .percentualFaturamentoTipo(pctTipo)
                    .totaisMensaisTipo(Arrays.asList(mensaisTipo))
                    .clientes(linhasClientesDTO)
                    .build());
        }

        // Construir Totais Mensais no DTO
        List<BoardTipoServicoDTO.TotalMensalDTO> totaisMensaisDTO = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            BigDecimal valMes = totaisMensaisAno[i];
            BigDecimal pctMes = calcularPorcentagem(valMes, faturamentoTotalAno);
            totaisMensaisDTO.add(BoardTipoServicoDTO.TotalMensalDTO.builder()
                    .mes(i + 1)
                    .nomeMes(NOMES_MESES[i])
                    .valorTotal(valMes)
                    .percentualAno(pctMes)
                    .build());
        }

        return BoardTipoServicoDTO.builder()
                .ano(ano)
                .faturamentoTotalAno(faturamentoTotalAno)
                .totaisMensais(totaisMensaisDTO)
                .gruposTipo(gruposDTO)
                .build();
    }

    private String resolverTipoKey(Receita receita) {
        // 1. Verificar se a Categoria é Royalties
        if (receita.getCategoria() != null && receita.getCategoria().getNome() != null) {
            String catNome = receita.getCategoria().getNome().toUpperCase();
            if (catNome.contains("ROYALT") || catNome.contains("SOCIEDADE")) {
                return "ROYALTIES";
            }
        }

        // 2. Verificar Tipo de Projeto
        if (receita.getProjeto() != null && receita.getProjeto().getTipoProjeto() != null) {
            TipoProjeto tp = receita.getProjeto().getTipoProjeto();
            switch (tp) {
                case ALOCACAO:
                    return "ALOCACAO";
                case SOB_MEDIDA:
                case SOFTWARE_SOB_MEDIDA:
                    return "SOB_MEDIDA";
                case HORAS_AVULSA:
                case VENDA_HORA:
                    return "HORAS";
                default:
                    return tp.name();
            }
        }

        // 3. Fallback por Categoria
        if (receita.getCategoria() != null && receita.getCategoria().getNome() != null) {
            String catNome = receita.getCategoria().getNome().toUpperCase();
            if (catNome.contains("ALOCAÇ") || catNome.contains("ALOCAC")) return "ALOCACAO";
            if (catNome.contains("SOB MEDIDA")) return "SOB_MEDIDA";
            if (catNome.contains("HORA")) return "HORAS";
            return "CAT_" + receita.getCategoria().getId();
        }

        return "OUTROS";
    }

    private String resolverNomeExibicao(String tipoKey, Receita receita) {
        switch (tipoKey) {
            case "ALOCACAO": return "Alocação";
            case "SOB_MEDIDA": return "Sob Medida";
            case "HORAS": return "Venda de Horas";
            case "ROYALTIES": return "Royalties";
            case "OUTROS": return "Outros Serviços";
            default:
                if (receita.getProjeto() != null && receita.getProjeto().getTipoProjeto() != null) {
                    return receita.getProjeto().getTipoProjeto().getDescricao();
                }
                if (receita.getCategoria() != null) {
                    return receita.getCategoria().getNome();
                }
                return tipoKey;
        }
    }

    private BigDecimal calcularPorcentagem(BigDecimal parte, BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) == 0 || parte == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return parte.multiply(BigDecimal.valueOf(100))
                .divide(total, 2, RoundingMode.HALF_UP);
    }

    private static class LinhaClienteAcumuladora {
        UUID clienteId;
        String nomeCliente;
        BigDecimal[] valoresMensais = new BigDecimal[12];
        BigDecimal totalAno = BigDecimal.ZERO;

        LinhaClienteAcumuladora(UUID clienteId, String nomeCliente) {
            this.clienteId = clienteId;
            this.nomeCliente = nomeCliente;
            Arrays.fill(valoresMensais, BigDecimal.ZERO);
        }
    }
}

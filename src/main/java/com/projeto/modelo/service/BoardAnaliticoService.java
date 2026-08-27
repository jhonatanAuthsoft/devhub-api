import com.projeto.modelo.dto.relatorio.BoardCentroCustoDTO;
import com.projeto.modelo.dto.relatorio.BoardTipoServicoDTO;
import com.projeto.modelo.model.entity.Categoria;
import com.projeto.modelo.model.entity.Cliente;
import com.projeto.modelo.model.entity.Despesa;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Receita;
import com.projeto.modelo.model.enums.StatusDespesa;
import com.projeto.modelo.model.enums.StatusReceita;
import com.projeto.modelo.model.enums.TipoProjeto;
import com.projeto.modelo.repository.DespesaRepository;
import com.projeto.modelo.repository.ProjetoRepository;
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
    private final ProjetoRepository projetoRepository;
    private final DespesaRepository despesaRepository;

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
        // Garantir ordem preferencial: ALOCACAO, SOB_MEDIDA, HORAS, ROYALTIES, SERVIDOR, REPASSE_DE_DEMANDA
        tipoKeyToNome.put("ALOCACAO", "Alocação");
        tipoKeyToNome.put("SOB_MEDIDA", "Sob Medida");
        tipoKeyToNome.put("HORAS", "Venda de Horas");
        tipoKeyToNome.put("ROYALTIES", "Royalties");
        tipoKeyToNome.put("SERVIDOR", "Servidor");
        tipoKeyToNome.put("REPASSE_DE_DEMANDA", "Repasse de Demanda");

        // Map: TipoKey -> Map<ClienteId, LinhaAcumuladora>
        Map<String, Map<UUID, LinhaClienteAcumuladora>> matriz = new LinkedHashMap<>();

        BigDecimal faturamentoTotalAno = BigDecimal.ZERO;
        BigDecimal[] totaisMensaisAno = new BigDecimal[12];
        Arrays.fill(totaisMensaisAno, BigDecimal.ZERO);

        // Guardar conjunto de (projetoId, mesIndex) para os quais já existe Receita no banco
        Set<String> receitasExistentes = new HashSet<>();

        for (Receita receita : receitasValidas) {
            // Obter Cliente
            Cliente cliente = null;
            if (receita.getProjeto() != null && receita.getProjeto().getCliente() != null) {
                cliente = receita.getProjeto().getCliente();
            }
            UUID clienteId = cliente != null ? cliente.getId() : UUID.nameUUIDFromBytes("Outros".getBytes());
            String nomeCliente = cliente != null ? cliente.getNome() : "Outros Clientes";

            // Registrar se existe projeto vinculado
            if (receita.getProjeto() != null && receita.getProjeto().getId() != null) {
                int m = receita.getDataVencimento().getMonthValue() - 1;
                receitasExistentes.add(receita.getProjeto().getId() + "_" + m);
            }

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

        // Se não for somente recebidos, projetar mensalidades recorrentes para meses que ainda não têm parcelas no banco
        if (!somenteRecebidos) {
            List<Projeto> projetos = projetoRepository.findAll();
            for (Projeto proj : projetos) {
                if (proj.getValorContratoMensal() != null && proj.getValorContratoMensal().compareTo(BigDecimal.ZERO) > 0) {
                    if (proj.getCliente() == null) continue;

                    LocalDate inicio = proj.getDataInicio() != null ? proj.getDataInicio() : LocalDate.of(2020, 1, 1);
                    LocalDate fim = proj.getDataFimProjeto();

                    if (inicio.getYear() > ano) continue; // Projeto ainda não iniciou neste ano
                    if (fim != null && fim.getYear() < ano) continue; // Projeto já finalizou em ano anterior

                    int startMonth = (inicio.getYear() < ano) ? 0 : (inicio.getMonthValue() - 1);
                    int endMonth = (fim != null && fim.getYear() == ano) ? (fim.getMonthValue() - 1) : 11;

                    for (int m = startMonth; m <= endMonth; m++) {
                        String keyExistente = proj.getId() + "_" + m;
                        if (!receitasExistentes.contains(keyExistente)) {
                            BigDecimal valor = proj.getValorContratoMensal();
                            UUID clienteId = proj.getCliente().getId();
                            String nomeCliente = proj.getCliente().getNome();

                            String tipoKey = resolverTipoKeyDoProjeto(proj);
                            String nomeExibicao = resolverNomeExibicaoDoProjeto(tipoKey, proj);
                            tipoKeyToNome.putIfAbsent(tipoKey, nomeExibicao);

                            totaisMensaisAno[m] = totaisMensaisAno[m].add(valor);
                            faturamentoTotalAno = faturamentoTotalAno.add(valor);

                            matriz.computeIfAbsent(tipoKey, k -> new LinkedHashMap<>());
                            LinhaClienteAcumuladora linha = matriz.get(tipoKey).computeIfAbsent(clienteId, k -> new LinhaClienteAcumuladora(clienteId, nomeCliente));
                            linha.valoresMensais[m] = linha.valoresMensais[m].add(valor);
                            linha.totalAno = linha.totalAno.add(valor);
                        }
                    }
                }
            }
        }

        // Construir Grupos no DTO
        List<BoardTipoServicoDTO.GrupoTipoServicoDTO> gruposDTO = new ArrayList<>();

        for (Map.Entry<String, String> entry : tipoKeyToNome.entrySet()) {
            String tKey = entry.getKey();
            String tNome = entry.getValue();

            Map<UUID, LinhaClienteAcumuladora> clientesDoTipo = matriz.get(tKey);
            if (clientesDoTipo == null || clientesDoTipo.isEmpty()) {
                continue;
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

    public BoardCentroCustoDTO gerarBoardPorCentroCusto(int ano, boolean somentePagos) {
        LocalDate dataInicio = LocalDate.of(ano, 1, 1);
        LocalDate dataFim = LocalDate.of(ano, 12, 31);

        List<Despesa> despesas = despesaRepository.findByDataVencimentoBetween(dataInicio, dataFim);

        List<Despesa> despesasValidas = despesas.stream()
                .filter(d -> d.getStatus() != StatusDespesa.CANCELADA)
                .filter(d -> !somentePagos || d.getStatus() == StatusDespesa.PAGO)
                .collect(Collectors.toList());

        // Estruturas de agrupamento:
        // CentroCusto (Nome/Id) -> (CategoriaId/Nome -> valores por mês)
        Map<String, String> centroCustoNomes = new LinkedHashMap<>(); // Key -> NomeExibicao
        Map<String, Map<String, LinhaDespesaAcumuladora>> matriz = new LinkedHashMap<>();

        BigDecimal despesaTotalAno = BigDecimal.ZERO;
        BigDecimal[] totaisMensaisAno = new BigDecimal[12];
        Arrays.fill(totaisMensaisAno, BigDecimal.ZERO);

        for (Despesa despesa : despesasValidas) {
            Categoria cat = despesa.getCategoria();
            Categoria pai = (cat != null && cat.getPai() != null) ? cat.getPai() : cat;

            String centroCustoKey = pai != null ? pai.getId().toString() : "SEM_CENTRO";
            String centroCustoNome = pai != null ? pai.getNome() : "Outros / Sem Centro";
            centroCustoNomes.putIfAbsent(centroCustoKey, centroCustoNome);

            String catIdKey = cat != null ? cat.getId().toString() : "SEM_CAT";
            String catNome = cat != null ? cat.getNome() : "Geral";

            BigDecimal valor = BigDecimal.ZERO;
            if (despesa.getStatus() == StatusDespesa.PAGO && despesa.getValorPago() != null) {
                valor = despesa.getValorPago();
            } else if (despesa.getValorPrevisto() != null) {
                valor = despesa.getValorPrevisto();
            }

            int mesIndex = despesa.getDataVencimento().getMonthValue() - 1;
            if (mesIndex >= 0 && mesIndex < 12) {
                totaisMensaisAno[mesIndex] = totaisMensaisAno[mesIndex].add(valor);
            }
            despesaTotalAno = despesaTotalAno.add(valor);

            matriz.computeIfAbsent(centroCustoKey, k -> new LinkedHashMap<>());
            LinhaDespesaAcumuladora linha = matriz.get(centroCustoKey).computeIfAbsent(catIdKey, k -> new LinhaDespesaAcumuladora(cat != null ? cat.getId() : null, catNome));
            if (mesIndex >= 0 && mesIndex < 12) {
                linha.valoresMensais[mesIndex] = linha.valoresMensais[mesIndex].add(valor);
            }
            linha.totalAno = linha.totalAno.add(valor);
        }

        List<BoardCentroCustoDTO.GrupoCentroCustoDTO> gruposDTO = new ArrayList<>();

        for (Map.Entry<String, String> entry : centroCustoNomes.entrySet()) {
            String cKey = entry.getKey();
            String cNome = entry.getValue();

            Map<String, LinhaDespesaAcumuladora> subCats = matriz.get(cKey);
            if (subCats == null || subCats.isEmpty()) continue;

            BigDecimal totalValorCentro = BigDecimal.ZERO;
            BigDecimal[] mensaisCentro = new BigDecimal[12];
            Arrays.fill(mensaisCentro, BigDecimal.ZERO);
            List<BoardCentroCustoDTO.LinhaDespesaBoardDTO> despesasDTO = new ArrayList<>();

            for (LinhaDespesaAcumuladora acum : subCats.values()) {
                totalValorCentro = totalValorCentro.add(acum.totalAno);
                for (int m = 0; m < 12; m++) {
                    mensaisCentro[m] = mensaisCentro[m].add(acum.valoresMensais[m]);
                }

                BigDecimal pctShareSubCat = calcularPorcentagem(acum.totalAno, despesaTotalAno);

                despesasDTO.add(BoardCentroCustoDTO.LinhaDespesaBoardDTO.builder()
                        .categoriaId(acum.categoriaId)
                        .nomeCategoria(acum.nomeCategoria)
                        .valoresMensais(Arrays.asList(acum.valoresMensais))
                        .totalDespesaAno(acum.totalAno)
                        .percentualShare(pctShareSubCat)
                        .build());
            }

            despesasDTO.sort(Comparator.comparing(BoardCentroCustoDTO.LinhaDespesaBoardDTO::getNomeCategoria));
            BigDecimal pctCentro = calcularPorcentagem(totalValorCentro, despesaTotalAno);

            UUID parentUuid = null;
            try { parentUuid = UUID.fromString(cKey); } catch (Exception e) {}

            gruposDTO.add(BoardCentroCustoDTO.GrupoCentroCustoDTO.builder()
                    .categoriaPaiId(parentUuid)
                    .nomeCentroCusto(cNome)
                    .totalValorCentroCusto(totalValorCentro)
                    .percentualDespesaTipo(pctCentro)
                    .totaisMensaisCentroCusto(Arrays.asList(mensaisCentro))
                    .despesas(despesasDTO)
                    .build());
        }

        List<BoardCentroCustoDTO.TotalMensalDTO> totaisMensaisDTO = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            BigDecimal valMes = totaisMensaisAno[i];
            BigDecimal pctMes = calcularPorcentagem(valMes, despesaTotalAno);
            totaisMensaisDTO.add(BoardCentroCustoDTO.TotalMensalDTO.builder()
                    .mes(i + 1)
                    .nomeMes(NOMES_MESES[i])
                    .valorTotal(valMes)
                    .percentualAno(pctMes)
                    .build());
        }

        return BoardCentroCustoDTO.builder()
                .ano(ano)
                .despesaTotalAno(despesaTotalAno)
                .totaisMensais(totaisMensaisDTO)
                .gruposCentroCusto(gruposDTO)
                .build();
    }

    private static class LinhaDespesaAcumuladora {
        UUID categoriaId;
        String nomeCategoria;
        BigDecimal[] valoresMensais = new BigDecimal[12];
        BigDecimal totalAno = BigDecimal.ZERO;

        LinhaDespesaAcumuladora(UUID categoriaId, String nomeCategoria) {
            this.categoriaId = categoriaId;
            this.nomeCategoria = nomeCategoria;
            Arrays.fill(valoresMensais, BigDecimal.ZERO);
        }
    }

    private String resolverTipoKey(Receita receita) {
        if (receita.getCategoria() != null && receita.getCategoria().getNome() != null) {
            String catNome = receita.getCategoria().getNome().toUpperCase();
            if (catNome.contains("ROYALT") || catNome.contains("SOCIEDADE")) {
                return "ROYALTIES";
            }
            if (catNome.contains("SERVIDOR") || catNome.contains("SERVER") || catNome.contains("HOSTING")) {
                return "SERVIDOR";
            }
        }

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
                case SERVIDOR:
                    return "SERVIDOR";
                case REPASSE_DE_DEMANDA:
                    return "REPASSE_DE_DEMANDA";
                default:
                    return tp.name();
            }
        }

        if (receita.getCategoria() != null && receita.getCategoria().getNome() != null) {
            String catNome = receita.getCategoria().getNome().toUpperCase();
            if (catNome.contains("SERVIDOR") || catNome.contains("SERVER")) return "SERVIDOR";
            if (catNome.contains("ALOCAÇ") || catNome.contains("ALOCAC")) return "ALOCACAO";
            if (catNome.contains("SOB MEDIDA")) return "SOB_MEDIDA";
            if (catNome.contains("HORA")) return "HORAS";
            if (catNome.contains("REPASSE")) return "REPASSE_DE_DEMANDA";
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
            case "SERVIDOR": return "Servidor";
            case "REPASSE_DE_DEMANDA": return "Repasse de Demanda";
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

    private String resolverTipoKeyDoProjeto(Projeto proj) {
        if (proj.getTipoProjeto() != null) {
            TipoProjeto tp = proj.getTipoProjeto();
            switch (tp) {
                case ALOCACAO: return "ALOCACAO";
                case SOB_MEDIDA:
                case SOFTWARE_SOB_MEDIDA: return "SOB_MEDIDA";
                case HORAS_AVULSA:
                case VENDA_HORA: return "HORAS";
                case SERVIDOR: return "SERVIDOR";
                case REPASSE_DE_DEMANDA: return "REPASSE_DE_DEMANDA";
                default: return tp.name();
            }
        }
        return "OUTROS";
    }

    private String resolverNomeExibicaoDoProjeto(String tipoKey, Projeto proj) {
        switch (tipoKey) {
            case "ALOCACAO": return "Alocação";
            case "SOB_MEDIDA": return "Sob Medida";
            case "HORAS": return "Venda de Horas";
            case "ROYALTIES": return "Royalties";
            case "SERVIDOR": return "Servidor";
            case "REPASSE_DE_DEMANDA": return "Repasse de Demanda";
            case "OUTROS": return "Outros Serviços";
            default:
                if (proj.getTipoProjeto() != null) {
                    return proj.getTipoProjeto().getDescricao();
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

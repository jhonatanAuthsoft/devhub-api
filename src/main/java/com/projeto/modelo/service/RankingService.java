package com.projeto.modelo.service;

import com.projeto.modelo.dto.relatorio.RankingClienteDTO;
import com.projeto.modelo.dto.relatorio.RankingProjetoDTO;
import com.projeto.modelo.dto.relatorio.RankingResponseDTO;
import com.projeto.modelo.dto.relatorio.RankingTipoDTO;
import com.projeto.modelo.model.entity.Cliente;
import com.projeto.modelo.model.entity.Despesa;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Receita;
import com.projeto.modelo.model.enums.StatusDespesa;
import com.projeto.modelo.model.enums.StatusReceita;
import com.projeto.modelo.model.enums.TipoProjeto;
import com.projeto.modelo.repository.DespesaRepository;
import com.projeto.modelo.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

/**
 * Gera o Dashboard de Ranking: maiores PROJETOS e maiores CLIENTES por tipo de
 * serviço, ordenados por Receita e por Lucro.
 *
 * Fonte da verdade (mesma do BoardAnaliticoService, para os numeros baterem):
 *  - Receita do projeto = soma das Receitas do projeto (valorRecebido se RECEBIDO,
 *    senao valorPrevisto; ignora ESTORNADO).
 *  - Custo do projeto   = soma das Despesas do projeto (valorPago se PAGO, senao
 *    valorPrevisto; ignora CANCELADA).
 *  - Lucro = Receita - Custo. Margem = Lucro / Receita.
 */
@Service
@RequiredArgsConstructor
public class RankingService {

    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;

    public RankingResponseDTO gerarRanking(int ano, boolean somenteRealizados) {
        LocalDate dataInicio = LocalDate.of(ano, 1, 1);
        LocalDate dataFim = LocalDate.of(ano, 12, 31);

        // Acumulador por projeto (receita + custo)
        Map<UUID, ProjetoAcc> porProjeto = new LinkedHashMap<>();

        // ---- Receitas ----
        for (Receita receita : receitaRepository.findByDataVencimentoBetween(dataInicio, dataFim)) {
            if (receita.getStatus() == StatusReceita.ESTORNADO) continue;
            if (somenteRealizados && receita.getStatus() != StatusReceita.RECEBIDO) continue;
            Projeto proj = receita.getProjeto();
            if (proj == null) continue; // ranking e por projeto/cliente

            BigDecimal valor = BigDecimal.ZERO;
            if (receita.getStatus() == StatusReceita.RECEBIDO && receita.getValorRecebido() != null) {
                valor = receita.getValorRecebido();
            } else if (receita.getValorPrevisto() != null) {
                valor = receita.getValorPrevisto();
            }
            acc(porProjeto, proj).receita = acc(porProjeto, proj).receita.add(valor);
        }

        // ---- Despesas (custo) ----
        for (Despesa despesa : despesaRepository.findByDataVencimentoBetween(dataInicio, dataFim)) {
            if (despesa.getStatus() == StatusDespesa.CANCELADA) continue;
            if (somenteRealizados && despesa.getStatus() != StatusDespesa.PAGO) continue;
            Projeto proj = despesa.getProjeto();
            if (proj == null) continue;

            BigDecimal valor = BigDecimal.ZERO;
            if (despesa.getStatus() == StatusDespesa.PAGO && despesa.getValorPago() != null) {
                valor = despesa.getValorPago();
            } else if (despesa.getValorPrevisto() != null) {
                valor = despesa.getValorPrevisto();
            }
            acc(porProjeto, proj).custo = acc(porProjeto, proj).custo.add(valor);
        }

        // ---- Monta ranking por tipo ----
        Map<String, GrupoAcc> grupos = new LinkedHashMap<>();
        BigDecimal receitaTotal = BigDecimal.ZERO;
        BigDecimal lucroTotal = BigDecimal.ZERO;
        BigDecimal valorTotalGlobal = BigDecimal.ZERO;

        for (ProjetoAcc pa : porProjeto.values()) {
            BigDecimal receita = pa.receita;
            BigDecimal lucro = pa.receita.subtract(pa.custo);
            BigDecimal valorTotalProj = pa.projeto.getValorTotal() != null ? pa.projeto.getValorTotal() : BigDecimal.ZERO;
            receitaTotal = receitaTotal.add(receita);
            lucroTotal = lucroTotal.add(lucro);
            valorTotalGlobal = valorTotalGlobal.add(valorTotalProj);

            String tipoKey = resolverTipoKey(pa.projeto);
            String nomeTipo = resolverNomeExibicao(tipoKey, pa.projeto);
            GrupoAcc grupo = grupos.computeIfAbsent(tipoKey, k -> new GrupoAcc(k, nomeTipo));
            grupo.receita = grupo.receita.add(receita);
            grupo.lucro = grupo.lucro.add(lucro);
            grupo.valorTotal = grupo.valorTotal.add(valorTotalProj);

            Cliente cliente = pa.projeto.getCliente();
            UUID clienteId = cliente != null ? cliente.getId() : null;
            String nomeCliente = cliente != null ? cliente.getNome() : "Sem cliente";

            grupo.projetos.add(RankingProjetoDTO.builder()
                    .projetoId(pa.projeto.getId())
                    .nomeProjeto(pa.projeto.getTitulo())
                    .nomeCliente(nomeCliente)
                    .tipoKey(tipoKey)
                    .nomeTipo(nomeTipo)
                    .receita(receita)
                    .lucro(lucro)
                    .margem(margem(lucro, receita))
                    .valorTotal(valorTotalProj)
                    .build());

            // Acumula por cliente dentro do tipo
            ClienteAcc ca = grupo.clientes.computeIfAbsent(
                    clienteId != null ? clienteId.toString() : "SEM_CLIENTE",
                    k -> new ClienteAcc(clienteId, nomeCliente));
            ca.receita = ca.receita.add(receita);
            ca.lucro = ca.lucro.add(lucro);
            ca.valorTotal = ca.valorTotal.add(valorTotalProj);
            ca.qtdProjetos++;
        }

        // ---- Converte para DTO (ordenado por receita desc; o front reordena por metrica) ----
        List<RankingTipoDTO> gruposDTO = new ArrayList<>();
        for (GrupoAcc g : grupos.values()) {
            g.projetos.sort(Comparator.comparing(RankingProjetoDTO::getReceita).reversed());

            List<RankingClienteDTO> clientesDTO = new ArrayList<>();
            for (ClienteAcc ca : g.clientes.values()) {
                clientesDTO.add(RankingClienteDTO.builder()
                        .clienteId(ca.clienteId)
                        .nomeCliente(ca.nomeCliente)
                        .tipoKey(g.tipoKey)
                        .nomeTipo(g.nomeExibicao)
                        .receita(ca.receita)
                        .lucro(ca.lucro)
                        .margem(margem(ca.lucro, ca.receita))
                        .qtdProjetos(ca.qtdProjetos)
                        .valorTotal(ca.valorTotal)
                        .build());
            }
            clientesDTO.sort(Comparator.comparing(RankingClienteDTO::getReceita).reversed());

            gruposDTO.add(RankingTipoDTO.builder()
                    .tipoKey(g.tipoKey)
                    .nomeExibicao(g.nomeExibicao)
                    .receitaTipo(g.receita)
                    .lucroTipo(g.lucro)
                    .valorTotalTipo(g.valorTotal)
                    .projetos(g.projetos)
                    .clientes(clientesDTO)
                    .build());
        }
        // Grupos de maior receita primeiro
        gruposDTO.sort(Comparator.comparing(RankingTipoDTO::getReceitaTipo).reversed());

        return RankingResponseDTO.builder()
                .ano(ano)
                .receitaTotal(receitaTotal)
                .lucroTotal(lucroTotal)
                .margemMedia(margem(lucroTotal, receitaTotal))
                .valorTotalGlobal(valorTotalGlobal)
                .grupos(gruposDTO)
                .build();
    }

    // ---- Helpers ----

    private ProjetoAcc acc(Map<UUID, ProjetoAcc> map, Projeto proj) {
        return map.computeIfAbsent(proj.getId(), k -> new ProjetoAcc(proj));
    }

    private BigDecimal margem(BigDecimal lucro, BigDecimal receita) {
        if (receita == null || receita.compareTo(BigDecimal.ZERO) == 0 || lucro == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return lucro.multiply(BigDecimal.valueOf(100)).divide(receita, 2, RoundingMode.HALF_UP);
    }

    private String resolverTipoKey(Projeto proj) {
        if (proj.getTipoProjeto() == null) return "OUTROS";
        switch (proj.getTipoProjeto()) {
            case ALOCACAO: return "ALOCACAO";
            case SOB_MEDIDA:
            case SOFTWARE_SOB_MEDIDA: return "SOB_MEDIDA";
            case HORAS_AVULSA:
            case VENDA_HORA: return "HORAS";
            case SERVIDOR: return "SERVIDOR";
            case SUSTENTACAO: return "SUSTENTACAO";
            case REPASSE_DE_DEMANDA: return "REPASSE_DE_DEMANDA";
            default: return proj.getTipoProjeto().name();
        }
    }

    private String resolverNomeExibicao(String tipoKey, Projeto proj) {
        switch (tipoKey) {
            case "ALOCACAO": return "Alocação";
            case "SOB_MEDIDA": return "Sob Medida";
            case "HORAS": return "Venda de Horas";
            case "SERVIDOR": return "Servidor";
            case "SUSTENTACAO": return "Sustentação";
            case "REPASSE_DE_DEMANDA": return "Repasse de Demanda";
            case "OUTROS": return "Outros Serviços";
            default:
                return proj.getTipoProjeto() != null ? proj.getTipoProjeto().getDescricao() : tipoKey;
        }
    }

    // ---- Acumuladores internos ----

    private static class ProjetoAcc {
        final Projeto projeto;
        BigDecimal receita = BigDecimal.ZERO;
        BigDecimal custo = BigDecimal.ZERO;
        ProjetoAcc(Projeto projeto) { this.projeto = projeto; }
    }

    private static class GrupoAcc {
        final String tipoKey;
        final String nomeExibicao;
        BigDecimal receita = BigDecimal.ZERO;
        BigDecimal lucro = BigDecimal.ZERO;
        BigDecimal valorTotal = BigDecimal.ZERO;
        final List<RankingProjetoDTO> projetos = new ArrayList<>();
        final Map<String, ClienteAcc> clientes = new LinkedHashMap<>();
        GrupoAcc(String tipoKey, String nomeExibicao) { this.tipoKey = tipoKey; this.nomeExibicao = nomeExibicao; }
    }

    private static class ClienteAcc {
        final UUID clienteId;
        final String nomeCliente;
        BigDecimal receita = BigDecimal.ZERO;
        BigDecimal lucro = BigDecimal.ZERO;
        BigDecimal valorTotal = BigDecimal.ZERO;
        int qtdProjetos = 0;
        ClienteAcc(UUID clienteId, String nomeCliente) { this.clienteId = clienteId; this.nomeCliente = nomeCliente; }
    }
}

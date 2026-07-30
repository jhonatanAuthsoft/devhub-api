package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.request.EstornarReceitaDTO;
import com.projeto.modelo.controller.dto.request.ParcelaPersonalizadaDTO;
import com.projeto.modelo.controller.dto.request.ReceberReceitaDTO;
import com.projeto.modelo.controller.dto.request.ReceitaRequestDTO;
import com.projeto.modelo.controller.dto.response.ReceitaResponseDTO;
import com.projeto.modelo.model.entity.Categoria;
import com.projeto.modelo.model.entity.ContaBancaria;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Receita;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.AcaoAuditLog;
import com.projeto.modelo.model.enums.Periodicidade;
import com.projeto.modelo.model.enums.StatusReceita;
import com.projeto.modelo.model.enums.TipoRecorrencia;
import com.projeto.modelo.repository.CategoriaRepository;
import com.projeto.modelo.repository.ContaBancariaRepository;
import com.projeto.modelo.repository.ProjetoRepository;
import com.projeto.modelo.repository.ReceitaRepository;
import com.projeto.modelo.service.AuditLogService;
import com.projeto.modelo.service.ContaBancariaService;
import com.projeto.modelo.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceitaServiceImp implements ReceitaService {

    private final ReceitaRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final ProjetoRepository projetoRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final AuditLogService auditLogService;
    private final ContaBancariaService contaBancariaService;

    @Override
    @Transactional
    public List<ReceitaResponseDTO> criar(ReceitaRequestDTO dto, Usuario usuarioLogado) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
                
        Projeto projeto = null;
        if (dto.getProjetoId() != null) {
            projeto = projetoRepository.findById(dto.getProjetoId())
                    .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        }

        ContaBancaria contaBancaria = null;
        if (dto.getContaBancariaId() != null) {
            contaBancaria = contaBancariaRepository.findById(dto.getContaBancariaId())
                    .orElseThrow(() -> new RuntimeException("Conta Bancária não encontrada"));
        }

        List<Receita> geradas = new ArrayList<>();

        if (dto.getTipoRecorrencia() == TipoRecorrencia.UNICA) {
            Receita receita = Receita.builder()
                    .descricao(dto.getDescricao())
                    .valorPrevisto(dto.getValorPrevisto())
                    .dataVencimento(dto.getDataVencimento())
                    .categoria(categoria)
                    .projeto(projeto)
                    .conta(contaBancaria)
                    .status(StatusReceita.PENDENTE)
                    .tipoRecorrencia(TipoRecorrencia.UNICA)
                    .build();
            receita.setCriadoPor(usuarioLogado);
            geradas.add(repository.save(receita));
            
        } else if (dto.getTipoRecorrencia() == TipoRecorrencia.PARCELADA) {
            if ("PERSONALIZADO".equals(dto.getModoDistribuicao())) {
                BigDecimal soma = dto.getParcelasPersonalizadas().stream()
                        .map(ParcelaPersonalizadaDTO::getValor)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                if (soma.compareTo(dto.getValorTotal()) != 0) {
                    throw new RuntimeException("RN-04: Em parcelas personalizadas, a soma das parcelas (" + soma + ") deve ser igual ao valor total (" + dto.getValorTotal() + ").");
                }
                
                Receita pai = null;
                int current = 1;
                for (ParcelaPersonalizadaDTO parc : dto.getParcelasPersonalizadas()) {
                    Receita receita = Receita.builder()
                            .descricao(dto.getDescricao() + " (" + current + "/" + dto.getParcelasPersonalizadas().size() + ")")
                            .valorPrevisto(parc.getValor())
                            .dataVencimento(parc.getDataVencimento())
                            .categoria(categoria)
                            .projeto(projeto)
                            .conta(contaBancaria)
                            .status(StatusReceita.PENDENTE)
                            .tipoRecorrencia(TipoRecorrencia.PARCELADA)
                            .parcelaNumero(current)
                            .parcelaTotal(dto.getParcelasPersonalizadas().size())
                            .build();
                    receita.setCriadoPor(usuarioLogado);
                    
                    if (current == 1) {
                        pai = repository.save(receita);
                        receita.setRecorrenciaPai(pai); // self reference
                        pai = repository.save(receita);
                        geradas.add(pai);
                    } else {
                        receita.setRecorrenciaPai(pai);
                        geradas.add(repository.save(receita));
                    }
                    current++;
                }
            } else {
                // IGUALITÁRIO
                int max = dto.getQuantidadeParcelas();
                BigDecimal base = dto.getValorTotal().divide(BigDecimal.valueOf(max), 2, RoundingMode.DOWN);
                BigDecimal resto = dto.getValorTotal().subtract(base.multiply(BigDecimal.valueOf(max)));

                Receita pai = null;
                LocalDate vcto = dto.getDataVencimento();
                for (int i = 1; i <= max; i++) {
                    BigDecimal valor = (i == max) ? base.add(resto) : base;
                    Receita receita = Receita.builder()
                            .descricao(dto.getDescricao() + " (" + i + "/" + max + ")")
                            .valorPrevisto(valor)
                            .dataVencimento(vcto)
                            .categoria(categoria)
                            .projeto(projeto)
                            .conta(contaBancaria)
                            .status(StatusReceita.PENDENTE)
                            .tipoRecorrencia(TipoRecorrencia.PARCELADA)
                            .parcelaNumero(i)
                            .parcelaTotal(max)
                            .periodicidade(dto.getPeriodicidade()) // Opcional param para meses
                            .build();
                    receita.setCriadoPor(usuarioLogado);

                    if (i == 1) {
                        pai = repository.save(receita);
                        receita.setRecorrenciaPai(pai);
                        pai = repository.save(receita);
                        geradas.add(pai);
                    } else {
                        receita.setRecorrenciaPai(pai);
                        geradas.add(repository.save(receita));
                    }
                    vcto = proximoVencimento(vcto, dto.getPeriodicidade());
                }
            }
        } else if (dto.getTipoRecorrencia() == TipoRecorrencia.RECORRENTE) {
             // Gera por exemplo 12 lançamentos (1 ano visível)
             int limite = 12;
             Receita pai = null;
             LocalDate vcto = dto.getDataVencimento();
             for (int i = 1; i <= limite; i++) {
                 Receita receita = Receita.builder()
                         .descricao(dto.getDescricao())
                         .valorPrevisto(dto.getValorPrevisto())
                         .dataVencimento(vcto)
                         .categoria(categoria)
                         .projeto(projeto)
                         .conta(contaBancaria)
                         .status(StatusReceita.PENDENTE)
                         .tipoRecorrencia(TipoRecorrencia.RECORRENTE)
                         .periodicidade(dto.getPeriodicidade())
                         .build();
                 receita.setCriadoPor(usuarioLogado);

                 if (i == 1) {
                     pai = repository.save(receita);
                     receita.setRecorrenciaPai(pai);
                     pai = repository.save(receita);
                     geradas.add(pai);
                 } else {
                     receita.setRecorrenciaPai(pai);
                     geradas.add(repository.save(receita));
                 }
                 vcto = proximoVencimento(vcto, dto.getPeriodicidade());
             }
        }

        // Auditar criacao (só do pai em lotes para n poluir)
        auditLogService.registrarLog("Receita", geradas.get(0).getId(), AcaoAuditLog.CRIOU, usuarioLogado, null, "Receita cadastrada", null);

        return geradas.stream().map(ReceitaResponseDTO::fromEntity).collect(Collectors.toList());
    }

    private LocalDate proximoVencimento(LocalDate base, Periodicidade p) {
        if (p == null) return base.plusMonths(1); // default
        return switch (p) {
            case SEMANAL -> base.plusWeeks(1);
            case QUINZENAL -> base.plusDays(15);
            case MENSAL -> base.plusMonths(1);
            case BIMESTRAL -> base.plusMonths(2);
            case TRIMESTRAL -> base.plusMonths(3);
            case SEMESTRAL -> base.plusMonths(6);
            case ANUAL -> base.plusYears(1);
        };
    }

    @Override
    public ReceitaResponseDTO buscarPorId(UUID id) {
        return ReceitaResponseDTO.fromEntity(repository.findById(id).orElseThrow(() -> new RuntimeException("Receita não encontrada")));
    }

    @Override
    public List<ReceitaResponseDTO> listarTodos(LocalDate dataInicio, LocalDate dataFim, UUID categoriaId) {
        return repository.findAll().stream()
                .filter(r -> r.getExcluidoEm() == null)
                .filter(r -> {
                    boolean noPeriodo = (dataInicio == null || !r.getDataVencimento().isBefore(dataInicio)) &&
                                        (dataFim == null || !r.getDataVencimento().isAfter(dataFim));
                    return noPeriodo;
                })
                .filter(r -> categoriaId == null || (r.getCategoria() != null && r.getCategoria().getId().equals(categoriaId)))
                .map(ReceitaResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ReceitaResponseDTO> atualizar(UUID id, ReceitaRequestDTO dto, Usuario usuarioLogado) {
        Receita receita = repository.findById(id).orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        
        // RN-06: Ao editar receita de série, perguntar escopo: apenas esta / esta e as próximas / todas
        // Para simplificar a lógica agora, vamos alterar "apenas esta"
        
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        
        ContaBancaria contaBancaria = null;
        if (dto.getContaBancariaId() != null) {
            contaBancaria = contaBancariaRepository.findById(dto.getContaBancariaId())
                    .orElseThrow(() -> new RuntimeException("Conta Bancária não encontrada"));
        }
        
        String dadosAntigos = "Desc=" + receita.getDescricao() + ", Vlr=" + receita.getValorPrevisto();
        
        LocalDate dataAntiga = receita.getDataVencimento();
        LocalDate novaData = dto.getDataVencimento();
        
        receita.setDescricao(dto.getDescricao());
        receita.setValorPrevisto(dto.getValorPrevisto());
        receita.setDataVencimento(novaData);
        receita.setCategoria(categoria);
        receita.setConta(contaBancaria);
        receita.setAtualizadoPor(usuarioLogado);
        
        String dadosNovos = "Desc=" + dto.getDescricao() + ", Vlr=" + dto.getValorPrevisto();
        
        Receita receitaSalva = repository.save(receita);
        List<Receita> atualizadas = new ArrayList<>();
        atualizadas.add(receitaSalva);
        
        auditLogService.registrarLog("Receita", receitaSalva.getId(), AcaoAuditLog.EDITOU, usuarioLogado, dadosAntigos, dadosNovos, null);
        
        if ("ESTA_E_PROXIMAS".equals(dto.getEscopoEdicao()) && receitaSalva.getRecorrenciaPai() != null) {
            List<Receita> irmas = repository.findByRecorrenciaPaiId(receitaSalva.getRecorrenciaPai().getId());
            
            final UUID currentId = receitaSalva.getId();
            List<Receita> futuras = irmas.stream()
                .filter(r -> !r.getId().equals(currentId))
                .filter(r -> r.getExcluidoEm() == null)
                .filter(r -> !r.getDataVencimento().isBefore(dataAntiga))
                .sorted((r1, r2) -> r1.getDataVencimento().compareTo(r2.getDataVencimento()))
                .collect(Collectors.toList());
                
            LocalDate vctoBase = novaData;
            for (Receita futura : futuras) {
                vctoBase = proximoVencimento(vctoBase, futura.getPeriodicidade() != null ? futura.getPeriodicidade() : receita.getPeriodicidade());
                
                String dadosAntigosF = "Desc=" + futura.getDescricao() + ", Vlr=" + futura.getValorPrevisto();
                
                futura.setValorPrevisto(dto.getValorPrevisto());
                futura.setDataVencimento(vctoBase);
                futura.setCategoria(categoria);
                futura.setConta(contaBancaria);
                futura.setAtualizadoPor(usuarioLogado);
                
                String dadosNovosF = "Desc=" + futura.getDescricao() + ", Vlr=" + futura.getValorPrevisto();
                futura = repository.save(futura);
                atualizadas.add(futura);
                
                auditLogService.registrarLog("Receita", futura.getId(), AcaoAuditLog.EDITOU, usuarioLogado, dadosAntigosF, dadosNovosF, "Alteração em lote (ESTA_E_PROXIMAS)");
            }
        }
        
        return atualizadas.stream().map(ReceitaResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(UUID id, String escopoExclusao, Usuario usuarioLogado) {
        Receita receita = repository.findById(id).orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        
        // RN-01: Receita com status RECEBIDO não pode ser excluída, apenas estornada
        if (receita.getStatus() == StatusReceita.RECEBIDO) {
            throw new RuntimeException("RN-01: Receita com status RECEBIDO não pode ser excluída, apenas estornada.");
        }
        
        // RN-07: Ao excluir escopos
        // Na prática de soft delete
        receita.setExcluidoEm(LocalDate.now().atStartOfDay());
        receita.setAtualizadoPor(usuarioLogado);
        repository.save(receita);
        
        auditLogService.registrarLog("Receita", receita.getId(), AcaoAuditLog.EXCLUIU, usuarioLogado, null, "Receita excluída (Soft Delete)", null);
    }

    @Override
    @Transactional
    public ReceitaResponseDTO receber(UUID id, ReceberReceitaDTO dto, Usuario usuarioLogado) {
        Receita receita = repository.findById(id).orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        
        ContaBancaria conta = contaBancariaRepository.findById(dto.getContaBancariaId())
                .orElseThrow(() -> new RuntimeException("Conta Bancária não encontrada"));
                
        receita.setStatus(StatusReceita.RECEBIDO);
        receita.setConta(conta);
        receita.setValorRecebido(dto.getValorRecebido());
        receita.setDataRecebimento(dto.getDataRecebimento());
        receita.setAtualizadoPor(usuarioLogado);
        
        repository.save(receita);
        
        // RN-09: O saldo do caixa deve ser atualizado pelo valor_recebido
        contaBancariaService.atualizarSaldo(conta.getId(), dto.getValorRecebido());
        
        // RN-10: Gerar Log imutavel
        auditLogService.registrarLog("Receita", receita.getId(), AcaoAuditLog.RECEBEU, usuarioLogado, null, "Recebimento efetuado. Valor: " + dto.getValorRecebido() + " em " + conta.getNome(), null);
        
        return ReceitaResponseDTO.fromEntity(receita);
    }

    @Override
    @Transactional
    public List<ReceitaResponseDTO> estornar(UUID id, EstornarReceitaDTO dto, Usuario usuarioLogado) {
        Receita receita = repository.findById(id).orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        
        if (receita.getStatus() != StatusReceita.RECEBIDO) {
             throw new RuntimeException("Somente receitas pagas podem ser estornadas.");
        }
        
        ContaBancaria conta = receita.getConta();
        
        receita.setStatus(StatusReceita.ESTORNADO);
        receita.setAtualizadoPor(usuarioLogado);
        
        repository.save(receita);
        
        // RN-01 e RN-10: Estorno gera lançamento negativo
        contaBancariaService.atualizarSaldo(conta.getId(), receita.getValorRecebido().negate());
        
        auditLogService.registrarLog("Receita", receita.getId(), AcaoAuditLog.ESTORNOU, usuarioLogado, null, "Estorno: " + dto.getMotivo() + ". Valor abatido do caixa: " + receita.getValorRecebido(), null);
        
        return List.of(ReceitaResponseDTO.fromEntity(receita));
    }

    // RN-05: Job noturno para EM_ATRASO
    @Override
    @Scheduled(cron = "0 0 1 * * ?") // Roda todo dia a 1:00 AM
    @Transactional
    public void atualizarStatusAtraso() {
        LocalDate ontem = LocalDate.now().minusDays(1);
        List<Receita> pendentesAtrasadas = repository.findByStatusAndDataVencimentoBefore(StatusReceita.PENDENTE, LocalDate.now());
        
        for (Receita r : pendentesAtrasadas) {
            r.setStatus(StatusReceita.EM_ATRASO);
            repository.save(r);
            auditLogService.registrarLog("Receita", r.getId(), AcaoAuditLog.EDITOU, null, "Status: PENDENTE", "Status: EM_ATRASO (Job automático do sistema)", null);
        }
    }

    // Geração automática contínua para receitas recorrentes (mantém 24 meses sempre gerados no banco)
    @Scheduled(cron = "0 0 2 * * ?") // Roda todo dia às 2:00 AM
    @org.springframework.context.event.EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
    @Transactional
    public void processarRecorrenciasAutomaticas() {
        List<Receita> todasReceitas = repository.findAll();
        
        List<Receita> paisRecorrentes = todasReceitas.stream()
                .filter(r -> r.getExcluidoEm() == null)
                .filter(r -> r.getTipoRecorrencia() == TipoRecorrencia.RECORRENTE)
                .filter(r -> r.getRecorrenciaPai() == null || r.getRecorrenciaPai().getId().equals(r.getId()))
                .filter(r -> r.getProjeto() == null || r.getProjeto().getStatus() != com.projeto.modelo.model.enums.StatusProjeto.CANCELADO)
                .collect(Collectors.toList());

        LocalDate limiteHorizonte = LocalDate.now().plusMonths(24);

        for (Receita pai : paisRecorrentes) {
            List<Receita> familia = repository.findByRecorrenciaPaiId(pai.getId());
            if (familia == null || familia.isEmpty()) {
                familia = List.of(pai);
            }

            LocalDate maiorVencimento = familia.stream()
                    .map(Receita::getDataVencimento)
                    .max(LocalDate::compareTo)
                    .orElse(pai.getDataVencimento());

            LocalDate dataFimProjeto = pai.getProjeto() != null ? pai.getProjeto().getDataFimProjeto() : null;

            LocalDate proximoVcto = proximoVencimento(maiorVencimento, pai.getPeriodicidade());
            while (!proximoVcto.isAfter(limiteHorizonte)) {
                if (dataFimProjeto != null && proximoVcto.isAfter(dataFimProjeto)) {
                    break;
                }

                Receita novaReceita = Receita.builder()
                        .descricao(pai.getDescricao())
                        .valorPrevisto(pai.getValorPrevisto())
                        .dataVencimento(proximoVcto)
                        .categoria(pai.getCategoria())
                        .projeto(pai.getProjeto())
                        .conta(pai.getConta())
                        .status(StatusReceita.PENDENTE)
                        .tipoRecorrencia(TipoRecorrencia.RECORRENTE)
                        .periodicidade(pai.getPeriodicidade())
                        .recorrenciaPai(pai)
                        .build();
                novaReceita.setCriadoPor(pai.getCriadoPor());
                repository.save(novaReceita);

                proximoVcto = proximoVencimento(proximoVcto, pai.getPeriodicidade());
            }
        }
    }
}

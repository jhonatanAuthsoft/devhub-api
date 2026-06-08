package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.request.EstornarDespesaDTO;
import com.projeto.modelo.controller.dto.request.ParcelaPersonalizadaDTO;
import com.projeto.modelo.controller.dto.request.PagarDespesaDTO;
import com.projeto.modelo.controller.dto.request.DespesaRequestDTO;
import com.projeto.modelo.controller.dto.response.DespesaResponseDTO;
import com.projeto.modelo.model.entity.Categoria;
import com.projeto.modelo.model.entity.ContaBancaria;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Despesa;
import com.projeto.modelo.model.entity.CartaoCredito;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.AcaoAuditLog;
import com.projeto.modelo.model.enums.Periodicidade;
import com.projeto.modelo.model.enums.StatusDespesa;
import com.projeto.modelo.model.enums.TipoRecorrencia;
import com.projeto.modelo.repository.CategoriaRepository;
import com.projeto.modelo.repository.ContaBancariaRepository;
import com.projeto.modelo.repository.ProjetoRepository;
import com.projeto.modelo.repository.DespesaRepository;
import com.projeto.modelo.repository.CartaoCreditoRepository;
import com.projeto.modelo.repository.ApontamentoRepository;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.repository.EquipeProjetoRepository;
import com.projeto.modelo.model.entity.EquipeProjeto;
import com.projeto.modelo.service.AuditLogService;
import com.projeto.modelo.service.ContaBancariaService;
import com.projeto.modelo.service.DespesaService;
import com.projeto.modelo.controller.dto.response.SugestaoPagamentoDTO;
import com.projeto.modelo.model.entity.Apontamento;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
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
public class DespesaServiceImp implements DespesaService {

    private final DespesaRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final ProjetoRepository projetoRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final CartaoCreditoRepository cartaoCreditoRepository;
    private final ApontamentoRepository apontamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipeProjetoRepository equipeProjetoRepository;
    private final AuditLogService auditLogService;
    private final ContaBancariaService contaBancariaService;

    @Override
    @Transactional
    public List<DespesaResponseDTO> criar(DespesaRequestDTO dto, Usuario usuarioLogado) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
                
        Projeto projeto = null;
        if (dto.getProjetoId() != null) {
            projeto = projetoRepository.findById(dto.getProjetoId())
                    .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        }
        
        Usuario colaborador = null;
        if (dto.getColaboradorId() != null) {
            colaborador = usuarioRepository.findById(dto.getColaboradorId())
                    .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
        }

        ContaBancaria contaBancaria = null;
        if (dto.getContaBancariaId() != null) {
            contaBancaria = contaBancariaRepository.findById(dto.getContaBancariaId())
                    .orElseThrow(() -> new RuntimeException("Conta Bancária não encontrada"));
        }

        CartaoCredito cartaoCredito = null;
        if (dto.getCartaoCreditoId() != null) {
            cartaoCredito = cartaoCreditoRepository.findById(dto.getCartaoCreditoId())
                    .orElseThrow(() -> new RuntimeException("Cartão de Crédito não encontrado"));
        }

        List<Despesa> geradas = new ArrayList<>();

        if (dto.getTipoRecorrencia() == TipoRecorrencia.UNICA) {
            Despesa despesa = Despesa.builder()
                    .descricao(dto.getDescricao())
                    .valorPrevisto(dto.getValorPrevisto())
                    .dataVencimento(cartaoCredito != null ? calcularVencimentoCartao(dto.getDataVencimento(), cartaoCredito) : dto.getDataVencimento())
                    .categoria(categoria)
                    .projeto(projeto)
                    .conta(contaBancaria)
                    .cartaoCredito(cartaoCredito)
                    .colaborador(colaborador)
                    .mesReferencia(dto.getMesReferencia())
                    .status(StatusDespesa.PENDENTE)
                    .tipoRecorrencia(TipoRecorrencia.UNICA)
                    .build();
            despesa.setCriadoPor(usuarioLogado);
            geradas.add(repository.save(despesa));
            
        } else if (dto.getTipoRecorrencia() == TipoRecorrencia.PARCELADA) {
            if ("PERSONALIZADO".equals(dto.getModoDistribuicao())) {
                BigDecimal soma = dto.getParcelasPersonalizadas().stream()
                        .map(ParcelaPersonalizadaDTO::getValor)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                if (soma.compareTo(dto.getValorTotal()) != 0) {
                    throw new RuntimeException("Em parcelas personalizadas, a soma das parcelas (" + soma + ") deve ser igual ao valor total (" + dto.getValorTotal() + ").");
                }
                
                Despesa pai = null;
                int current = 1;
                for (ParcelaPersonalizadaDTO parc : dto.getParcelasPersonalizadas()) {
                    Despesa despesa = Despesa.builder()
                            .descricao(dto.getDescricao() + " (" + current + "/" + dto.getParcelasPersonalizadas().size() + ")")
                            .valorPrevisto(parc.getValor())
                            .dataVencimento(cartaoCredito != null ? calcularVencimentoCartao(parc.getDataVencimento(), cartaoCredito) : parc.getDataVencimento())
                            .categoria(categoria)
                            .projeto(projeto)
                            .conta(contaBancaria)
                            .cartaoCredito(cartaoCredito)
                            .colaborador(colaborador)
                            .mesReferencia(dto.getMesReferencia())
                            .status(StatusDespesa.PENDENTE)
                            .tipoRecorrencia(TipoRecorrencia.PARCELADA)
                            .parcelaNumero(current)
                            .parcelaTotal(dto.getParcelasPersonalizadas().size())
                            .build();
                    despesa.setCriadoPor(usuarioLogado);
                    
                    if (current == 1) {
                        pai = repository.save(despesa);
                        despesa.setRecorrenciaPai(pai);
                        pai = repository.save(despesa);
                        geradas.add(pai);
                    } else {
                        despesa.setRecorrenciaPai(pai);
                        geradas.add(repository.save(despesa));
                    }
                    current++;
                }
            } else {
                int max = dto.getQuantidadeParcelas();
                BigDecimal base = dto.getValorTotal().divide(BigDecimal.valueOf(max), 2, RoundingMode.DOWN);
                BigDecimal resto = dto.getValorTotal().subtract(base.multiply(BigDecimal.valueOf(max)));

                Despesa pai = null;
                LocalDate vcto = cartaoCredito != null ? calcularVencimentoCartao(dto.getDataVencimento(), cartaoCredito) : dto.getDataVencimento();
                for (int i = 1; i <= max; i++) {
                    BigDecimal valor = (i == max) ? base.add(resto) : base;
                    Despesa despesa = Despesa.builder()
                            .descricao(dto.getDescricao() + " (" + i + "/" + max + ")")
                            .valorPrevisto(valor)
                            .dataVencimento(vcto)
                            .categoria(categoria)
                            .projeto(projeto)
                            .conta(contaBancaria)
                            .cartaoCredito(cartaoCredito)
                            .colaborador(colaborador)
                            .mesReferencia(dto.getMesReferencia())
                            .status(StatusDespesa.PENDENTE)
                            .tipoRecorrencia(TipoRecorrencia.PARCELADA)
                            .parcelaNumero(i)
                            .parcelaTotal(max)
                            .periodicidade(dto.getPeriodicidade())
                            .build();
                    despesa.setCriadoPor(usuarioLogado);

                    if (i == 1) {
                        pai = repository.save(despesa);
                        despesa.setRecorrenciaPai(pai);
                        pai = repository.save(despesa);
                        geradas.add(pai);
                    } else {
                        despesa.setRecorrenciaPai(pai);
                        geradas.add(repository.save(despesa));
                    }
                    vcto = proximoVencimento(vcto, dto.getPeriodicidade());
                }
            }
        } else if (dto.getTipoRecorrencia() == TipoRecorrencia.RECORRENTE) {
             int limite = 12;
             Despesa pai = null;
             LocalDate vcto = cartaoCredito != null ? calcularVencimentoCartao(dto.getDataVencimento(), cartaoCredito) : dto.getDataVencimento();
             for (int i = 1; i <= limite; i++) {
                 Despesa despesa = Despesa.builder()
                         .descricao(dto.getDescricao())
                         .valorPrevisto(dto.getValorPrevisto())
                         .dataVencimento(vcto)
                         .categoria(categoria)
                         .projeto(projeto)
                         .conta(contaBancaria)
                         .cartaoCredito(cartaoCredito)
                         .colaborador(colaborador)
                         .mesReferencia(dto.getMesReferencia())
                         .status(StatusDespesa.PENDENTE)
                         .tipoRecorrencia(TipoRecorrencia.RECORRENTE)
                         .periodicidade(dto.getPeriodicidade())
                         .build();
                 despesa.setCriadoPor(usuarioLogado);

                 if (i == 1) {
                     pai = repository.save(despesa);
                     despesa.setRecorrenciaPai(pai);
                     pai = repository.save(despesa);
                     geradas.add(pai);
                 } else {
                     despesa.setRecorrenciaPai(pai);
                     geradas.add(repository.save(despesa));
                 }
                 vcto = proximoVencimento(vcto, dto.getPeriodicidade());
             }
        }

        auditLogService.registrarLog("Despesa", geradas.get(0).getId(), AcaoAuditLog.CRIOU, usuarioLogado, null, "Despesa cadastrada", null);

        return geradas.stream().map(DespesaResponseDTO::fromEntity).collect(Collectors.toList());
    }

    private LocalDate proximoVencimento(LocalDate base, Periodicidade p) {
        if (p == null) return base.plusMonths(1);
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

    private LocalDate calcularVencimentoCartao(LocalDate dataReferencia, CartaoCredito cartao) {
        int diaFechamento = cartao.getDiaFechamento();
        int diaVencimento = cartao.getDiaVencimento();
        
        LocalDate monthOfBilling = dataReferencia;
        if (dataReferencia.getDayOfMonth() >= diaFechamento) {
            monthOfBilling = monthOfBilling.plusMonths(1);
        }
        
        LocalDate due = monthOfBilling.withDayOfMonth(Math.min(diaVencimento, monthOfBilling.lengthOfMonth()));
        if (diaVencimento < diaFechamento) {
            due = due.plusMonths(1);
        }
        return due;
    }

    @Override
    public DespesaResponseDTO buscarPorId(UUID id) {
        return DespesaResponseDTO.fromEntity(repository.findById(id).orElseThrow(() -> new RuntimeException("Despesa não encontrada")));
    }

    @Override
    public List<DespesaResponseDTO> listarTodos(LocalDate dataInicio, LocalDate dataFim, UUID categoriaId) {
        return repository.findAll().stream()
                .filter(r -> r.getExcluidoEm() == null)
                .filter(r -> {
                    boolean noPeriodo = (dataInicio == null || !r.getDataVencimento().isBefore(dataInicio)) &&
                                        (dataFim == null || !r.getDataVencimento().isAfter(dataFim));
                    boolean pendentePassado = (r.getStatus() == com.projeto.modelo.model.enums.StatusDespesa.PENDENTE || 
                                               r.getStatus() == com.projeto.modelo.model.enums.StatusDespesa.ATRASADO) &&
                                              (dataInicio != null && r.getDataVencimento().isBefore(dataInicio));
                    return noPeriodo || pendentePassado;
                })
                .filter(r -> categoriaId == null || (r.getCategoria() != null && r.getCategoria().getId().equals(categoriaId)))
                .map(DespesaResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DespesaResponseDTO> atualizar(UUID id, DespesaRequestDTO dto, Usuario usuarioLogado) {
        Despesa despesa = repository.findById(id).orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        
        ContaBancaria contaBancaria = null;
        if (dto.getContaBancariaId() != null) {
            contaBancaria = contaBancariaRepository.findById(dto.getContaBancariaId())
                    .orElseThrow(() -> new RuntimeException("Conta Bancária não encontrada"));
        }
        
        CartaoCredito cartaoCredito = null;
        if (dto.getCartaoCreditoId() != null) {
            cartaoCredito = cartaoCreditoRepository.findById(dto.getCartaoCreditoId())
                    .orElseThrow(() -> new RuntimeException("Cartão de Crédito não encontrado"));
        }
        
        String dadosAntigos = "Desc=" + despesa.getDescricao() + ", Vlr=" + despesa.getValorPrevisto();
        
        despesa.setDescricao(dto.getDescricao());
        despesa.setValorPrevisto(dto.getValorPrevisto());
        despesa.setDataVencimento(cartaoCredito != null ? calcularVencimentoCartao(dto.getDataVencimento(), cartaoCredito) : dto.getDataVencimento());
        despesa.setCategoria(categoria);
        despesa.setConta(contaBancaria);
        despesa.setCartaoCredito(cartaoCredito);
        despesa.setAtualizadoPor(usuarioLogado);
        
        String dadosNovos = "Desc=" + dto.getDescricao() + ", Vlr=" + dto.getValorPrevisto();
        
        despesa = repository.save(despesa);
        
        auditLogService.registrarLog("Despesa", despesa.getId(), AcaoAuditLog.EDITOU, usuarioLogado, dadosAntigos, dadosNovos, null);
        
        return List.of(DespesaResponseDTO.fromEntity(despesa));
    }

    @Override
    @Transactional
    public void deletar(UUID id, String escopoExclusao, Usuario usuarioLogado) {
        Despesa despesa = repository.findById(id).orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        
        if (despesa.getStatus() == StatusDespesa.PAGO) {
            throw new RuntimeException("Despesa com status PAGO não pode ser excluída, apenas estornada.");
        }
        
        despesa.setExcluidoEm(LocalDate.now().atStartOfDay());
        despesa.setAtualizadoPor(usuarioLogado);
        repository.save(despesa);
        
        auditLogService.registrarLog("Despesa", despesa.getId(), AcaoAuditLog.EXCLUIU, usuarioLogado, null, "Despesa excluída (Soft Delete)", null);
    }

    @Override
    @Transactional
    public DespesaResponseDTO pagar(UUID id, PagarDespesaDTO dto, Usuario usuarioLogado) {
        Despesa despesa = repository.findById(id).orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        
        ContaBancaria conta = contaBancariaRepository.findById(dto.getContaBancariaId())
                .orElseThrow(() -> new RuntimeException("Conta Bancária não encontrada"));
                
        despesa.setStatus(StatusDespesa.PAGO);
        despesa.setConta(conta);
        despesa.setValorPago(dto.getValorPago());
        despesa.setDataPagamento(dto.getDataPagamento());
        despesa.setAtualizadoPor(usuarioLogado);
        
        repository.save(despesa);
        
        // Pagamento DEDUZ o saldo
        contaBancariaService.atualizarSaldo(conta.getId(), dto.getValorPago().negate());
        
        auditLogService.registrarLog("Despesa", despesa.getId(), AcaoAuditLog.RECEBEU, usuarioLogado, null, "Pagamento efetuado. Valor: " + dto.getValorPago() + " em " + conta.getNome(), null);
        
        return DespesaResponseDTO.fromEntity(despesa);
    }

    @Override
    @Transactional
    public List<DespesaResponseDTO> estornar(UUID id, EstornarDespesaDTO dto, Usuario usuarioLogado) {
        Despesa despesa = repository.findById(id).orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        
        if (despesa.getStatus() != StatusDespesa.PAGO) {
             throw new RuntimeException("Somente despesas pagas podem ser estornadas.");
        }
        
        ContaBancaria conta = despesa.getConta();
        
        despesa.setStatus(StatusDespesa.CANCELADA);
        despesa.setAtualizadoPor(usuarioLogado);
        
        repository.save(despesa);
        
        // Estorno DEVOLVE o saldo
        contaBancariaService.atualizarSaldo(conta.getId(), despesa.getValorPago());
        
        auditLogService.registrarLog("Despesa", despesa.getId(), AcaoAuditLog.ESTORNOU, usuarioLogado, null, "Estorno: " + dto.getMotivo() + ". Valor devolvido ao caixa: " + despesa.getValorPago(), null);
        
        return List.of(DespesaResponseDTO.fromEntity(despesa));
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void atualizarStatusAtraso() {
        // Encontrar as <LocalDate.now() não funciona direto pelo repository a menos que tenhamos o método certo.
        // Já que ele não existe explicitamente (vou buscar por findAll e iterar para garantir safety ou posso iterar stream)
        List<Despesa> pendentes = repository.findAll().stream()
                .filter(d -> d.getStatus() == StatusDespesa.PENDENTE)
                .filter(d -> d.getDataVencimento().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
        
        for (Despesa d : pendentes) {
            d.setStatus(StatusDespesa.ATRASADO);
            repository.save(d);
auditLogService.registrarLog("Despesa", d.getId(), AcaoAuditLog.EDITOU, null, "Status: PENDENTE", "Status: ATRASADO (Job automático do sistema)", null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SugestaoPagamentoDTO> buscarSugestoesPagamento(String mesAno) {
        // mesAno vem no formato yyyy-MM. Queremos as horas do mês anterior.
        YearMonth currentMonth = YearMonth.parse(mesAno, DateTimeFormatter.ofPattern("yyyy-MM"));
        YearMonth prevMonth = currentMonth.minusMonths(1);
        
        LocalDate dataInicio = prevMonth.atDay(1);
        LocalDate dataFim = prevMonth.atEndOfMonth();
        
        List<Apontamento> apontamentos = apontamentoRepository.findByDataApontamentoBetween(dataInicio, dataFim);
        
        class ValoresColaborador {
            BigDecimal salarioFixo = BigDecimal.ZERO;
            BigDecimal valorHoras = BigDecimal.ZERO;
            BigDecimal quantidadeHoras = BigDecimal.ZERO;
            BigDecimal getValorTotal() {
                return salarioFixo.add(valorHoras);
            }
        }
        
        // Agrupar o VALOR a pagar por colaborador
        Map<Usuario, ValoresColaborador> valorPorColaborador = new HashMap<>();
        
        // 1. Adicionar o Salário Fixo para todos os colaboradores ativos que possuem valor fixo
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        for (Usuario u : todosUsuarios) {
            if (u.getStatus() == com.projeto.modelo.model.enums.UsuarioStatus.ATIVO 
                && u.getValorFixo() != null 
                && u.getValorFixo().compareTo(BigDecimal.ZERO) > 0) {
                
                boolean hasProjetoSalarioFixo = equipeProjetoRepository.findByColaboradorId(u.getId()).stream()
                            .anyMatch(ep -> Boolean.TRUE.equals(ep.getUsaSalarioFixo()));
                            
                if (hasProjetoSalarioFixo) {
                    ValoresColaborador vc = new ValoresColaborador();
                    vc.salarioFixo = u.getValorFixo();
                    valorPorColaborador.put(u, vc);
                }
            }
        }
        
        // 2. Adicionar 160 horas default para usuários tipo FIXO que não recebem salário fixo
        // em meses atuais ou futuros, se não tiverem apontado horas ainda.
        YearMonth now = YearMonth.now();
        boolean isCurrentOrFuture = !prevMonth.isBefore(now);

        for (Usuario u : todosUsuarios) {
            if (u.getStatus() == com.projeto.modelo.model.enums.UsuarioStatus.ATIVO 
                && u.getTipoContratacao() == com.projeto.modelo.model.enums.TipoContratacao.FIXO
                && (u.getValorFixo() == null || u.getValorFixo().compareTo(BigDecimal.ZERO) == 0)
                && u.getValorHora() != null 
                && u.getValorHora().compareTo(BigDecimal.ZERO) > 0
                && isCurrentOrFuture) {
                
                // Se for mes atual/futuro e o cara for FIXO e não tiver salario fixo, injetamos as 160h.
                // Mas pera, se ele lançou algumas horas já, não vamos sobrepor, a menos que ele tenha 0 horas.
                // Inicializamos com 0, se chegar no fim e ele não tiver apontado, vai continuar 160.
                // Mas aqui podemos inicializar já com 160. Se ele tiver apontamentos, no loop abaixo a gente soma as apontadas 
                // e teria que tirar essas 160. A regra diz: "caso não tenha lançado horas ainda... considere 160".
                // Ou seja, se o apontamento total for zero, considerar 160. Vamos fazer isso DEPOIS de somar os apontamentos!
            }
        }
        
        // 2. Somar o valor das horas apontadas apenas para projetos onde NÃO usa salário fixo
        for (Apontamento ap : apontamentos) {
            Usuario colab = ap.getColaborador();
            if (colab == null || ap.getProjeto() == null || colab.getStatus() == com.projeto.modelo.model.enums.UsuarioStatus.INATIVO) continue;
            
            Boolean usaSalarioFixo = apontamentoRepository.findUsaSalarioFixoByProjetoAndColaborador(ap.getProjeto().getId(), colab.getId());
            
            if (Boolean.TRUE.equals(usaSalarioFixo)) {
                // Horas estão cobertas pelo salário fixo, não gera conta a pagar extra (apenas entra no custo do projeto)
                continue;
            }
            
            // Se não recebe salário fixo para esse projeto, soma o valor das horas
            if (colab.getValorHora() != null && colab.getValorHora().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal valorHoras = ap.getHoras().multiply(colab.getValorHora());
                ValoresColaborador vc = valorPorColaborador.getOrDefault(colab, new ValoresColaborador());
                vc.valorHoras = vc.valorHoras.add(valorHoras);
                vc.quantidadeHoras = vc.quantidadeHoras.add(ap.getHoras());
                valorPorColaborador.put(colab, vc);
            }
        }

        // 3. Aplicar regra de 160h para usuários tipo FIXO em meses atuais ou futuros que não apontaram horas
        if (isCurrentOrFuture) {
            for (Usuario u : todosUsuarios) {
                if (u.getStatus() == com.projeto.modelo.model.enums.UsuarioStatus.ATIVO
                    && u.getTipoContratacao() == com.projeto.modelo.model.enums.TipoContratacao.FIXO
                    && u.getValorHora() != null
                    && u.getValorHora().compareTo(BigDecimal.ZERO) > 0) {
                    
                    // Verifica se o usuário possui algum projeto configurado com "usaSalarioFixo" = true
                    boolean hasProjetoSalarioFixo = equipeProjetoRepository.findByColaboradorId(u.getId()).stream()
                            .anyMatch(ep -> Boolean.TRUE.equals(ep.getUsaSalarioFixo()));
                    
                    if (!hasProjetoSalarioFixo) {
                        ValoresColaborador vc = valorPorColaborador.getOrDefault(u, new ValoresColaborador());
                        if (vc.quantidadeHoras.compareTo(BigDecimal.ZERO) == 0) {
                            vc.quantidadeHoras = new BigDecimal("160");
                            vc.valorHoras = new BigDecimal("160").multiply(u.getValorHora());
                            valorPorColaborador.put(u, vc);
                        }
                    }
                }
            }
        }
        
        String prevMonthStr = prevMonth.toString(); // "yyyy-MM"
        
        List<SugestaoPagamentoDTO> sugestoes = new ArrayList<>();
        
        for (Map.Entry<Usuario, ValoresColaborador> entry : valorPorColaborador.entrySet()) {
            Usuario colab = entry.getKey();
            ValoresColaborador vc = entry.getValue();
            
            // Verifica se ja existe despesa
            boolean jaLancado = repository.findAll().stream()
                .anyMatch(d -> d.getExcluidoEm() == null 
                            && d.getColaborador() != null 
                            && d.getColaborador().getId().equals(colab.getId())
                            && prevMonthStr.equals(d.getMesReferencia()));
                            
            if (!jaLancado) {
                BigDecimal valorSugerido = vc.getValorTotal();
                
                SugestaoPagamentoDTO sugestao = new SugestaoPagamentoDTO();
                sugestao.setId(UUID.randomUUID()); // ID fake só pro frontend renderizar key
                sugestao.setDescricao(colab.getNome() + " ref. " + prevMonthStr);
                sugestao.setValorPrevisto(valorSugerido);
                sugestao.setDataVencimento(LocalDate.now().toString()); // Vencimento pro dia atual (sugestão)
                sugestao.setStatus("SUGESTAO");
                sugestao.setColaboradorId(colab.getId());
                sugestao.setMesReferencia(prevMonthStr);
                sugestao.setValorSalarioFixo(vc.salarioFixo);
                sugestao.setValorHoras(vc.valorHoras);
                sugestao.setQuantidadeHoras(vc.quantidadeHoras);
                
                sugestoes.add(sugestao);
            }
        }
        
        return sugestoes;
    }
}

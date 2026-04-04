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
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.AcaoAuditLog;
import com.projeto.modelo.model.enums.Periodicidade;
import com.projeto.modelo.model.enums.StatusDespesa;
import com.projeto.modelo.model.enums.TipoRecorrencia;
import com.projeto.modelo.repository.CategoriaRepository;
import com.projeto.modelo.repository.ContaBancariaRepository;
import com.projeto.modelo.repository.ProjetoRepository;
import com.projeto.modelo.repository.DespesaRepository;
import com.projeto.modelo.service.AuditLogService;
import com.projeto.modelo.service.ContaBancariaService;
import com.projeto.modelo.service.DespesaService;
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

        ContaBancaria contaBancaria = null;
        if (dto.getContaBancariaId() != null) {
            contaBancaria = contaBancariaRepository.findById(dto.getContaBancariaId())
                    .orElseThrow(() -> new RuntimeException("Conta Bancária não encontrada"));
        }

        List<Despesa> geradas = new ArrayList<>();

        if (dto.getTipoRecorrencia() == TipoRecorrencia.UNICA) {
            Despesa despesa = Despesa.builder()
                    .descricao(dto.getDescricao())
                    .valorPrevisto(dto.getValorPrevisto())
                    .dataVencimento(dto.getDataVencimento())
                    .categoria(categoria)
                    .projeto(projeto)
                    .conta(contaBancaria)
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
                            .dataVencimento(parc.getDataVencimento())
                            .categoria(categoria)
                            .projeto(projeto)
                            .conta(contaBancaria)
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
                LocalDate vcto = dto.getDataVencimento();
                for (int i = 1; i <= max; i++) {
                    BigDecimal valor = (i == max) ? base.add(resto) : base;
                    Despesa despesa = Despesa.builder()
                            .descricao(dto.getDescricao() + " (" + i + "/" + max + ")")
                            .valorPrevisto(valor)
                            .dataVencimento(vcto)
                            .categoria(categoria)
                            .projeto(projeto)
                            .conta(contaBancaria)
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
             LocalDate vcto = dto.getDataVencimento();
             for (int i = 1; i <= limite; i++) {
                 Despesa despesa = Despesa.builder()
                         .descricao(dto.getDescricao())
                         .valorPrevisto(dto.getValorPrevisto())
                         .dataVencimento(vcto)
                         .categoria(categoria)
                         .projeto(projeto)
                         .conta(contaBancaria)
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

    @Override
    public DespesaResponseDTO buscarPorId(UUID id) {
        return DespesaResponseDTO.fromEntity(repository.findById(id).orElseThrow(() -> new RuntimeException("Despesa não encontrada")));
    }

    @Override
    public List<DespesaResponseDTO> listarTodos(LocalDate dataInicio, LocalDate dataFim, UUID categoriaId) {
        return repository.findAll().stream()
                .filter(r -> r.getExcluidoEm() == null)
                .filter(r -> dataInicio == null || !r.getDataVencimento().isBefore(dataInicio))
                .filter(r -> dataFim == null || !r.getDataVencimento().isAfter(dataFim))
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
        
        String dadosAntigos = "Desc=" + despesa.getDescricao() + ", Vlr=" + despesa.getValorPrevisto();
        
        despesa.setDescricao(dto.getDescricao());
        despesa.setValorPrevisto(dto.getValorPrevisto());
        despesa.setDataVencimento(dto.getDataVencimento());
        despesa.setCategoria(categoria);
        despesa.setConta(contaBancaria);
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
}

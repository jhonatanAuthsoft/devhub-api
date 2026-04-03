package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.request.CadastrarApontamentoDTO;
import com.projeto.modelo.controller.dto.response.ApontamentoResponseDTO;
import com.projeto.modelo.model.entity.Apontamento;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.repository.ApontamentoRepository;
import com.projeto.modelo.repository.ProjetoRepository;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.service.ApontamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApontamentoServiceImp implements ApontamentoService {

    private final ApontamentoRepository repository;
    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ApontamentoResponseDTO cadastrar(CadastrarApontamentoDTO dto) {
        Projeto projeto = projetoRepository.findById(dto.projetoId())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        Usuario colaborador = usuarioRepository.findById(dto.colaboradorId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));


        // VALIDATION LOGIC
        // Verificar se o colaborador usa salário fixo neste projeto
        Boolean usaSalarioFixo = repository.findUsaSalarioFixoByProjetoAndColaborador(dto.projetoId(), dto.colaboradorId());
        
        // Se usa salário fixo OU é ALOCACAO, não aplicar validação de horas
        if (Boolean.TRUE.equals(usaSalarioFixo) || "ALOCACAO".equals(projeto.getTipoProjeto().toString()) || "REPASSE_DE_DEMANDA".equals(projeto.getTipoProjeto().toString())) {
            // Colaborador com salário fixo ou em projeto de Alocação pode lançar horas ilimitadas
        } else if (!Boolean.TRUE.equals(projeto.getPermiteUltrapassarHoras())) {
            // Para projetos SOB_MEDIDA, validar contra as horas alocadas para o colaborador específico
            // Para outros tipos (ALOCACAO, HORAS_AVULSA), validar contra horas estimadas do projeto
            BigDecimal horasLimite;
            
            if ("SOB_MEDIDA".equals(projeto.getTipoProjeto().toString())) {
                // Buscar horas alocadas para este colaborador específico
                BigDecimal horasAlocadas = repository.sumHorasByProjetoIdAndColaboradorId(dto.projetoId(), dto.colaboradorId());
                if (horasAlocadas == null) horasAlocadas = BigDecimal.ZERO;
                
                // Buscar a alocação do colaborador na equipe
                BigDecimal horasPrevistas = repository.findHorasPrevistasByProjetoAndColaborador(dto.projetoId(), dto.colaboradorId());
                horasLimite = horasPrevistas != null ? horasPrevistas : BigDecimal.ZERO;
                
                BigDecimal novaSoma = horasAlocadas.add(dto.horas());
                
                if (novaSoma.compareTo(horasLimite) > 0) {
                    throw new RuntimeException("O apontamento excede as horas alocadas para você neste projeto (" + horasLimite + "h). Saldo atual: " + horasAlocadas + "h. Tentativa: " + dto.horas() + "h.");
                }
            } else {
                // Para ALOCACAO e HORAS_AVULSA, usar horas estimadas do projeto
                BigDecimal horasAtuais = repository.sumHorasByProjetoId(dto.projetoId());
                horasLimite = projeto.getHorasEstimadas() != null ? projeto.getHorasEstimadas() : BigDecimal.ZERO;
                BigDecimal novaSoma = horasAtuais.add(dto.horas());
                
                if (novaSoma.compareTo(horasLimite) > 0) {
                    throw new RuntimeException("O apontamento excede as horas estimadas do projeto (" + horasLimite + "h). Saldo atual: " + horasAtuais + "h. Tentativa: " + dto.horas() + "h.");
                }
            }
        }

        LocalDate dataNova = dto.dataApontamento();
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimiteMesAnterior = hoje.withDayOfMonth(5);

        // Verifica a data do novo lançamento
        if (dataNova.isBefore(hoje.withDayOfMonth(1))) { // Se for lançada para meses anteriores
            if (hoje.isAfter(dataLimiteMesAnterior) || dataNova.isBefore(hoje.minusMonths(1).withDayOfMonth(1))) {
                 // Se hoje for depois do dia 5 do mês atual, ou a nova data for anterior ao mês passado
                throw new RuntimeException("A data do apontamento deve ser dentro do mês atual, ou do mês anterior até o 5º dia.");
            }
        }


        Apontamento apontamento = Apontamento.builder()
                .projeto(projeto)
                .colaborador(colaborador)
                .dataApontamento(dto.dataApontamento())
                .horas(dto.horas())
                .descricao(dto.descricao())
                .build();

        apontamento = repository.save(apontamento);
        return mapToDTO(apontamento);
    }

    @Override
    public ApontamentoResponseDTO atualizar(UUID id, CadastrarApontamentoDTO dto) {
        Apontamento apontamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apontamento não encontrado"));

        LocalDate dataAntiga = apontamento.getDataApontamento();
        LocalDate dataNova = dto.dataApontamento();
        LocalDate hoje = LocalDate.now();

        // Data limite para apontamentos do mês anterior (dia 5 do mês atual)
        LocalDate dataLimiteMesAnterior = hoje.withDayOfMonth(5);

        // Verifica a data original
        if (dataAntiga.isBefore(hoje.withDayOfMonth(1))) { // Se for de meses anteriores
            if (hoje.isAfter(dataLimiteMesAnterior) || dataAntiga.isBefore(hoje.minusMonths(1).withDayOfMonth(1))) {
                // Se hoje for depois do dia 5 do mês atual, ou a data original for anterior ao mês passado
                throw new RuntimeException("Não é possível alterar apontamentos de meses passados após o 5º dia do mês corrente.");
            }
        }

        // Verifica a nova data
        if (dataNova.isBefore(hoje.withDayOfMonth(1))) { // Se for lançada para meses anteriores
            if (hoje.isAfter(dataLimiteMesAnterior) || dataNova.isBefore(hoje.minusMonths(1).withDayOfMonth(1))) {
                 // Se hoje for depois do dia 5 do mês atual, ou a nova data for anterior ao mês passado
                throw new RuntimeException("A nova data do apontamento deve ser dentro do mês atual, ou do mês anterior até o 5º dia.");
            }
        }
        
        // Atualizar campos permitidos
        apontamento.setDataApontamento(dto.dataApontamento());
        apontamento.setHoras(dto.horas());
        apontamento.setDescricao(dto.descricao());
        
        // Se necessário atualizar projeto/colaborador, descomentar abaixo, 
        // mas geralmente edição rápida é só dados. Se mudar projeto, precisaria revalidar regras de projeto.
        // Por simplificação e segurança, manteremos no mesmo projeto/colaborador por enquanto ou assumimos que o DTO traz os mesmos.
        // Se o DTO trouxer projeto diferente, teríamos que buscar e setar. 
        // Vamos assumir que a edição é focada em corrigir o lançamento (hora/descrição/dia).
        
        apontamento = repository.save(apontamento);
        return mapToDTO(apontamento);
    }

    @Override
    public List<ApontamentoResponseDTO> listarPorProjeto(UUID projetoId, LocalDate dataInicio, LocalDate dataFim, UUID colaboradorId) {
        org.springframework.data.jpa.domain.Specification<Apontamento> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            
            if (projetoId != null) {
                predicates.add(cb.equal(root.get("projeto").get("id"), projetoId));
            }

            if (colaboradorId != null) {
                predicates.add(cb.equal(root.get("colaborador").get("id"), colaboradorId));
            }
            
            if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataApontamento"), dataInicio));
            }
            
            if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataApontamento"), dataFim));
            }

            // Order by date descending
            query.orderBy(cb.desc(root.get("dataApontamento")));
            
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        List<Apontamento> apontamentos = repository.findAll(spec);
        
        return apontamentos.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ApontamentoResponseDTO mapToDTO(Apontamento a) {
        return new ApontamentoResponseDTO(
                a.getId(),
                a.getProjeto().getId(),
                a.getProjeto().getTitulo(),
                a.getColaborador().getNome(),
                a.getDataApontamento(),
                a.getHoras(),
                a.getDescricao(),
                a.getCreatedAt()
        );
    }
}

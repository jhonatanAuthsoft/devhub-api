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
        if (!Boolean.TRUE.equals(projeto.getPermiteUltrapassarHoras())) {
            BigDecimal horasAtuais = repository.sumHorasByProjetoId(dto.projetoId());
            BigDecimal horasEstimadas = projeto.getHorasEstimadas() != null ? projeto.getHorasEstimadas() : BigDecimal.ZERO;
            BigDecimal novaSoma = horasAtuais.add(dto.horas());

            if (novaSoma.compareTo(horasEstimadas) > 0) {
                throw new RuntimeException("O apontamento excede as horas estimadas do projeto (" + horasEstimadas + "h). Saldo atual: " + horasAtuais + "h. Tentativa: " + dto.horas() + "h.");
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
    public List<ApontamentoResponseDTO> listarPorProjeto(UUID projetoId) {
        return repository.findByProjetoId(projetoId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ApontamentoResponseDTO mapToDTO(Apontamento a) {
        return new ApontamentoResponseDTO(
                a.getId(),
                a.getProjeto().getTitulo(),
                a.getColaborador().getNome(),
                a.getDataApontamento(),
                a.getHoras(),
                a.getDescricao(),
                a.getCreatedAt()
        );
    }
}

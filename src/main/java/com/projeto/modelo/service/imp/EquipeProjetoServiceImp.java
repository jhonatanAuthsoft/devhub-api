package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.request.AtualizarHorasDTO;
import com.projeto.modelo.controller.dto.response.AlocacaoHorasResponseDTO;
import com.projeto.modelo.model.entity.EquipeProjeto;
import com.projeto.modelo.repository.ApontamentoRepository;
import com.projeto.modelo.repository.EquipeProjetoRepository;
import com.projeto.modelo.service.EquipeProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipeProjetoServiceImp implements EquipeProjetoService {

    private final EquipeProjetoRepository equipeProjetoRepository;
    private final ApontamentoRepository apontamentoRepository;

    @Override
    public List<AlocacaoHorasResponseDTO> listarAlocacao(UUID projetoId) {
        List<EquipeProjeto> equipe = equipeProjetoRepository.findByProjetoId(projetoId);
        
        return equipe.stream().map(membro -> {
            
            // Valor de Desenvolvimento do Projeto (base de cálculo)
            BigDecimal valorDesenvolvimento = membro.getProjeto().getValorDesenvolvimento();
            if (valorDesenvolvimento == null) valorDesenvolvimento = BigDecimal.ZERO;

            // Porcentagem do membro
            BigDecimal porcentagem = membro.getPorcentagem() != null ? membro.getPorcentagem() : BigDecimal.ZERO;

            // Valor Hora do Usuario
            BigDecimal valorHora = membro.getColaborador().getValorHora();
            if (valorHora == null || valorHora.compareTo(BigDecimal.ZERO) == 0) {
                 // Evitar divisão por zero se usuario nao tiver valor hora. 
                 // Nesse caso custo/horas seria 0 ou indefinido.
                 valorHora = BigDecimal.ONE; // Placeholder para não quebrar ? Ou deixar null
            }
            
            // Custo Calculado = Valor Desenv * (Porcentagem / 100)
            BigDecimal custoCalculado = valorDesenvolvimento.multiply(porcentagem).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            // Horas Calculadas = Custo Calculado / Valor Hora
            BigDecimal horasCalculadas = BigDecimal.ZERO;
            if (membro.getColaborador().getValorHora() != null && membro.getColaborador().getValorHora().compareTo(BigDecimal.ZERO) > 0) {
                horasCalculadas = custoCalculado.divide(membro.getColaborador().getValorHora(), 2, RoundingMode.HALF_UP);
            }

            // NOVOS CAMPOS: Rastreamento de Uso
            // Horas Utilizadas = Soma de apontamentos deste colaborador neste projeto
            BigDecimal horasUtilizadas = apontamentoRepository.sumHorasByProjetoIdAndColaboradorId(projetoId, membro.getColaborador().getId());
            if (horasUtilizadas == null) horasUtilizadas = BigDecimal.ZERO;

            // Valor Utilizado = Horas Utilizadas * Valor Hora
            BigDecimal valorUtilizado = horasUtilizadas.multiply(membro.getColaborador().getValorHora() != null ? membro.getColaborador().getValorHora() : BigDecimal.ZERO);

            // Saldo Horas = Horas Previstas - Horas Utilizadas
            BigDecimal horasPrevistas = membro.getHorasPrevistas() != null ? membro.getHorasPrevistas() : BigDecimal.ZERO;
            BigDecimal saldoHoras = horasPrevistas.subtract(horasUtilizadas);

            // Saldo Valor = Custo Previsto - Valor Utilizado
            BigDecimal custoPrevisto = membro.getCustoPrevisto() != null ? membro.getCustoPrevisto() : BigDecimal.ZERO;
            BigDecimal saldoValor = custoPrevisto.subtract(valorUtilizado);

            return new AlocacaoHorasResponseDTO(
                membro.getId(),
                membro.getColaborador().getNome(),
                membro.getFuncao(),
                membro.getColaborador().getValorHora(),
                porcentagem,
                custoCalculado,
                horasCalculadas,
                membro.getHorasPrevistas(),
                membro.getCustoPrevisto(),
                horasUtilizadas,
                valorUtilizado,
                saldoHoras,
                saldoValor
            );
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void atualizarHoras(UUID equipeId, AtualizarHorasDTO dto) {
        EquipeProjeto membro = equipeProjetoRepository.findById(equipeId)
            .orElseThrow(() -> new RuntimeException("Membro da equipe não encontrado"));
        
        membro.setHorasPrevistas(dto.horasPrevistas());
        
        // Calcular Custo Previsto
        BigDecimal valorHora = membro.getColaborador().getValorHora();
        if (valorHora == null) valorHora = BigDecimal.ZERO;
        
        BigDecimal custo = dto.horasPrevistas() != null 
            ? dto.horasPrevistas().multiply(valorHora).setScale(2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
            
        membro.setCustoPrevisto(custo);
        
        equipeProjetoRepository.save(membro);
    }
}

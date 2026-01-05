package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.model.enums.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CadastrarProjetoDTO(
    String titulo,
    String descricao,
    String requisitos,
    UUID clienteId,
    UUID vendedorId,
    LocalDate dataInicio,
    LocalDate dataFimDesenv,
    LocalDate dataFimProjeto,
    TipoProjeto tipoProjeto,
    TipoVenda tipoVenda,
    UUID projetoOrigemId,
    String nomeIndicacao,
    BigDecimal valorTotal,
    BigDecimal valorContratoMensal,
    BigDecimal horasEstimadas,
    Integer quantidadeParcelas,
    Boolean emitirNf,
    Boolean permiteUltrapassarHoras,
    StatusProjeto status,
    List<ParcelaProjetoRequestDTO> parcelas,
    List<LinkProjetoRequestDTO> links,
    List<EquipeProjetoRequestDTO> equipe
) {}

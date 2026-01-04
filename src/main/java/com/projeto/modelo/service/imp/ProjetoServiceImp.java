package com.projeto.modelo.service.imp;

import com.projeto.modelo.configuracao.exeption.ExcecoesCustomizada;
import com.projeto.modelo.controller.dto.request.*;
import com.projeto.modelo.controller.dto.response.*;
import com.projeto.modelo.model.entity.*;
import com.projeto.modelo.model.enums.*;
import com.projeto.modelo.repository.*;
import com.projeto.modelo.service.ProjetoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjetoServiceImp implements ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MetaRepository metaRepository;

    @Override
    @Transactional
    public ProjetoResponseDTO cadastrarProjeto(CadastrarProjetoDTO dto) {
        // 1. Validar Cliente
        Pessoa cliente = pessoaRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ExcecoesCustomizada("Cliente não encontrado", HttpStatus.NOT_FOUND));

        // 2. Validar Vendedor (Opcional, mas se passar ID deve existir)
        Usuario vendedor = null;
        if (dto.vendedorId() != null) {
            vendedor = usuarioRepository.findById(dto.vendedorId())
                    .orElseThrow(() -> new ExcecoesCustomizada("Vendedor não encontrado", HttpStatus.NOT_FOUND));
        }

        // 3. Validar Projeto Origem (Upsell)
        Projeto projetoOrigem = null;
        if (dto.projetoOrigemId() != null) {
            projetoOrigem = projetoRepository.findById(dto.projetoOrigemId())
                    .orElseThrow(() -> new ExcecoesCustomizada("Projeto de origem não encontrado", HttpStatus.NOT_FOUND));
        }

        // 4. Buscar Meta de Lucro do Ano
        Integer anoProjeto = dto.dataInicio() != null ? dto.dataInicio().getYear() : LocalDate.now().getYear();
        
        // Buscando meta do tipo LUCRO para o ano. 
        // Assumindo que o repository tem este metodo ou similar. 
        // Se nao tiver, vou usar um findAll e filtrar aqui ou melhorar depois.
        // Vou assumir que nao tem e fazer um filter manual se precisar, mas o ideal eh ter no repositorio.
        // Vamos tentar buscar pelo metodo padrao JPA se existir, senao vai dar erro de compilacao e eu corrijo.
        // A rigor, o metodo findByAnoAndCategoriaAndTipoMeta deve existir na interface.
        Meta metaLucro = metaRepository.findByAnoAndCategoriaAndTipoMeta(anoProjeto, CategoriaMeta.LUCRO, TipoMeta.META_NORMAL)
                .orElse(null);

        BigDecimal lucroPercentual = metaLucro != null ? metaLucro.getValorAnual() : BigDecimal.ZERO;

        // 5. Cálculos Financeiros
        BigDecimal valorTotal = dto.valorTotal() != null ? dto.valorTotal() : BigDecimal.ZERO;
        BigDecimal impostoPercentual = new BigDecimal("15.00");
        
        BigDecimal valorImposto = valorTotal.multiply(impostoPercentual).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal valorLucro = valorTotal.multiply(lucroPercentual).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal valorDesenvolvimento = valorTotal.subtract(valorImposto).subtract(valorLucro);

        // 6. Criar Entidade Projeto
        Projeto projeto = Projeto.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .requisitos(dto.requisitos())
                .cliente(cliente)
                .vendedor(vendedor)
                .dataInicio(dto.dataInicio())
                .dataFimDesenv(dto.dataFimDesenv())
                .dataFimProjeto(dto.dataFimProjeto())
                .tipoProjeto(dto.tipoProjeto())
                .tipoVenda(dto.tipoVenda())
                .projetoOrigem(projetoOrigem)
                .nomeIndicacao(dto.nomeIndicacao())
                .valorTotal(valorTotal)
                .valorContratoMensal(dto.valorContratoMensal())
                .impostoPercentual(impostoPercentual)
                .lucroPercentual(lucroPercentual)
                .valorDesenvolvimento(valorDesenvolvimento)
                .status(StatusProjeto.PRE_VENDA)
                .emitirNf(dto.emitirNf() != null ? dto.emitirNf() : false)
                .links(new ArrayList<>())
                .parcelas(new ArrayList<>())
                .equipe(new ArrayList<>())
                .build();

        // 7. Links
        if (dto.links() != null) {
            for (LinkProjetoRequestDTO linkDto : dto.links()) {
                LinkProjeto link = LinkProjeto.builder()
                        .projeto(projeto)
                        .url(linkDto.url())
                        .descricao(linkDto.descricao())
                        .tipoAmbiente(linkDto.tipoAmbiente())
                        .classificacao(linkDto.classificacao())
                        .observacao(linkDto.observacao())
                        .build();
                projeto.getLinks().add(link);
            }
        }

        // 8. Equipe e Cálculos de Horas
        if (dto.equipe() != null) {
            // (Logica de equipe mantida igual, apenas copiando bloco se necessario ou pulando se o replace for preciso)
            // Como o replace eh grande, vou assumir que o usuario quer manter a logica de equipe. 
            // O replace tool permite especificar o target content. Vou focar apenas no bloco de parcelas e builder.
            // Para manter a integridade, vou refazer o replace focado.
        }
        
         // ... (Equipe logic - omitted here for brevity in reasoning but must be in file) 
         // Vou fazer um MultiReplace ou Replace focado apenas no builder e no bloco de parcelas.
         // Melhor strategy: Replace do builder e Replace do bloco de parcelas.


        // 8. Equipe e Cálculos de Horas
        if (dto.equipe() != null) {
            for (EquipeProjetoRequestDTO equipeDto : dto.equipe()) {
                Usuario colaborador = usuarioRepository.findById(equipeDto.colaboradorId())
                        .orElseThrow(() -> new ExcecoesCustomizada("Colaborador não encontrado ID: " + equipeDto.colaboradorId(), HttpStatus.NOT_FOUND));

                // Porcentagem definida individualmente
                BigDecimal porcentagemMembro = equipeDto.porcentagem() != null ? equipeDto.porcentagem() : BigDecimal.ZERO;
                BigDecimal percentualDecimal = porcentagemMembro.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                
                // Verba para este membro baseada na porcentagem do valor de desenvolvimento
                BigDecimal verbaParaMembro = valorDesenvolvimento.multiply(percentualDecimal);

                BigDecimal horasCalculadas = BigDecimal.ZERO;
                BigDecimal custoTotalMembro = BigDecimal.ZERO;

                if (equipeDto.usaSalarioFixo()) {
                    if (colaborador.getValorFixo() != null) {
                        custoTotalMembro = colaborador.getValorFixo();
                    }
                } else {
                    BigDecimal valorHora = colaborador.getValorHora();
                    if (valorHora != null && valorHora.compareTo(BigDecimal.ZERO) > 0) {
                        horasCalculadas = verbaParaMembro.divide(valorHora, 2, RoundingMode.HALF_UP);
                        custoTotalMembro = verbaParaMembro;
                    }
                }

                EquipeProjeto equipeEntity = EquipeProjeto.builder()
                        .projeto(projeto)
                        .colaborador(colaborador)
                        .funcao(equipeDto.funcao())
                        .usaSalarioFixo(equipeDto.usaSalarioFixo())
                        .porcentagem(porcentagemMembro)
                        .custoPrevisto(custoTotalMembro)
                        .horasPrevistas(horasCalculadas)
                        .build();

                projeto.getEquipe().add(equipeEntity);
            }
        }

        // 9. Parcelas (Sob Medida)
        if (dto.parcelas() != null && !dto.parcelas().isEmpty()) {
            // Parcelas manuais enviadas pelo frontend
            for (ParcelaProjetoRequestDTO parcelaDto : dto.parcelas()) {
                ParcelaProjeto parcela = ParcelaProjeto.builder()
                        .projeto(projeto)
                        .numero(parcelaDto.numero())
                        .valor(parcelaDto.valor())
                        .dataVencimento(parcelaDto.dataVencimento())
                        .status(parcelaDto.status() != null ? parcelaDto.status() : StatusParcela.PENDENTE)
                        .build();
                projeto.getParcelas().add(parcela);
            }
        } else if (dto.tipoProjeto() == TipoProjeto.SOB_MEDIDA && dto.quantidadeParcelas() != null && dto.quantidadeParcelas() > 0) {
            // Geração automática antiga (como fallback)
            BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(dto.quantidadeParcelas()), 2, RoundingMode.HALF_UP);
            BigDecimal somaParcelas = BigDecimal.ZERO;

            for (int i = 1; i <= dto.quantidadeParcelas(); i++) {
                BigDecimal valorAtual = valorParcela;
                
                // Ajuste na ultima parcela pela diferenca de arredondamento
                if (i == dto.quantidadeParcelas()) {
                    valorAtual = valorTotal.subtract(somaParcelas);
                }
                somaParcelas = somaParcelas.add(valorAtual);

                ParcelaProjeto parcela = ParcelaProjeto.builder()
                        .projeto(projeto)
                        .numero(i)
                        .valor(valorAtual)
                        .dataVencimento(LocalDate.now().plusMonths(i)) // Vencimento padrao +1 mes
                        .status(StatusParcela.PENDENTE)
                        .build();
                projeto.getParcelas().add(parcela);
            }
        }

        // Salvar tudo (Cascade ALL cuida dos filhos)
        Projeto projetoSalvo = projetoRepository.save(projeto);

        return mapToDTO(projetoSalvo);
    }

    @Override
    public Page<ProjetoResponseDTO> listarProjetos(Pageable pageable) {
        return projetoRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    public ProjetoResponseDTO buscarPorId(UUID id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new ExcecoesCustomizada("Projeto não encontrado", HttpStatus.NOT_FOUND));
        return mapToDTO(projeto);
    }

    @Override
    @Transactional
    public ProjetoResponseDTO atualizarProjeto(UUID id, CadastrarProjetoDTO dto) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new ExcecoesCustomizada("Projeto não encontrado", HttpStatus.NOT_FOUND));

        // 1. Validar e Atualizar Cliente
        Pessoa cliente = pessoaRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ExcecoesCustomizada("Cliente não encontrado", HttpStatus.NOT_FOUND));
        projeto.setCliente(cliente);

        // 2. Validar e Atualizar Vendedor
        if (dto.vendedorId() != null) {
            Usuario vendedor = usuarioRepository.findById(dto.vendedorId())
                    .orElseThrow(() -> new ExcecoesCustomizada("Vendedor não encontrado", HttpStatus.NOT_FOUND));
            projeto.setVendedor(vendedor);
        } else {
            projeto.setVendedor(null);
        }

        // 3. Projeto Origem
        if (dto.projetoOrigemId() != null) {
            Projeto origem = projetoRepository.findById(dto.projetoOrigemId())
                    .orElseThrow(() -> new ExcecoesCustomizada("Projeto origem não encontrado", HttpStatus.NOT_FOUND));
            projeto.setProjetoOrigem(origem);
        } else {
            projeto.setProjetoOrigem(null);
        }

        // 4. Campos Simples
        projeto.setTitulo(dto.titulo());
        projeto.setDescricao(dto.descricao());
        projeto.setRequisitos(dto.requisitos());
        projeto.setDataInicio(dto.dataInicio());
        projeto.setDataFimDesenv(dto.dataFimDesenv());
        projeto.setDataFimProjeto(dto.dataFimProjeto());
        projeto.setTipoProjeto(dto.tipoProjeto());
        projeto.setTipoVenda(dto.tipoVenda());
        projeto.setNomeIndicacao(dto.nomeIndicacao());
        projeto.setEmitirNf(dto.emitirNf() != null ? dto.emitirNf() : false);
        projeto.setValorContratoMensal(dto.valorContratoMensal());

        // 5. Recalcular Financeiro (Se necessário for implementado igual ao cadastro)
        // Por simplicidade, recalculamos sempre baseados no valorTotal novo ou antigo
        BigDecimal valorTotal = dto.valorTotal() != null ? dto.valorTotal() : BigDecimal.ZERO;
        projeto.setValorTotal(valorTotal);

        // Recalcular
        BigDecimal impostoPercentual = projeto.getImpostoPercentual(); // Mantem 15% ou atualiza? Geralmente fixo 15.
        
        Integer anoProjeto = dto.dataInicio() != null ? dto.dataInicio().getYear() : LocalDate.now().getYear();
        Meta metaLucro = metaRepository.findByAnoAndCategoriaAndTipoMeta(anoProjeto, CategoriaMeta.LUCRO, TipoMeta.META_NORMAL).orElse(null);
        BigDecimal lucroPercentual = metaLucro != null ? metaLucro.getValorAnual() : BigDecimal.ZERO;
        projeto.setLucroPercentual(lucroPercentual);

        BigDecimal valorImposto = valorTotal.multiply(impostoPercentual).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal valorLucro = valorTotal.multiply(lucroPercentual).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal valorDesenvolvimento = valorTotal.subtract(valorImposto).subtract(valorLucro);
        projeto.setValorDesenvolvimento(valorDesenvolvimento);


        // 6. Atualizar Listas (Strategy: Clear & Re-add)
        
        // Links
        projeto.getLinks().clear();
        if (dto.links() != null) {
            for (LinkProjetoRequestDTO linkDto : dto.links()) {
                LinkProjeto link = LinkProjeto.builder()
                        .projeto(projeto)
                        .url(linkDto.url())
                        .descricao(linkDto.descricao())
                        .tipoAmbiente(linkDto.tipoAmbiente())
                        .classificacao(linkDto.classificacao())
                        .observacao(linkDto.observacao())
                        .build();
                projeto.getLinks().add(link);
            }
        }

        // Equipe
        projeto.getEquipe().clear();
        if (dto.equipe() != null) {
            for (EquipeProjetoRequestDTO equipeDto : dto.equipe()) {
                Usuario colaborador = usuarioRepository.findById(equipeDto.colaboradorId())
                        .orElseThrow(() -> new ExcecoesCustomizada("Colaborador não encontrado", HttpStatus.NOT_FOUND));

                // Porcentagem definida individualmente
                BigDecimal porcentagemMembro = equipeDto.porcentagem() != null ? equipeDto.porcentagem() : BigDecimal.ZERO;
                BigDecimal percentualDecimal = porcentagemMembro.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                
                // Verba para este membro
                BigDecimal verbaParaMembro = valorDesenvolvimento.multiply(percentualDecimal);

                BigDecimal horasCalculadas = BigDecimal.ZERO;
                BigDecimal custoTotalMembro = BigDecimal.ZERO;

                if (equipeDto.usaSalarioFixo()) {
                    if (colaborador.getValorFixo() != null) {
                        custoTotalMembro = colaborador.getValorFixo();
                    }
                } else {
                    BigDecimal valorHora = colaborador.getValorHora();
                    if (valorHora != null && valorHora.compareTo(BigDecimal.ZERO) > 0) {
                        horasCalculadas = verbaParaMembro.divide(valorHora, 2, RoundingMode.HALF_UP);
                        custoTotalMembro = verbaParaMembro;
                    }
                }

                EquipeProjeto equipeEntity = EquipeProjeto.builder()
                        .projeto(projeto)
                        .colaborador(colaborador)
                        .funcao(equipeDto.funcao())
                        .usaSalarioFixo(equipeDto.usaSalarioFixo())
                        .porcentagem(porcentagemMembro)
                        .custoPrevisto(custoTotalMembro)
                        .horasPrevistas(horasCalculadas)
                        .build();

                projeto.getEquipe().add(equipeEntity);
            }
        }

        // Parcelas
        projeto.getParcelas().clear();
        if (dto.parcelas() != null && !dto.parcelas().isEmpty()) {
             for (ParcelaProjetoRequestDTO parcelaDto : dto.parcelas()) {
                ParcelaProjeto parcela = ParcelaProjeto.builder()
                        .projeto(projeto)
                        .numero(parcelaDto.numero())
                        .valor(parcelaDto.valor())
                        .dataVencimento(parcelaDto.dataVencimento())
                        .status(parcelaDto.status() != null ? parcelaDto.status() : StatusParcela.PENDENTE)
                        .build();
                projeto.getParcelas().add(parcela);
            }
        } else if (dto.tipoProjeto() == TipoProjeto.SOB_MEDIDA && dto.quantidadeParcelas() != null && dto.quantidadeParcelas() > 0) {
            BigDecimal valorParcelaCalc = valorTotal.divide(BigDecimal.valueOf(dto.quantidadeParcelas()), 2, RoundingMode.HALF_UP);
            BigDecimal somaParcelas = BigDecimal.ZERO;

            for (int i = 1; i <= dto.quantidadeParcelas(); i++) {
                BigDecimal valorAtual = valorParcelaCalc;
                if (i == dto.quantidadeParcelas()) {
                    valorAtual = valorTotal.subtract(somaParcelas);
                }
                somaParcelas = somaParcelas.add(valorAtual);

                ParcelaProjeto parcela = ParcelaProjeto.builder()
                        .projeto(projeto)
                        .numero(i)
                        .valor(valorAtual)
                        .dataVencimento(LocalDate.now().plusMonths(i))
                        .status(StatusParcela.PENDENTE)
                        .build();
                projeto.getParcelas().add(parcela);
            }
        }

        projetoRepository.save(projeto);
        return mapToDTO(projeto);
    }

    @Override
    @Transactional
    public void deletarProjeto(UUID id) {
        if (!projetoRepository.existsById(id)) {
            throw new ExcecoesCustomizada("Projeto não encontrado", HttpStatus.NOT_FOUND);
        }
        projetoRepository.deleteById(id);
    }

    // Mapper manual auxiliar
    private ProjetoResponseDTO mapToDTO(Projeto p) {
        return new ProjetoResponseDTO(
            p.getId(),
            p.getTitulo(),
            p.getDescricao(),
            p.getRequisitos(),
            p.getCliente().getId(),
            p.getCliente().getNome(),
            p.getVendedor() != null ? p.getVendedor().getId() : null,
            p.getVendedor() != null ? p.getVendedor().getNome() : null,
            p.getDataInicio(),
            p.getDataFimDesenv(),
            p.getDataFimProjeto(),
            p.getTipoProjeto(),
            p.getTipoVenda(),
            p.getProjetoOrigem() != null ? new ProjetoReferenciaDTO(p.getProjetoOrigem().getId(), p.getProjetoOrigem().getTitulo()) : null,
            p.getNomeIndicacao(),
            p.getValorTotal(),
            p.getValorContratoMensal(),
            p.getImpostoPercentual(),
            p.getLucroPercentual(),
            p.getValorDesenvolvimento(),
            p.getStatus(),
            p.getLinks().stream().map(this::mapLink).collect(Collectors.toList()),
            p.getParcelas().stream().map(this::mapParcela).collect(Collectors.toList()),
            p.getEquipe().stream().map(this::mapEquipe).collect(Collectors.toList()),
            p.getCreatedAt(),
            p.getUpdatedAt()
        );
    }

    private LinkProjetoResponseDTO mapLink(LinkProjeto l) {
        return new LinkProjetoResponseDTO(l.getId(), l.getUrl(), l.getDescricao(), l.getTipoAmbiente(), l.getClassificacao(), l.getObservacao());
    }

    private ParcelaProjetoResponseDTO mapParcela(ParcelaProjeto pa) {
        return new ParcelaProjetoResponseDTO(pa.getId(), pa.getNumero(), pa.getValor(), pa.getDataVencimento(), pa.getStatus());
    }

    private EquipeProjetoResponseDTO mapEquipe(EquipeProjeto e) {
        return new EquipeProjetoResponseDTO(
            e.getId(), 
            e.getColaborador().getId(), 
            e.getColaborador().getNome(), 
            e.getFuncao(), 
            e.getUsaSalarioFixo(), 
            e.getPorcentagem(),
            e.getHorasPrevistas(), 
            e.getCustoPrevisto()
        );
    }
}

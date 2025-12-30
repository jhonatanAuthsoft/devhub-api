package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.request.CadastrarMetaDTO;
import com.projeto.modelo.controller.dto.response.MetaResponseDTO;
import com.projeto.modelo.configuracao.exeption.ExcecoesCustomizada;
import com.projeto.modelo.mapper.MetaMapper;
import com.projeto.modelo.model.entity.Meta;
import com.projeto.modelo.model.enums.CategoriaMeta;
import com.projeto.modelo.model.enums.TipoMeta;
import com.projeto.modelo.repository.MetaRepository;
import com.projeto.modelo.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class MetaServiceImp implements MetaService {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private MetaMapper metaMapper;

    @Override
    @Transactional
    public MetaResponseDTO cadastrarMeta(CadastrarMetaDTO cadastrarMetaDTO) {
        // Verificar se já existe meta para o ano, categoria e tipo
        CategoriaMeta categoria = CategoriaMeta.valueOf(cadastrarMetaDTO.categoria());
        TipoMeta tipoMeta = TipoMeta.valueOf(cadastrarMetaDTO.tipoMeta());

        Optional<Meta> metaExistente = metaRepository.findByAnoAndCategoriaAndTipoMeta(
                cadastrarMetaDTO.ano(), categoria, tipoMeta);

        if (metaExistente.isPresent()) {
            throw new ExcecoesCustomizada(
                    "Já existe uma meta cadastrada para este ano, categoria e tipo de meta",
                    HttpStatus.CONFLICT);
        }

        Meta meta = new Meta();
        meta.setAno(cadastrarMetaDTO.ano());
        meta.setCategoria(categoria);
        meta.setTipoMeta(tipoMeta);
        meta.setValorAnual(cadastrarMetaDTO.valorAnual());
        meta.setJaneiro(cadastrarMetaDTO.janeiro());
        meta.setFevereiro(cadastrarMetaDTO.fevereiro());
        meta.setMarco(cadastrarMetaDTO.marco());
        meta.setAbril(cadastrarMetaDTO.abril());
        meta.setMaio(cadastrarMetaDTO.maio());
        meta.setJunho(cadastrarMetaDTO.junho());
        meta.setJulho(cadastrarMetaDTO.julho());
        meta.setAgosto(cadastrarMetaDTO.agosto());
        meta.setSetembro(cadastrarMetaDTO.setembro());
        meta.setOutubro(cadastrarMetaDTO.outubro());
        meta.setNovembro(cadastrarMetaDTO.novembro());
        meta.setDezembro(cadastrarMetaDTO.dezembro());

        Meta metaSalva = metaRepository.save(meta);
        return metaMapper.toResponseDTO(metaSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MetaResponseDTO> listarMetasPaginado(Pageable pageable) {
        Page<Meta> metas = metaRepository.findAll(pageable);
        return metas.map(metaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public MetaResponseDTO buscarMetaPorId(UUID id) {
        Meta meta = metaRepository.findById(id)
                .orElseThrow(() -> new ExcecoesCustomizada("Meta não encontrada", HttpStatus.NOT_FOUND));
        return metaMapper.toResponseDTO(meta);
    }

    @Override
    @Transactional
    public MetaResponseDTO atualizarMeta(UUID id, CadastrarMetaDTO cadastrarMetaDTO) {
        Meta meta = metaRepository.findById(id)
                .orElseThrow(() -> new ExcecoesCustomizada("Meta não encontrada", HttpStatus.NOT_FOUND));

        // Atualizar campos
        meta.setAno(cadastrarMetaDTO.ano());
        meta.setCategoria(CategoriaMeta.valueOf(cadastrarMetaDTO.categoria()));
        meta.setTipoMeta(TipoMeta.valueOf(cadastrarMetaDTO.tipoMeta()));
        meta.setValorAnual(cadastrarMetaDTO.valorAnual());
        meta.setJaneiro(cadastrarMetaDTO.janeiro());
        meta.setFevereiro(cadastrarMetaDTO.fevereiro());
        meta.setMarco(cadastrarMetaDTO.marco());
        meta.setAbril(cadastrarMetaDTO.abril());
        meta.setMaio(cadastrarMetaDTO.maio());
        meta.setJunho(cadastrarMetaDTO.junho());
        meta.setJulho(cadastrarMetaDTO.julho());
        meta.setAgosto(cadastrarMetaDTO.agosto());
        meta.setSetembro(cadastrarMetaDTO.setembro());
        meta.setOutubro(cadastrarMetaDTO.outubro());
        meta.setNovembro(cadastrarMetaDTO.novembro());
        meta.setDezembro(cadastrarMetaDTO.dezembro());

        Meta metaAtualizada = metaRepository.save(meta);
        return metaMapper.toResponseDTO(metaAtualizada);
    }
}

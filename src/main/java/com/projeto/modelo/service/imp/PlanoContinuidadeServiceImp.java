package com.projeto.modelo.service.imp;

import com.projeto.modelo.model.PlanoContinuidade;
import com.projeto.modelo.repository.PlanoContinuidadeRepository;
import com.projeto.modelo.service.PlanoContinuidadeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlanoContinuidadeServiceImp implements PlanoContinuidadeService {

    private final PlanoContinuidadeRepository repository;

    public PlanoContinuidadeServiceImp(PlanoContinuidadeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PlanoContinuidade> findAll() {
        return repository.findAll();
    }

    @Override
    public PlanoContinuidade findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Plano não encontrado"));
    }

    @Override
    public PlanoContinuidade create(PlanoContinuidade plano) {
        return repository.save(plano);
    }

    @Override
    public PlanoContinuidade update(UUID id, PlanoContinuidade plano) {
        PlanoContinuidade existing = findById(id);
        existing.setNome(plano.getNome());
        existing.setDescricaoDestino(plano.getDescricaoDestino());
        existing.setRecomendado(plano.getRecomendado());
        
        existing.setBeneficio1Titulo(plano.getBeneficio1Titulo());
        existing.setBeneficio1Descricao(plano.getBeneficio1Descricao());
        existing.setBeneficio2Titulo(plano.getBeneficio2Titulo());
        existing.setBeneficio2Descricao(plano.getBeneficio2Descricao());
        existing.setBeneficio3Titulo(plano.getBeneficio3Titulo());
        existing.setBeneficio3Descricao(plano.getBeneficio3Descricao());

        existing.setPrecoDoisAnos(plano.getPrecoDoisAnos());
        existing.setPrecoUmAno(plano.getPrecoUmAno());
        existing.setPrecoSemFidelidade(plano.getPrecoSemFidelidade());

        return repository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}

package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.request.CategoriaRequestDTO;
import com.projeto.modelo.controller.dto.response.CategoriaResponseDTO;
import com.projeto.modelo.model.entity.Categoria;
import com.projeto.modelo.repository.CategoriaRepository;
// import com.projeto.modelo.repository.ReceitaRepository;
import com.projeto.modelo.service.CategoriaService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImp implements CategoriaService {

    private final CategoriaRepository repository;
    // private final ReceitaRepository receitaRepository; // TODO uncomment when Receita is mapped to use it here for delete validation

    @PostConstruct
    @Transactional
    public void seedCategoriasPreConfiguradas() {
        if (repository.count() > 0) return;

        Categoria operacionais = criarPai("Receitas Operacionais");
        criarFilha("Alocação de servidor", operacionais);
        criarFilha("Sofware sob medidida", operacionais);
        criarFilha("Alocação", operacionais);
        criarFilha("Sociedade com outras empresas", operacionais);
        
        Categoria financeiras = criarPai("Receitas Financeiras");
        criarFilha("Juros recebidos", financeiras);
        criarFilha("Rendimentos de aplicação", financeiras);
        criarFilha("Descontos obtidos", financeiras);
        criarFilha("Emprestimo", financeiras);
        
        Categoria outras = criarPai("Outras Receitas");
        criarFilha("Aluguéis recebidos", outras);
        criarFilha("Reembolsos", outras);
        criarFilha("Receitas eventuais", outras);
        criarFilha("Investimento", outras);
        criarFilha("Agua", outras);
        criarFilha("Luz", outras);
    }

    private Categoria criarPai(String nome) {
        return repository.save(Categoria.builder().nome(nome).preConfigurada(true).ativo(true).build());
    }

    private void criarFilha(String nome, Categoria pai) {
        repository.save(Categoria.builder().nome(nome).pai(pai).preConfigurada(true).ativo(true).build());
    }

    @Override
    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        Categoria categoria = Categoria.builder()
                .nome(dto.getNome())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .preConfigurada(false)
                .build();
                
        if (dto.getPaiId() != null) {
            Categoria pai = repository.findById(dto.getPaiId())
                    .orElseThrow(() -> new RuntimeException("Categoria pai não encontrada"));
            categoria.setPai(pai);
        }
        
        return CategoriaResponseDTO.fromEntity(repository.save(categoria));
    }

    @Override
    public CategoriaResponseDTO buscarPorId(UUID id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return CategoriaResponseDTO.fromEntity(categoria);
    }

    @Override
    public List<CategoriaResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(CategoriaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoriaResponseDTO> listarRaizes() {
        return repository.findByPaiIsNull().stream()
                .map(CategoriaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoriaResponseDTO atualizar(UUID id, CategoriaRequestDTO dto) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
                
        categoria.setNome(dto.getNome());
        if (dto.getAtivo() != null) categoria.setAtivo(dto.getAtivo());
        
        if (dto.getPaiId() != null) {
            Categoria pai = repository.findById(dto.getPaiId())
                    .orElseThrow(() -> new RuntimeException("Categoria pai não encontrada"));
            categoria.setPai(pai);
        } else {
            categoria.setPai(null);
        }
        
        return CategoriaResponseDTO.fromEntity(repository.save(categoria));
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
                
        if (Boolean.TRUE.equals(categoria.getPreConfigurada())) {
            throw new RuntimeException("Categorias pré-configuradas não podem ser excluídas, apenas desativadas.");
        }
        
        // TODO: Validate if there are any Receitas linked
        // long count = receitaRepository.countByCategoriaId(id);
        // if (count > 0) throw new RuntimeException("A categoria possui receitas vinculadas e não pode ser excluída.");
        
        if (categoria.getFilhas() != null && !categoria.getFilhas().isEmpty()) {
            throw new RuntimeException("A categoria possui subcategorias e não pode ser excluída.");
        }

        repository.delete(categoria);
    }
}

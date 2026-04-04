package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.request.CategoriaRequestDTO;
import com.projeto.modelo.controller.dto.response.CategoriaResponseDTO;
import com.projeto.modelo.model.entity.Categoria;
import com.projeto.modelo.model.enums.TipoCategoria;
import com.projeto.modelo.repository.CategoriaRepository;
import com.projeto.modelo.service.CategoriaService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImp implements CategoriaService {

    private final CategoriaRepository repository;

    @PostConstruct
    @Transactional
    public void seedCategoriasPreConfiguradas() {
        if (repository.count() > 0) return;

        // Receitas
        Categoria operacionais = criarPai("Receitas Operacionais", TipoCategoria.RECEITA);
        criarFilha("Alocação de Servidor", operacionais, TipoCategoria.RECEITA);
        criarFilha("Software Sob Medida", operacionais, TipoCategoria.RECEITA);
        criarFilha("Alocação", operacionais, TipoCategoria.RECEITA);
        criarFilha("Sociedade com Outras Empresas", operacionais, TipoCategoria.RECEITA);
        
        Categoria financeiras = criarPai("Receitas Financeiras", TipoCategoria.RECEITA);
        criarFilha("Juros Recebidos", financeiras, TipoCategoria.RECEITA);
        criarFilha("Rendimentos de Aplicação", financeiras, TipoCategoria.RECEITA);
        criarFilha("Descontos Obtidos", financeiras, TipoCategoria.RECEITA);
        
        Categoria outrasReceitas = criarPai("Outras Receitas", TipoCategoria.RECEITA);
        criarFilha("Aportes e Investimentos", outrasReceitas, TipoCategoria.RECEITA);

        // Despesas
        Categoria despOperacionais = criarPai("Despesas Operacionais", TipoCategoria.DESPESA);
        criarFilha("Hospedagem em Nuvem", despOperacionais, TipoCategoria.DESPESA);
        criarFilha("Licenças de Software", despOperacionais, TipoCategoria.DESPESA);
        criarFilha("Salários e Encargos", despOperacionais, TipoCategoria.DESPESA);
        criarFilha("Marketing", despOperacionais, TipoCategoria.DESPESA);

        Categoria despAdministrativas = criarPai("Despesas Administrativas", TipoCategoria.DESPESA);
        criarFilha("Aluguel e Condomínio", despAdministrativas, TipoCategoria.DESPESA);
        criarFilha("Água", despAdministrativas, TipoCategoria.DESPESA);
        criarFilha("Luz", despAdministrativas, TipoCategoria.DESPESA);
        criarFilha("Internet", despAdministrativas, TipoCategoria.DESPESA);
        criarFilha("Materiais de Escritório", despAdministrativas, TipoCategoria.DESPESA);

        Categoria impostos = criarPai("Impostos e Taxas", TipoCategoria.DESPESA);
        criarFilha("Imposto de Software", impostos, TipoCategoria.DESPESA);
        criarFilha("Taxas Bancárias", impostos, TipoCategoria.DESPESA);
    }

    private Categoria criarPai(String nome, TipoCategoria tipo) {
        return repository.save(Categoria.builder().nome(nome).preConfigurada(true).ativo(true).tipo(tipo).build());
    }

    private void criarFilha(String nome, Categoria pai, TipoCategoria tipo) {
        repository.save(Categoria.builder().nome(nome).pai(pai).preConfigurada(true).ativo(true).tipo(tipo).build());
    }

    @Override
    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        Categoria categoria = Categoria.builder()
                .nome(dto.getNome())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .preConfigurada(false)
                .tipo(dto.getTipo() != null ? dto.getTipo() : TipoCategoria.AMBOS)
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
    public List<CategoriaResponseDTO> listarTodos(TipoCategoria tipo) {
        List<Categoria> categorias;
        if (tipo != null) {
            categorias = repository.findByTipoIn(Arrays.asList(tipo, TipoCategoria.AMBOS));
        } else {
            categorias = repository.findAll();
        }
        
        return categorias.stream()
                .map(CategoriaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoriaResponseDTO> listarRaizes(TipoCategoria tipo) {
        List<Categoria> categorias;
        if (tipo != null) {
            categorias = repository.findByPaiIsNullAndTipoIn(Arrays.asList(tipo, TipoCategoria.AMBOS));
        } else {
            categorias = repository.findByPaiIsNull();
        }

        return categorias.stream()
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
        if (dto.getTipo() != null) categoria.setTipo(dto.getTipo());
        
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
        
        if (categoria.getFilhas() != null && !categoria.getFilhas().isEmpty()) {
            throw new RuntimeException("A categoria possui subcategorias e não pode ser excluída.");
        }

        repository.delete(categoria);
    }
}

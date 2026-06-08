package com.projeto.modelo.service.imp;



import com.projeto.modelo.configuracao.exeption.ExcecoesCustomizada;
import com.projeto.modelo.util.StringUtils;
import com.projeto.modelo.controller.dto.request.CadastraUsuarioDTO;
import com.projeto.modelo.controller.dto.request.UsuarioEsqueceuSenhaRequestDTO;
import com.projeto.modelo.controller.dto.request.ValidaTrocaSenhaRequestDTO;
import com.projeto.modelo.controller.dto.response.AuthenticatedResposeDTO;
import com.projeto.modelo.controller.dto.response.UsuarioResposeDTO;
import com.projeto.modelo.mapper.UsuarioMapper;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.PermissaoStatus;
import com.projeto.modelo.model.enums.TipoContratacao;
import com.projeto.modelo.model.enums.UsuarioStatus;
import com.projeto.modelo.repository.EmailService;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.service.UsuarioService;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UsuarioServiceImp implements UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private EmailService emailService;



    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Transactional(readOnly = true)
    @Override
    public AuthenticatedResposeDTO retornoAutenticacao(String emailAuthsoft, String jwt){
        UsuarioResposeDTO responseDTO = this.usuarioMapper.toResponseDTO(this.buscarPorEmailAuthsoft(emailAuthsoft));

        return AuthenticatedResposeDTO.builder()
                .usuarioRespose(responseDTO)
                .token(jwt)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new ExcecoesCustomizada("Usuário não encontrado", HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    @Override
    public Usuario buscarPorEmailAuthsoft(String emailAuthsoft) {
        return usuarioRepository.findByEmailAuthsoft(emailAuthsoft)
                .orElseThrow(() -> new ExcecoesCustomizada("Usuário não encontrado com este e-mail Authsoft", HttpStatus.NOT_FOUND));
    }

    @Override
    public void validaTrocaSenha(ValidaTrocaSenhaRequestDTO validaTrocaSenhaRequestDTO){
        Optional<Usuario> usuarioOptional = this.usuarioRepository
                .findByEmailAndCodigoTrocaSenha(validaTrocaSenhaRequestDTO.email(), validaTrocaSenhaRequestDTO.codigo());

        if(!usuarioOptional.isPresent()){
            throw new ExcecoesCustomizada("Codigo invalido",HttpStatus.BAD_REQUEST);
        }
        Usuario usuario = usuarioOptional.get();
        usuario.setSenha(this.passwordEncoder.encode(validaTrocaSenhaRequestDTO.senhaNova()));
        usuario.setCodigoTrocaSenha(null);
        this.usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioResposeDTO cadastraUsuario(CadastraUsuarioDTO cadastraUsuarioDTO){

        Usuario usuario = new Usuario();

        if (StringUtils.isNullOrEmpty(cadastraUsuarioDTO.senha())) {
            throw new ExcecoesCustomizada("senha não pode ficar em branco", HttpStatus.BAD_REQUEST);
        }
        String senhaCriptografada = this.passwordEncoder.encode(cadastraUsuarioDTO.senha());

        usuario.setEmail(cadastraUsuarioDTO.email());
        usuario.setNome(cadastraUsuarioDTO.nome());
        usuario.setStatus(UsuarioStatus.ATIVO);
        usuario.setSenha(senhaCriptografada);
        
        // Mapear permissão do DTO (se fornecida, senão COLABORADOR como padrão)
        if (!StringUtils.isNullOrEmpty(cadastraUsuarioDTO.permissao())) {
            usuario.setPermissao(PermissaoStatus.valueOf(cadastraUsuarioDTO.permissao()));
        } else {
            usuario.setPermissao(PermissaoStatus.COLABORADOR);
        }

        if (!StringUtils.isNullOrEmpty(cadastraUsuarioDTO.tipoContratacao())) {
            usuario.setTipoContratacao(TipoContratacao.valueOf(cadastraUsuarioDTO.tipoContratacao()));
        } else {
            usuario.setTipoContratacao(TipoContratacao.FREELANCER);
        }

        usuario.setCargo(cadastraUsuarioDTO.cargo());
        usuario.setTelefone(cadastraUsuarioDTO.telefone());
        usuario.setChavePix(cadastraUsuarioDTO.chavePix());
        usuario.setCep(cadastraUsuarioDTO.cep());
        usuario.setLogradouro(cadastraUsuarioDTO.logradouro());
        usuario.setBairro(cadastraUsuarioDTO.bairro());
        usuario.setNumero(cadastraUsuarioDTO.numero());
        usuario.setComplemento(cadastraUsuarioDTO.complemento());
        usuario.setCidade(cadastraUsuarioDTO.cidade());
        usuario.setEstado(cadastraUsuarioDTO.estado());
        usuario.setPais(cadastraUsuarioDTO.pais());
        usuario.setValorFixo(cadastraUsuarioDTO.valorFixo());
        usuario.setValorHora(cadastraUsuarioDTO.valorHora());
        usuario.setRazaoSocial(cadastraUsuarioDTO.razaoSocial());
        usuario.setCnpj(cadastraUsuarioDTO.cnpj());
        usuario.setCpf(cadastraUsuarioDTO.cpf());
        usuario.setEmailAuthsoft(cadastraUsuarioDTO.emailAuthsoft());
        
        Usuario usuarioSalvo = this.usuarioRepository.save(usuario);
        UsuarioResposeDTO responseDTO = this.usuarioMapper.toResponseDTO(usuarioSalvo);
        return responseDTO;
    }

    public String gerarSenha() {
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            senha.append(random.nextInt(10)); // Gera um número entre 0 e 9
        }

        return senha.toString();
    }

    @Override
    public void esqueceuSenha(UsuarioEsqueceuSenhaRequestDTO usuarioEsqueceuSenhaRequestDTO) {
        try {
            Optional<Usuario> usuarioOptional = this.usuarioRepository.findByEmailAndStatus(usuarioEsqueceuSenhaRequestDTO.email(), UsuarioStatus.ATIVO);
            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                Random random = new Random();
                int codigo = 1000 + random.nextInt(9000);
                usuario.setCodigoTrocaSenha(codigo);
                this.usuarioRepository.save(usuario);
                this.emailService.enviarEmailEsqueceuSenha(usuarioEsqueceuSenhaRequestDTO.email());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UsuarioResposeDTO> listarUsuariosPaginado(String search, UsuarioStatus status, Pageable pageable) {
        Page<Usuario> usuarios;
        if ((search != null && !search.trim().isEmpty()) || status != null) {
            String searchTerm = (search != null && !search.trim().isEmpty()) ? search : "";
            usuarios = this.usuarioRepository.buscarPorTermoEStatus(searchTerm, status, pageable);
        } else {
            usuarios = this.usuarioRepository.findAll(pageable);
        }
        return usuarios.map(this.usuarioMapper::toResponseDTO);
    }

    @Override
    public UsuarioResposeDTO atualizarUsuario(java.util.UUID id, CadastraUsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ExcecoesCustomizada("Usuário não encontrado", HttpStatus.NOT_FOUND));

        if (!StringUtils.isNullOrEmpty(usuarioDTO.nome())) usuario.setNome(usuarioDTO.nome());
        if (!StringUtils.isNullOrEmpty(usuarioDTO.email()) && !usuario.getEmail().equals(usuarioDTO.email())) {
            if (usuarioRepository.findByEmail(usuarioDTO.email()).isPresent()) {
                throw new ExcecoesCustomizada("E-mail já cadastrado", HttpStatus.BAD_REQUEST);
            }
            usuario.setEmail(usuarioDTO.email());
        }
        if (!StringUtils.isNullOrEmpty(usuarioDTO.senha())) {
             usuario.setSenha(this.passwordEncoder.encode(usuarioDTO.senha()));
        }
        
        if (usuarioDTO.cargo() != null) usuario.setCargo(usuarioDTO.cargo());
        if (usuarioDTO.telefone() != null) usuario.setTelefone(usuarioDTO.telefone());
        if (!StringUtils.isNullOrEmpty(usuarioDTO.permissao())) {
            usuario.setPermissao(PermissaoStatus.valueOf(usuarioDTO.permissao()));
        }

        if (!StringUtils.isNullOrEmpty(usuarioDTO.tipoContratacao())) {
            boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                usuario.setTipoContratacao(TipoContratacao.valueOf(usuarioDTO.tipoContratacao()));
            }
        }

        if (!StringUtils.isNullOrEmpty(usuarioDTO.status())) {
            usuario.setStatus(UsuarioStatus.valueOf(usuarioDTO.status()));
        }
        if (usuarioDTO.chavePix() != null) usuario.setChavePix(usuarioDTO.chavePix());
        if (usuarioDTO.cep() != null) usuario.setCep(usuarioDTO.cep());
        if (usuarioDTO.logradouro() != null) usuario.setLogradouro(usuarioDTO.logradouro());
        if (usuarioDTO.bairro() != null) usuario.setBairro(usuarioDTO.bairro());
        if (usuarioDTO.numero() != null) usuario.setNumero(usuarioDTO.numero());
        if (usuarioDTO.complemento() != null) usuario.setComplemento(usuarioDTO.complemento());
        if (usuarioDTO.cidade() != null) usuario.setCidade(usuarioDTO.cidade());
        if (usuarioDTO.estado() != null) usuario.setEstado(usuarioDTO.estado());
        if (usuarioDTO.pais() != null) usuario.setPais(usuarioDTO.pais());
        if (usuarioDTO.valorFixo() != null) usuario.setValorFixo(usuarioDTO.valorFixo());
        if (usuarioDTO.valorHora() != null) usuario.setValorHora(usuarioDTO.valorHora());
        if (usuarioDTO.razaoSocial() != null) usuario.setRazaoSocial(usuarioDTO.razaoSocial());
        if (usuarioDTO.cnpj() != null) usuario.setCnpj(usuarioDTO.cnpj());
        if (usuarioDTO.cpf() != null) usuario.setCpf(usuarioDTO.cpf());
        if (usuarioDTO.emailAuthsoft() != null) usuario.setEmailAuthsoft(usuarioDTO.emailAuthsoft());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioAtualizado);
    }
}

package com.projeto.modelo.service.imp;


import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.repository.PessoaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;

    public UserDetailsServiceImp(UsuarioRepository usuarioRepository, PessoaRepository pessoaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = this.usuarioRepository.findByEmailAuthsoft(username.toLowerCase());
        if (usuario.isPresent()) {
            return usuario.get();
        }

        Optional<Pessoa> pessoa = this.pessoaRepository.findByEmail(username.toLowerCase());
        if (pessoa.isPresent()) {
            return pessoa.get();
        }

        throw new UsernameNotFoundException("Usuário ou Pessoa não encontrado com o e-mail: " + username);
    }
}

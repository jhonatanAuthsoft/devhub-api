package com.projeto.modelo.service.imp;


import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImp(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.usuarioRepository.findByEmailAuthsoft(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail Authsoft: " + username));
    }
}

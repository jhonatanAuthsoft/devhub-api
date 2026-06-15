package com.projeto.modelo.service;

import com.projeto.modelo.model.entity.Pessoa;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.repository.PessoaRepository;
import com.projeto.modelo.repository.UsuarioRepository;
import com.projeto.modelo.service.imp.UserDetailsServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImpTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private UserDetailsServiceImp userDetailsServiceImp;

    @Test
    void loadUserByUsername_WithExistingUsuarioEmail_ReturnsUsuario() {
        String email = "usuario@test.com";
        Usuario usuario = new Usuario();
        usuario.setEmailAuthsoft(email);

        when(usuarioRepository.findByEmailAuthsoft(email)).thenReturn(Optional.of(usuario));

        UserDetails result = userDetailsServiceImp.loadUserByUsername(email);

        assertNotNull(result);
        assertTrue(result instanceof Usuario);
        assertEquals(email, ((Usuario) result).getEmailAuthsoft());
        verify(usuarioRepository).findByEmailAuthsoft(email);
        verifyNoInteractions(pessoaRepository);
    }

    @Test
    void loadUserByUsername_WithExistingPessoaEmail_ReturnsPessoa() {
        String email = "pessoa@test.com";
        Pessoa pessoa = new Pessoa();
        pessoa.setEmail(email);

        when(usuarioRepository.findByEmailAuthsoft(email)).thenReturn(Optional.empty());
        when(pessoaRepository.findByEmail(email)).thenReturn(Optional.of(pessoa));

        UserDetails result = userDetailsServiceImp.loadUserByUsername(email);

        assertNotNull(result);
        assertTrue(result instanceof Pessoa);
        assertEquals(email, ((Pessoa) result).getEmail());
        verify(usuarioRepository).findByEmailAuthsoft(email);
        verify(pessoaRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_WithNonExistingEmail_ThrowsUsernameNotFoundException() {
        String email = "inexistente@test.com";

        when(usuarioRepository.findByEmailAuthsoft(email)).thenReturn(Optional.empty());
        when(pessoaRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImp.loadUserByUsername(email));
        
        verify(usuarioRepository).findByEmailAuthsoft(email);
        verify(pessoaRepository).findByEmail(email);
    }
}

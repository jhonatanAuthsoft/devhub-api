package com.projeto.modelo.model.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PessoaUserDetailsTest {

    @Test
    void testGetAuthorities_ReturnsRoleCliente() {
        Pessoa pessoa = new Pessoa();
        Collection<? extends GrantedAuthority> authorities = pessoa.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_CLIENTE", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetUsername_ReturnsEmail() {
        Pessoa pessoa = new Pessoa();
        pessoa.setEmail("cliente@test.com");

        assertEquals("cliente@test.com", pessoa.getUsername());
    }
}

package com.projeto.modelo.controller;

import com.projeto.modelo.configuracao.JwtUtil;
import com.projeto.modelo.controller.dto.request.AuthenticationRequest;
import com.projeto.modelo.controller.dto.response.AuthenticatedResposeDTO;
import com.projeto.modelo.service.UsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UsuarioService usuarioService;

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    public AuthenticationController(AuthenticationManager authenticationManager, UsuarioService usuarioService, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/authenticate")
    public AuthenticatedResposeDTO createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername().toLowerCase(), authenticationRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername().toLowerCase());
        final String jwt = jwtUtil.generateToken(userDetails);
        return usuarioService.retornoAutenticacao(authenticationRequest.getUsername().toLowerCase(), jwt);
    }
}
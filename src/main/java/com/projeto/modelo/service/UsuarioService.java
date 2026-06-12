package com.projeto.modelo.service;

import com.projeto.modelo.controller.dto.request.CadastraUsuarioDTO;
import com.projeto.modelo.controller.dto.request.UsuarioEsqueceuSenhaRequestDTO;
import com.projeto.modelo.controller.dto.request.ValidaTrocaSenhaRequestDTO;
import com.projeto.modelo.controller.dto.response.AuthenticatedResposeDTO;
import com.projeto.modelo.controller.dto.response.UsuarioResposeDTO;
import com.projeto.modelo.model.entity.Usuario;
import com.projeto.modelo.model.enums.UsuarioStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    @Transactional(readOnly = true)
    AuthenticatedResposeDTO retornoAutenticacao(String email, String jwt);

    @Transactional(readOnly = true)
    Usuario buscarPorEmail(String email);

    @Transactional(readOnly = true)
    Usuario buscarPorEmailAuthsoft(String emailAuthsoft);

    void validaTrocaSenha(ValidaTrocaSenhaRequestDTO validaTrocaSenhaRequestDTO);

    UsuarioResposeDTO cadastraUsuario(CadastraUsuarioDTO cadastraUsuarioDTO);
    UsuarioResposeDTO atualizarUsuario(java.util.UUID id, CadastraUsuarioDTO usuarioDTO);

    void esqueceuSenha(UsuarioEsqueceuSenhaRequestDTO usuarioEsqueceuSenhaRequestDTO);

    @Transactional(readOnly = true)
    Page<UsuarioResposeDTO> listarUsuariosPaginado(String search, UsuarioStatus status, Pageable pageable);
}

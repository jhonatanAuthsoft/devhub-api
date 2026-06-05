package com.projeto.modelo.mapper;



import com.projeto.modelo.controller.dto.response.UsuarioResposeDTO;
import com.projeto.modelo.model.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {



    public UsuarioResposeDTO toResponseDTO(Usuario usuario) {
        return UsuarioResposeDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .cargo(usuario.getCargo())
                .telefone(usuario.getTelefone())
                .permissao(usuario.getPermissao() != null ? usuario.getPermissao().toString() : null)
                .tipoContratacao(usuario.getTipoContratacao() != null ? usuario.getTipoContratacao().toString() : "FREELANCER")
                .status(usuario.getStatus() != null ? usuario.getStatus().toString() : null)
                .chavePix(usuario.getChavePix())
                .cep(usuario.getCep())
                .logradouro(usuario.getLogradouro())
                .bairro(usuario.getBairro())
                .numero(usuario.getNumero())
                .complemento(usuario.getComplemento())
                .cidade(usuario.getCidade())
                .estado(usuario.getEstado())
                .pais(usuario.getPais())
                .valorFixo(usuario.getValorFixo())
                .valorHora(usuario.getValorHora())
                .razaoSocial(usuario.getRazaoSocial())
                .cnpj(usuario.getCnpj())
                .cpf(usuario.getCpf())
                .emailAuthsoft(usuario.getEmailAuthsoft())
                .build();
    }

}

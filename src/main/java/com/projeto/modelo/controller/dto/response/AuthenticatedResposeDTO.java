package com.projeto.modelo.controller.dto.response;

import lombok.Builder;

@Builder
public record AuthenticatedResposeDTO(UsuarioResposeDTO usuarioRespose, String token) {

    public static AuthenticatedResposeDTOBuilder builder() {
        return new AuthenticatedResposeDTOBuilder();
    }

    public static class AuthenticatedResposeDTOBuilder {
        private UsuarioResposeDTO usuarioRespose;
        private String token;

        public AuthenticatedResposeDTOBuilder usuarioRespose(UsuarioResposeDTO usuarioRespose) {
            this.usuarioRespose = usuarioRespose;
            return this;
        }

        public AuthenticatedResposeDTOBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticatedResposeDTO build() {
            return new AuthenticatedResposeDTO(usuarioRespose, token);
        }
    }
}

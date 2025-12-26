package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.model.enums.TipoPessoaVinculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequestDTO {
    private UUID clienteId;
    private TipoPessoaVinculo tipoPessoa;
    private String nome;
    private String email;
    private String telefone;
    
    // Campos específicos para CONTATO
    private String cargo;
    private Boolean recebeBoleto;
    private Boolean recebeNf;
}

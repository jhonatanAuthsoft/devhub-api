package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {
    private TipoPessoa tipoPessoa;
    private String cpfCnpj;
    private String nome;
    private String emailPrincipal;
    private String telefone;
    private ClienteStatus status;
    private String observacao;
    
    // Campos de Endereço
    private String logradouro;
    private String cidade;
    private String estado;
    private String cep;
    
    // Responsáveis (apenas para CNPJ)
    @Builder.Default
    private List<PessoaRequestDTO> responsaveis = new ArrayList<>();
}

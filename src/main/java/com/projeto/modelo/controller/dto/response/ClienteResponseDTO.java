package com.projeto.modelo.controller.dto.response;

import com.projeto.modelo.model.enums.ClienteStatus;
import com.projeto.modelo.model.enums.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private UUID id;
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
    
    // Pessoas vinculadas
    @Builder.Default
    private List<PessoaResponseDTO> pessoas = new ArrayList<>();
    
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}

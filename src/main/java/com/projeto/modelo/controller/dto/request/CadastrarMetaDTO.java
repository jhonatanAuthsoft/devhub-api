package com.projeto.modelo.controller.dto.request;

import com.projeto.modelo.configuracao.exeption.ExcecoesCustomizada;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public record CadastrarMetaDTO(
        Integer ano,
        String categoria,
        String tipoMeta,
        BigDecimal valorAnual,
        BigDecimal janeiro,
        BigDecimal fevereiro,
        BigDecimal marco,
        BigDecimal abril,
        BigDecimal maio,
        BigDecimal junho,
        BigDecimal julho,
        BigDecimal agosto,
        BigDecimal setembro,
        BigDecimal outubro,
        BigDecimal novembro,
        BigDecimal dezembro
) {
    public CadastrarMetaDTO {
        if (ano == null || ano < 2000) {
            throw new ExcecoesCustomizada("Ano inválido", HttpStatus.BAD_REQUEST);
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            throw new ExcecoesCustomizada("Categoria não pode ficar em branco", HttpStatus.BAD_REQUEST);
        }

        if (tipoMeta == null || tipoMeta.trim().isEmpty()) {
            throw new ExcecoesCustomizada("Tipo de meta não pode ficar em branco", HttpStatus.BAD_REQUEST);
        }

        if (valorAnual == null || valorAnual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ExcecoesCustomizada("Valor anual deve ser maior que zero", HttpStatus.BAD_REQUEST);
        }

        // Validar que a soma dos meses é igual ao valor anual
        BigDecimal totalMensal = BigDecimal.ZERO;
        if (janeiro != null) totalMensal = totalMensal.add(janeiro);
        if (fevereiro != null) totalMensal = totalMensal.add(fevereiro);
        if (marco != null) totalMensal = totalMensal.add(marco);
        if (abril != null) totalMensal = totalMensal.add(abril);
        if (maio != null) totalMensal = totalMensal.add(maio);
        if (junho != null) totalMensal = totalMensal.add(junho);
        if (julho != null) totalMensal = totalMensal.add(julho);
        if (agosto != null) totalMensal = totalMensal.add(agosto);
        if (setembro != null) totalMensal = totalMensal.add(setembro);
        if (outubro != null) totalMensal = totalMensal.add(outubro);
        if (novembro != null) totalMensal = totalMensal.add(novembro);
        if (dezembro != null) totalMensal = totalMensal.add(dezembro);

        if (totalMensal.compareTo(BigDecimal.ZERO) > 0 && totalMensal.compareTo(valorAnual) != 0) {
            throw new ExcecoesCustomizada("A soma das metas mensais deve ser igual ao valor anual", HttpStatus.BAD_REQUEST);
        }
    }
}

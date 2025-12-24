package com.projeto.modelo.util;

public final class StringUtils {

    /**
     * Construtor privado para prevenir a instanciação de uma classe utilitária.
     */
    private StringUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /**
     * Verifica se uma string é nula ou vazia (após remover espaços em branco).
     * @param str A string a ser verificada.
     * @return true se a string for null ou vazia, false caso contrário.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Constantes para "números mágicos" melhoram a legibilidade
    private static final int CPF_LENGTH = 11;
    private static final int CNPJ_LENGTH = 14;

    /**
     * Valida se a string fornecida é um CPF ou um CNPJ válido.
     * @param documento O documento (CPF ou CNPJ) com ou sem formatação.
     * @return true se for um CPF ou CNPJ válido, false caso contrário.
     */
    public static boolean cpfOuCnpjValido(String documento) {
        if (isNullOrEmpty(documento)) {
            return false;
        }

        String docLimpo = removerCaracteresEspeciais(documento);

        if (docLimpo.length() == CPF_LENGTH) {
            return isCpfValido(docLimpo);
        }

        if (docLimpo.length() == CNPJ_LENGTH) {
            return isCnpjValido(docLimpo);
        }

        return false; // Tamanho inválido
    }

    /**
     * Valida um CPF.
     * @param cpf O CPF com exatamente 11 dígitos.
     * @return true se o CPF for válido.
     */
    private static boolean isCpfValido(String cpf) {
        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpf.matches("(\\d)\\1{" + (CPF_LENGTH - 1) + "}")) {
            return false;
        }

        try {
            int soma1 = 0;
            for (int i = 0; i < 9; i++) {
                soma1 += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int digito1 = 11 - (soma1 % 11);
            if (digito1 >= 10) digito1 = 0;

            if (Character.getNumericValue(cpf.charAt(9)) != digito1) {
                return false;
            }

            int soma2 = 0;
            for (int i = 0; i < 10; i++) {
                soma2 += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int digito2 = 11 - (soma2 % 11);
            if (digito2 >= 10) digito2 = 0;

            return Character.getNumericValue(cpf.charAt(10)) == digito2;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida um CNPJ.
     * @param cnpj O CNPJ com exatamente 14 dígitos.
     * @return true se o CNPJ for válido.
     */
    private static boolean isCnpjValido(String cnpj) {
        // Verifica se todos os dígitos são iguais (ex: 11.111.111/1111-11)
        if (cnpj.matches("(\\d)\\1{" + (CNPJ_LENGTH - 1) + "}")) {
            return false;
        }

        try {
            int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int soma1 = 0;
            for (int i = 0; i < 12; i++) {
                soma1 += Character.getNumericValue(cnpj.charAt(i)) * pesos1[i];
            }
            int digito1 = 11 - (soma1 % 11);
            if (digito1 >= 10) digito1 = 0;

            if (Character.getNumericValue(cnpj.charAt(12)) != digito1) {
                return false;
            }

            int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int soma2 = 0;
            for (int i = 0; i < 13; i++) {
                soma2 += Character.getNumericValue(cnpj.charAt(i)) * pesos2[i];
            }
            int digito2 = 11 - (soma2 % 11);
            if (digito2 >= 10) digito2 = 0;

            return Character.getNumericValue(cnpj.charAt(13)) == digito2;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String removerCaracteresEspeciais(String texto) {
        if (texto == null) return null;
        // Remove tudo que não for dígito
        return texto.replaceAll("[^\\d]", "");
    }

    public static boolean cepValido(String cep) {
        if (isNullOrEmpty(cep)) {
            return false;
        }
        String cepLimpo = removerCaracteresEspeciais(cep);
        return cepLimpo.matches("\\d{8}");
    }
}

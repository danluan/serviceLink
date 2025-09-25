package br.com.servicelink.entity;

import java.util.Arrays;

public enum Intencao {
    SERVICO_DOMESTICO,
    ORCAMENTO,
    DUVIDA_COMUM,
    ELOGIO,
    RECLAMACAO,
    AGRADECIMENTO,
    SAUDACAO,
    GERAL; // Categoria para o que não se encaixar em nenhum dos outros

    public static Intencao fromString(String texto){
        if (texto == null) {
            return GERAL;
        }

        String textoNormalizado = texto.toUpperCase();

        return Arrays.stream(values())
                .filter(intencao -> intencao.name().equals(textoNormalizado))
                .findFirst()
                .orElse(GERAL);
    }
}

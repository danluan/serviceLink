package br.com.serviceframework.framework.domain.enumerations;

import java.util.Arrays;

public enum ServicosCategorias {
    LIMPEZA,
    HIDRAULICA,
    ELETRICA,
    PINTURA,
    JARDINAGEM,
    COZINHA,
    OUTRAS;

    public static ServicosCategorias fromString(String texto){
        if (texto == null) {
            return OUTRAS;
        }

        String textoNormalizado = texto.toUpperCase();

        return Arrays.stream(values())
                .filter(servico -> servico.name().equals(textoNormalizado))
                .findFirst()
                .orElse(OUTRAS);
    }
}

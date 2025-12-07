package br.com.servicelink.enumerations;

import br.com.serviceframework.domain.interfaces.ICategoriaServicos;

import java.util.Arrays;

public enum CategoriaDomesticas implements ICategoriaServicos {
    LIMPEZA(1L, "Limpeza"),
    HIDRAULICA(2L, "Hidráulica"),
    ELETRICA(3L, "Elétrica"),
    PINTURA(4L, "Pintura"),
    JARDINAGEM(5L, "Jardinagem"),
    COZINHA(6L, "Cozinha"),

    OUTRAS(999L, "Outras");

    private final Long id;
    private final String nomeCategoria;

    CategoriaDomesticas(Long id, String nomeCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
    }
    public static CategoriaDomesticas fromString(String texto){
        if (texto == null) {
            return OUTRAS;
        }

        String textoNormalizado = texto.toUpperCase();

        return Arrays.stream(values())
                .filter(servico -> servico.name().equals(textoNormalizado))
                .findFirst()
                .orElse(OUTRAS);
    }


    @Override
    public String getNomeCategoria() {
        return nomeCategoria;
    }

    @Override
    public Long getIdCategoria() {
        return id;
    }

    public static CategoriaDomesticas ofId(Long id) {
        if (id == null) {
            return OUTRAS;
        }
        return Arrays.stream(values())
                .filter(servico -> servico.getIdCategoria().equals(id))
                .findFirst()
                .orElse(OUTRAS);
    }
}

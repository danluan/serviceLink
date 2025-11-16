package br.com.serviceframework.DTO;

import java.math.BigDecimal;

public record ServicoDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoBase,
        String categoria,
        String imagemUrl
) {
}


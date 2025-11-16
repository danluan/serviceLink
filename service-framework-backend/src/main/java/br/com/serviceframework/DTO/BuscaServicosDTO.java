package br.com.serviceframework.DTO;

import java.math.BigDecimal;

public record BuscaServicosDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoMin,
        BigDecimal precoMax,
        String categoria
){

}

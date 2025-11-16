package br.com.serviceframework.DTO;

import java.time.LocalDateTime;

public record AgendamentoDTO(
        Long id,
        LocalDateTime dataHora,
        String observacao,
        String status,
        Long clienteId,
        Long servicoId,
        AvaliacaoDTO avaliacaoDTO
) {
}

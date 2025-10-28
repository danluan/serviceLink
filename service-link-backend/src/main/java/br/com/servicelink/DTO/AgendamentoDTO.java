package br.com.servicelink.DTO;

import java.time.LocalDateTime;

public record AgendamentoDTO(
        LocalDateTime dataHora,
        String observacao,
        Long clienteId,
        Long servicoId
) {
}

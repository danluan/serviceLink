package br.com.serviceframework.domain.DTO;

import jakarta.validation.constraints.*;
import org.antlr.v4.runtime.misc.NotNull;

public record AvaliacaoDTO(
        Long id,

        @NotNull(message = "Avalie o serviço.")
        @Min(value = 1, message = "A nota mínima é 1 estrela.")
        @Max(value = 5, message = "A nota máxima é 5 estrelas.")
        Integer estrelas,

        @Size(max = 500)
        String comentario
) {
}

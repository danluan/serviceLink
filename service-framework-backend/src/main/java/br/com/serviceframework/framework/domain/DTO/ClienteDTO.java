package br.com.serviceframework.framework.domain.DTO;

public record ClienteDTO(
        Long id,
        Long userId,
        String nome,
        String email,
        String telefone,
        String cpf
) {
}

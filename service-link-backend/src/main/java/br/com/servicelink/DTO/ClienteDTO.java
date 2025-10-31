package br.com.servicelink.DTO;

public record ClienteDTO(
        Long id,
        Long userId,
        String nome,
        String email,
        String telefone,
        String cpf
) {
}

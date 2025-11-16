package br.com.serviceframework.DTO;

public record PrestadorDTO(
        Long id,
        Boolean active,
        Long userId,
        String nome,
        String email,
        String telefone,
        String cpfCnpj,
        String biografia
) {
}

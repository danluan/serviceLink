package br.com.serviceframework.framework.domain.DTO;

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

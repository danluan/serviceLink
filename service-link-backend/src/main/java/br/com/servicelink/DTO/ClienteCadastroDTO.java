package br.com.servicelink.DTO;

public record ClienteCadastroDTO(
        String nome,
        String email,
        String senha,
        String telefone,
        String cpf,
        String endereco
) {
}

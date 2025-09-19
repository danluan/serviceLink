package br.com.servicelink.DTO;

import br.com.servicelink.enumerations.Perfis;

public record RegisterDTO(String email, String senha, Perfis perfil) {
}

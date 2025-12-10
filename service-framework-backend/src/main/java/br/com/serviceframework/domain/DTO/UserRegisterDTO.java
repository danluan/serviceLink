package br.com.serviceframework.domain.DTO;

import br.com.serviceframework.domain.entity.PerfilUsuario;
import br.com.serviceframework.domain.entity.User;
import br.com.serviceframework.domain.enumerations.Perfis;

public class UserRegisterDTO {
    private String email;
    private String senha;
    private PerfilUsuario perfil;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String email, String senha, PerfilUsuario perfil) {
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public User toEntity() {
        return new User(this.email, this.senha, this.perfil);
    }
}

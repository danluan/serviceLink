package br.com.serviceframework.DTO;

import br.com.serviceframework.entity.User;
import br.com.serviceframework.enumerations.Perfis;

public class UserRegisterDTO {
    private String nome;
    private String telefone;
    private String cpfCnpj;
    private String email;
    private String senha;
    private Perfis perfil;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String nome, String telefone, String cpfCnpj, String email, String senha, Perfis perfil) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
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

    public Perfis getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfis perfil) {
        this.perfil = perfil;
    }

    public User toEntity() {
        return new User(this.email, this.senha, this.nome, this.telefone, this.cpfCnpj, this.perfil);
    }
}

package br.com.servicelink.DTO;

import br.com.servicelink.entity.Prestador;

public class PrestadorCadastroDTO {

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String descricao;
    private String cpfCnpj;

    public PrestadorCadastroDTO() {}

    public PrestadorCadastroDTO(String nome, String senha, String telefone, String email, String descricao, String cpfCnpj) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.descricao = descricao;
        this.cpfCnpj = cpfCnpj;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

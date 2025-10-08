package br.com.servicelink.DTO;

import br.com.servicelink.entity.User;

public class UserDTO {
    private Long id;
    private String nome;
    private String telefone;
    private String cpfCnpj;
    private String email;
    private String perfil;

    public UserDTO() {
    }

    public UserDTO(Long id, String nome, String telefone, String cpfCnpj, String email, String perfil) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.perfil = perfil;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nome = user.getNome();
        this.telefone = user.getTelefone();
        this.cpfCnpj = user.getCpfCnpj();
        this.perfil = user.getPerfil().name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
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
}

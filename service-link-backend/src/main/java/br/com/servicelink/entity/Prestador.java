package br.com.servicelink.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Prestador {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private PerfilPrestador perfilPrestador;

    @OneToMany(mappedBy = "prestador", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<Servico> servicos;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PerfilPrestador getPerfilPrestador() {
        return perfilPrestador;
    }

    public void setPerfilPrestador(PerfilPrestador perfilPrestador) {
        this.perfilPrestador = perfilPrestador;
    }
}

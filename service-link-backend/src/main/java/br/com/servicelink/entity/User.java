package br.com.servicelink.entity;

import br.com.servicelink.enumerations.Perfis;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean active;

    @Column(unique = true)
    private String email;

    @Column
    private String senha;

    @Column
    private String nome;

    @Column
    private String telefone;

    @Column
    private String descricao;

    @Column(unique = true)
    private String cpfCnpj;

    @Column
    private Perfis perfil;

    public User(String email, String senha, Perfis perfil) {
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.active = true;
    }

    public User() {

    }

    // métodos UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        switch (this.perfil) {
            case ADMIN -> authorities.add(new SimpleGrantedAuthority("ADMIN"));
            case CLIENTE -> authorities.add(new SimpleGrantedAuthority("CLIENTE"));
            case PRESTADOR -> authorities.add(new SimpleGrantedAuthority("PRESTADOR"));
            default -> throw new IllegalStateException("Unexpected value: " + this.perfil); //TODO Criação de Exception personalizada
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}

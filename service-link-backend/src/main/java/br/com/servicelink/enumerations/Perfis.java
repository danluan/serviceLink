package br.com.servicelink.enumerations;

public enum Perfis {
    ADMIN("admin"),
    CLIENTE("cliente"),
    PRESTADOR("prestador");

    private String perfil;

    Perfis(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }
}

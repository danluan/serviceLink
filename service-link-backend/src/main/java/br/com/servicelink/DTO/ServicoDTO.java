package br.com.servicelink.DTO;

import br.com.servicelink.entity.Servico;

import java.math.BigDecimal;

public class ServicoDTO {
    private String nome;
    private String descricao;
    private BigDecimal precoBase;
    private String categoria;
    private String imagemUrl;

    public ServicoDTO() {}

    public ServicoDTO(String nome, String descricao, BigDecimal precoBase, String categoria, String imagemUrl) {
        this.nome = nome;
        this.descricao = descricao;
        this.precoBase = precoBase;
        this.categoria = categoria;
        this.imagemUrl = imagemUrl;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}


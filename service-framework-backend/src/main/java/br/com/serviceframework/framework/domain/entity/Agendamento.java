package br.com.serviceframework.framework.domain.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import br.com.serviceframework.framework.domain.interfaces.AgendamentoStatus;

@MappedSuperclass
public abstract class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @Column(name = "status")
    private Integer statusCode;

    @Column
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @OneToOne(fetch = FetchType.LAZY)
    private Avaliacao avaliacao;

    // Getters and Setters
    protected Integer getCodigoStatus() { return statusCode; }

    protected void setCodigoStatus(Integer codigoStatus) { this.statusCode = codigoStatus; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public void setStatus(AgendamentoStatus status) {
        if (status != null) {
            this.statusCode = status.getCodigoStatus();
        } else {
            this.statusCode = null;
        }
    }

    public abstract AgendamentoStatus getStatus();
}

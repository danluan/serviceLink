package br.com.servicelink.DTO;

import java.time.LocalDateTime;

public class AgendamentoListagemDTO {
    private Long id;
    private LocalDateTime dataHora;
    private String status;
    private String observacao;
    private Long clienteId;
    private String nomeCliente;
    private Long servicoId;
    private String nomeServico;

    public AgendamentoListagemDTO() {}

    public AgendamentoListagemDTO(Long id, LocalDateTime dataHora, String status, String observacao, Long clienteId, String nomeCliente, Long servicoId, String nomeServico) {
        this.id = id;
        this.dataHora = dataHora;
        this.status = status;
        this.observacao = observacao;
        this.clienteId = clienteId;
        this.nomeCliente = nomeCliente;
        this.servicoId = servicoId;
        this.nomeServico = nomeServico;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }
}

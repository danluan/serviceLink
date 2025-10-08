package br.com.servicelink.DTO;

import java.time.LocalDateTime;

public class AgendamentoDTO {
    private LocalDateTime dataHora;
    private String observacao;
    private Long clienteId;
    private Long servicoId;

    public AgendamentoDTO() {}

    public AgendamentoDTO(LocalDateTime dataHora, String observacao, Long clienteId, Long servicoId) {
        this.dataHora = dataHora;
        this.observacao = observacao;
        this.clienteId = clienteId;
        this.servicoId = servicoId;
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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }
}

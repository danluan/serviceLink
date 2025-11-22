package br.com.serviceframework.framework.service;

import br.com.serviceframework.framework.domain.entity.Agendamento;
import br.com.serviceframework.framework.domain.interfaces.AgendamentoStatus;

public abstract class AbstractAgendamentoService<T extends Agendamento> {
    public final T criarAgendamento(T agendamento) {
        validarRegrasDeNegocio(agendamento);
        agendamento.setStatus(getStatusInicial());
        calcularPreco(agendamento);
        return salvarNoRepositorio(agendamento);
    }

    protected abstract void validarRegrasDeNegocio(T agendamento);
    protected abstract AgendamentoStatus getStatusInicial();
    protected abstract void calcularPreco(T agendamento);
    protected abstract T salvarNoRepositorio(T agendamento);
}

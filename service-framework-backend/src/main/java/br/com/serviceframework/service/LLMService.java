package br.com.serviceframework.service;

import br.com.serviceframework.entity.Agendamento;
import br.com.serviceframework.enumerations.Classificacao;

public interface LLMService {
    Classificacao classificarIntencao(String mensagemCliente);

    String gerarMensagemConfirmacao(Agendamento agendamento);

    String responderDuvidaComum(String mensagemCliente);
}

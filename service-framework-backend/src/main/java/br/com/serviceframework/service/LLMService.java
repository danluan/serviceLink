package br.com.serviceframework.framework.service;

import br.com.serviceframework.framework.domain.entity.Agendamento;
import br.com.serviceframework.framework.domain.enumerations.Classificacao;

public interface LLMService {
    Classificacao classificarIntencao(String mensagemCliente);

    String gerarMensagemConfirmacao(Agendamento agendamento);

    String responderDuvidaComum(String mensagemCliente);
}

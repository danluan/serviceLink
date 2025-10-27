package br.com.servicelink.service;

import br.com.servicelink.entity.Agendamento;
import br.com.servicelink.enumerations.Classificacao;

public interface LLMService {
    Classificacao classificarIntencao(String mensagemCliente);

    String gerarMensagemConfirmacao(Agendamento agendamento);

    String responderDuvidaComum(String mensagemCliente);
}

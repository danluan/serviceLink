package br.com.serviceframework.chatbot.handler;

import br.com.serviceframework.chatbot.model.UserState;
import org.springframework.stereotype.Component;

@Component("orcamento_solicitadoHandler")
public class OrcamentoHandler implements IntentionHandler {

    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "Certo! Qual serviço você precisa? Digite a sua solicitação em uma frase curta, como por exemplo: 'conserto de torneira'.";    }
}
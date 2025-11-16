package br.com.serviceframework.chatbot.handler;

import br.com.serviceframework.chatbot.model.UserState;
import org.springframework.stereotype.Component;

@Component
public class ReclamacaoHandler implements IntentionHandler{
    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "Lamentamos muito por qualquer inconveniente que você tenha tido. 😔\n\n" +
                "Sua reclamação é muito importante e será encaminhada para a nossa equipe responsável. Em breve, entraremos em contato para entender melhor a situação e buscar a melhor solução.\n\n" +
                "Agradecemos a sua compreensão.";
    }
}

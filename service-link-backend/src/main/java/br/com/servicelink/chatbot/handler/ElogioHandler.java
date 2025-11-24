package br.com.serviceframework.framework.chatbot.handler;

import br.com.serviceframework.framework.chatbot.model.UserState;
import org.springframework.stereotype.Component;

@Component
public class ElogioHandler implements IntentionHandler{
    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "Ficamos muito felizes em saber disso! 😊\n\n" +
                "Sua opinião é super importante para nós da ServiceLink e nos motiva a melhorar cada dia mais. Muito obrigado pelo seu feedback!";
    }
}

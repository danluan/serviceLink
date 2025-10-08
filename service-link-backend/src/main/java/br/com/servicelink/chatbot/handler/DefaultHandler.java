package br.com.servicelink.chatbot.handler;

import br.com.servicelink.chatbot.model.UserState;
import org.springframework.stereotype.Component;

@Component
public class DefaultHandler implements IntentionHandler{

    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "Olá! Sou o assistente virtual da ServiceLink. Parece que não consegui entender sua solicitação. Por favor, me diga se você precisa de um orçamento ou se tem alguma dúvida para que eu possa te ajudar.";    }
}

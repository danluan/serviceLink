package br.com.servicelink.chatbot.handler;

import br.com.servicelink.chatbot.model.UserState;
import org.springframework.stereotype.Component;

@Component("inicioHandler")
public class InicioConversaHandler implements IntentionHandler{

    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "Olá! 😊 Seja bem-vindo à ServiceLink. Eu sou seu assistente virtual e estou aqui para ajudar com serviços domésticos de forma rápida e prática.\n\n" +
                "Para começar, me diga se você precisa de um *orçamento*, se tem alguma *dúvida comum* ou se deseja *agendar* um serviço.";    }
}

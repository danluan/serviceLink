package br.com.serviceframework.chatbot.handler;

import br.com.serviceframework.chatbot.model.UserState;
import org.springframework.stereotype.Component;

@Component
public class GeralHandler implements IntentionHandler{
    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "Sou um assistente focado em serviços domésticos da ServiceLink. No momento, só consigo te ajudar com questões relacionadas à nossa plataforma.\n\n" +
                "Se tiver alguma dúvida sobre como agendar um serviço, solicitar um orçamento ou algo parecido, pode me perguntar!";
    }
}

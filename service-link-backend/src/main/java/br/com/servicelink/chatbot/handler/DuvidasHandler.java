package br.com.servicelink.chatbot.handler;

import br.com.servicelink.chatbot.model.UserState;
import org.springframework.stereotype.Component;

@Component
public class DuvidasHandler implements IntentionHandler{

    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "No momento, estou em fase de testes para responder a dúvidas comuns. " +
                "Por favor, me diga sobre o que é a sua dúvida (cancelamento, pagamento, etc.), e farei o possível para te ajudar.\n\n" +
                "Em breve, esta funcionalidade terá um fluxo completo para te atender melhor!";
    }
}

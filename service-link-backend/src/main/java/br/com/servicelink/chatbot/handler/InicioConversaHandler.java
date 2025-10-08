package br.com.servicelink.chatbot.handler;

import br.com.servicelink.chatbot.model.UserState;
import org.springframework.stereotype.Component;

@Component("inicioHandler")
public class InicioConversaHandler implements IntentionHandler {

    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "Olá! 😊 Bem-vindo à ServiceLink. Sou seu assistente virtual e estou aqui para ajudar com seus serviços domésticos.\n\n" +
                "Você pode me perguntar sobre: \n" +
                "💰 **Orçamento:** para saber o preço de um serviço.\n" +
                "✅ **Recomendação de Serviço:** para encontrar o serviço ideal para o seu problema.\n" +
                "❓ **Dúvidas:** para tirar dúvidas comuns sobre nossa plataforma.\n\n" +
                "Se tiver um elogio ou reclamação, também pode me dizer!";
    }
}

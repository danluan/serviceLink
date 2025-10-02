package br.com.servicelink.chatbot.handler;

import br.com.servicelink.chatbot.model.UserState;
import org.springframework.stereotype.Component;

@Component("recomendacao_solicitadaHandler")
public class RecomendacaoSolicitadaHandler implements IntentionHandler{
    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "Certo! 😊 Para que eu possa te ajudar a encontrar a solução ideal, por favor, descreva o problema com o máximo de detalhes possível. \n\n" +
                "Conte-me o que aconteceu, qual o local do problema e qualquer informação que ajude a entender a situação. Assim, poderei te recomendar o serviço mais adequado!";
    }
}

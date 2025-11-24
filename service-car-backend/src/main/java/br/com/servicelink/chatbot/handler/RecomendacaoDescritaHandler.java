package br.com.serviceframework.framework.chatbot.handler;

import br.com.serviceframework.framework.chatbot.model.UserState;
import br.com.serviceframework.serviceLink.service.LLMServiceImpl;
import org.springframework.stereotype.Component;

@Component("recomendacao_descritaHandler")
public class RecomendacaoDescritaHandler implements IntentionHandler{
    private final LLMServiceImpl llmService;

    public RecomendacaoDescritaHandler(LLMServiceImpl llmService) {
        this.llmService = llmService;
    }

    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        String respostaLLM = llmService.recomendarServico(mensagem);

        String[] partes = respostaLLM.split(";");
        String categoria = partes[0].trim();
        String nome = partes[1].trim();
        String explicacao = partes[2].trim();

        String mensagemFinal = "Com base na sua descrição, a melhor recomendação é:\n\n" +
                "Categoria: *" + categoria + "*\n" +
                "Tipo de Serviço: *" + nome + "*\n\n" +
                "Explicação: _" + explicacao + "_";

        return mensagemFinal;
    }
}

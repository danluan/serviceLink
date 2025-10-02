package br.com.servicelink.chatbot.handler;

import br.com.servicelink.chatbot.model.UserState;
import br.com.servicelink.service.impl.LLMServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class DuvidasHandler implements IntentionHandler{
    private final LLMServiceImpl llmService;

    public DuvidasHandler(LLMServiceImpl llmService) {
        this.llmService = llmService;
    }

    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return llmService.responderDuvidaComum(mensagem);
    }
}

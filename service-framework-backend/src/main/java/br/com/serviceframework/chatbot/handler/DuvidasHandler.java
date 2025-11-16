package br.com.serviceframework.chatbot.handler;

import br.com.serviceframework.chatbot.model.UserState;
import br.com.serviceframework.service.impl.LLMServiceImpl;
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

package br.com.serviceframework.framework.chatbot.handler;

import br.com.serviceframework.framework.chatbot.model.UserState;

public interface IntentionHandler {
    String handle(String mensagem, String chatId, UserState userState);
}

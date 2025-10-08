package br.com.servicelink.chatbot.handler;

import br.com.servicelink.chatbot.model.UserState;

public interface IntentionHandler {
    String handle(String mensagem, String chatId, UserState userState);
}

package br.com.serviceframework.chatbot.handler;

import main.java.br.com.serviceframework.framework.chatbot.model.UserState;

public interface IntentionHandler {
    String handle(String mensagem, String chatId, UserState userState);
}

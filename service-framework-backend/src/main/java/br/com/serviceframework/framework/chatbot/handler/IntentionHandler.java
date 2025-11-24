package main.java.br.com.serviceframework.framework.chatbot.handler;

import main.java.br.com.serviceframework.framework.chatbot.model.UserState;

public interface IntentionHandler {
    String handle(String mensagem, String chatId, UserState userState);
}

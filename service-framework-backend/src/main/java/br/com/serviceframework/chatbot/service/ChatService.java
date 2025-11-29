package main.java.br.com.serviceframework.framework.chatbot.service;

import main.java.br.com.serviceframework.framework.chatbot.model.EtapaConversa;
import main.java.br.com.serviceframework.framework.chatbot.model.UserState;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService {

    private final Map<String, UserState> userStates = new ConcurrentHashMap<>();

    public UserState getUserState(String chatId) {
        return userStates.get(chatId);
    }

    public void updateState(String chatId, EtapaConversa novaEtapa, String dadosConversa) {
        UserState state = userStates.get(chatId);
        if (state != null) {
            state.setEtapaAtual(novaEtapa);
            state.setDadosConversa(dadosConversa);
            state.setLastUpdated(LocalDateTime.now());
            userStates.put(chatId, state);
        }
    }

    public void updateState(String chatId, EtapaConversa novaEtapa) {
        UserState state = userStates.get(chatId);
        if (state != null) {
            state.setEtapaAtual(novaEtapa);
            state.setLastUpdated(LocalDateTime.now());
            userStates.put(chatId, state);
        }
    }

    public void createState(String chatId, UserState userState) {
        userStates.put(chatId, userState);
    }

    public void clearUserState(String chatId) {
        userStates.remove(chatId);
    }
}
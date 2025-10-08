package br.com.servicelink.chatbot.service;

import br.com.servicelink.chatbot.model.EtapaConversa;
import br.com.servicelink.chatbot.model.UserState;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;

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
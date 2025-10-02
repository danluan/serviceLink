package br.com.servicelink.chatbot.handler;

import br.com.servicelink.chatbot.model.EtapaConversa;
import br.com.servicelink.chatbot.model.UserState;
import br.com.servicelink.entity.Servico;
import br.com.servicelink.service.ServicoService;
import br.com.servicelink.service.impl.LLMServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("orcamento_solicitadoHandler")
public class OrcamentoHandler implements IntentionHandler {

    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        return "Ok! Por favor, me dê uma descrição detalhada do problema ou serviço que você precisa.";
    }
}
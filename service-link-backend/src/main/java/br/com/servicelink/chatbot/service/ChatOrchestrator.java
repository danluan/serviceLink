package br.com.servicelink.chatbot.service;

import br.com.servicelink.chatbot.handler.IntentionHandler;
import br.com.servicelink.chatbot.model.EtapaConversa;
import br.com.servicelink.chatbot.model.UserState;
import br.com.servicelink.entity.Classificacao;
import br.com.servicelink.service.ServicoService;
import br.com.servicelink.service.impl.LLMServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatOrchestrator {
    private final LLMServiceImpl llmService;
    private final ChatService chatService;
    private final Map<String, IntentionHandler> handlers;

    private final Map<Classificacao, EtapaConversa> classificacaoParaEstado = Map.of(
            Classificacao.ORCAMENTO, EtapaConversa.ORCAMENTO_SOLICITADO,
            Classificacao.RECOMENDACAO_SERVICO, EtapaConversa.RECOMENDACAO_SOLICITADA,
            Classificacao.DUVIDA_COMUM, EtapaConversa.DUVIDAS,
            Classificacao.ELOGIO, EtapaConversa.ELOGIO,
            Classificacao.AGRADECIMENTO, EtapaConversa.ELOGIO,
            Classificacao.RECLAMACAO, EtapaConversa.RECLAMACAO,
            Classificacao.SAUDACAO, EtapaConversa.INICIO,
            Classificacao.GERAL, EtapaConversa.GERAL
    );

    public ChatOrchestrator(LLMServiceImpl llmService,
                       ChatService chatService,
                       Map<String, IntentionHandler> handlers
    ) {
        this.llmService = llmService;
        this.chatService = chatService;
        this.handlers = handlers;
    }

    public String processarMensagem(String mensagemUsuario, String chatId) {
        UserState userState = chatService.getUserState(chatId);

        if (userState == null || userState.getEtapaAtual() == EtapaConversa.INICIO) {
            Classificacao classificacao = llmService.classificarIntencao(mensagemUsuario);
            System.out.println("Classificacao: " + classificacao);

            EtapaConversa etapaAtual = classificacaoParaEstado.getOrDefault(classificacao, EtapaConversa.INICIO);
            System.out.println("Etapa atual: " + etapaAtual);
            userState = new UserState(etapaAtual);
            chatService.createState(chatId, userState);
        }

        String handlerName = userState.getEtapaAtual().name().toLowerCase() + "Handler";
        IntentionHandler handler = handlers.get(handlerName);

        if (handler == null) {
            handler = handlers.get("defaultHandler");
        }

        String respostaBot = handler.handle(mensagemUsuario, chatId, userState);

        if (userState.getEtapaAtual() == EtapaConversa.ORCAMENTO_SOLICITADO) {
            userState.setEtapaAtual(EtapaConversa.ORCAMENTO_DESCRITO);
            chatService.updateState(chatId, EtapaConversa.ORCAMENTO_DESCRITO);
        } else if (userState.getEtapaAtual() == EtapaConversa.ORCAMENTO_DESCRITO) {
            chatService.clearUserState(chatId);
        }

        if (userState.getEtapaAtual() == EtapaConversa.RECOMENDACAO_SOLICITADA) {
            userState.setEtapaAtual(EtapaConversa.RECOMENDACAO_DESCRITA);
            chatService.updateState(chatId, EtapaConversa.RECOMENDACAO_DESCRITA);
        } else if (userState.getEtapaAtual() == EtapaConversa.RECOMENDACAO_DESCRITA) {
            chatService.clearUserState(chatId);
        }

        if (userState.getEtapaAtual() == EtapaConversa.INICIO ||
                userState.getEtapaAtual() == EtapaConversa.ELOGIO ||
                userState.getEtapaAtual() == EtapaConversa.RECLAMACAO ||
                userState.getEtapaAtual() == EtapaConversa.DUVIDAS) {

            chatService.clearUserState(chatId);
        }

        return respostaBot;
    }
}

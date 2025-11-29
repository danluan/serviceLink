package main.java.br.com.serviceframework.framework.chatbot.service;

import br.com.serviceframework.framework.domain.enumerations.Classificacao;
import main.java.br.com.serviceframework.framework.chatbot.handler.IntentionHandler;
import main.java.br.com.serviceframework.framework.chatbot.model.EtapaConversa;
import org.springframework.stereotype.Service;
import br.com.serviceframework.framework.service.LLMService;

import java.util.Map;

@Service
public abstract class ChatOrchestrator {
    private final LLMService llmService;
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

    public ChatOrchestrator(LLMService llmService,
                       ChatService chatService,
                       Map<String, IntentionHandler> handlers
    ) {
        this.llmService = llmService;
        this.chatService = chatService;
        this.handlers = handlers;
    }

    public String processarMensagem(String mensagemUsuario, String chatId) {
        return mensagemUsuario;
    }
}

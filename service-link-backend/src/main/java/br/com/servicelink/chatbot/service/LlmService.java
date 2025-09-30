package br.com.servicelink.chatbot.service;

import org.springframework.ai.chat.client.ChatClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LlmService {
    private final ChatClient chatClient;

    @Autowired
    public LlmService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String classificarIntencao(String mensagemUsuario){
        String systemPrompt = "Você é um classificador de intenções para um chatbot de serviços domésticos. Sua única tarefa é analisar a mensagem do usuário e responder APENAS com uma das seguintes categorias: servico_domestico, orcamento, duvida_comum, elogio, reclamacao, agradecimento, saudacao, geral.";

        return chatClient.prompt()
                .system(systemPrompt)
                .user(mensagemUsuario)
                .call()
                .content();
    }

    public String classificarServico(String mensagemUsuario) {
        String systemPrompt = "Você é um extrator de informações de serviços domésticos. Sua única tarefa é analisar a mensagem do usuário e retornar APENAS a categoria do serviço e o nome do serviço separados por virgula Ex: LIMPEZA,limpeza geral. Se a categoria não for encontrada, retorne 'OUTRA'.\n" +
                "\n" +
                "Categorias de serviço: [HIDRÁULICA, ELÉTRICA, JARDINAGEM, LIMPEZA, OUTRA].";

        return chatClient.prompt()
                .system(systemPrompt)
                .user(mensagemUsuario)
                .call()
                .content();
    }
}

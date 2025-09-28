package br.com.servicelink.service.impl;

import br.com.servicelink.entity.Agendamento;
import br.com.servicelink.entity.Classificacao;
import br.com.servicelink.service.LLMService;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;


@Service
public class LLMServiceImpl implements LLMService {
    private static final Logger logger = LoggerFactory.getLogger(LLMServiceImpl.class);
    private final GenerativeModel generativeModel;

    public LLMServiceImpl(GenerativeModel generativeModel) {
        this.generativeModel = generativeModel;
    }

    @Override
    public Classificacao classificarIntencao(String mensagemCliente) {
        String prompt = String.format(
                "Você é um classificador de intenções para um chatbot de serviços domésticos. " +
                "Sua única tarefa é analisar a mensagem do usuário e responder APENAS com uma das seguintes categorias: " +
                "SERVICO_DOMESTICO, ORCAMENTO, DUVIDA_COMUM, ELOGIO, RECLAMACAO, AGRADECIMENTO, SAUDACAO, GERAL. " +
                "Mensagem do usuário: \"%s\"", mensagemCliente
        );
        String categoriaRetornada = chamarAPI(prompt);
        return Classificacao.valueOf(categoriaRetornada);
    }

    @Override
    public String gerarMensagemConfirmacao(Agendamento agendamento) {
        String prompt = String.format(
                "Gere uma mensagem de confirmação de agendamento amigável e concisa com os seguintes dados: " +
                "Cliente: %s, Serviço: %s, Data: %s, Horário: %s, O valor será de R$%s. " +
                "Não inclua saudações nem despedidas.",
                agendamento.getCliente().getNome(),
                agendamento.getServico().getNome(),
                agendamento.getDataHora().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                agendamento.getDataHora().toLocalTime().toString(),
                agendamento.getServico().getPrecoBase().toString()
        );
        return chamarAPI(prompt);
    }

    @Override
    public String responderDuvidaComum(String mensagemCliente) {
        String prompt = String.format(
                "Você é um assistente virtual de uma plataforma de serviços domésticos chamada ServiceLink. " +
                "Sua função é responder a perguntas comuns de clientes de forma concisa e útil, agindo como um FAQ inteligente. Siga estritamente as regras abaixo:\n" +
                "1. Se a pergunta for sobre **cancelamento**, a política é: 'Cancelamentos podem ser feitos com até 24 horas de antecedência sem custo. Após esse período, pode haver uma taxa.'\n" +
                "2. Se a pergunta for sobre **alterar o horário**, oriente o cliente a entrar em contato direto com o profissional pelo chat da plataforma para verificar a disponibilidade.\n" +
                "3. Se a pergunta for sobre **preço ou pagamento**, informe que o valor final é definido pelo profissional e que o pagamento é feito pela plataforma após a conclusão do serviço.\n" +
                "4. Se a pergunta não se encaixar nessas categorias, responda de forma educada que você não tem informações sobre esse tópico específico.\n\n" +
                "--- \n" +
                "Pergunta do cliente: \"%s\"",
                mensagemCliente
        );
        String resposta = chamarAPI(prompt);

        if (resposta.isEmpty()) {
            return "Desculpe, não consegui processar sua dúvida no momento. Tente novamente.";
        }

        return resposta;
    }

    private String chamarAPI(String prompt) {
        try {
            GenerateContentResponse response = this.generativeModel.generateContent(
                    ContentMaker.fromString(prompt)
            );
            return ResponseHandler.getText(response).trim();
        } catch (Exception e) {
            logger.error("Erro ao chamar API do Gemini: ", e);
            return "";
        }
    }
}

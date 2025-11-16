package br.com.serviceframework.chatbot.handler;

import br.com.serviceframework.chatbot.model.UserState;
import br.com.serviceframework.entity.Servico;
import br.com.serviceframework.service.ServicoService;
import br.com.serviceframework.service.impl.LLMServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("orcamento_descritoHandler")
public class OrcamentoDescritoHandler implements IntentionHandler{
    private final LLMServiceImpl llmService;
    private final ServicoService servicoService;

    public OrcamentoDescritoHandler(LLMServiceImpl llmService, ServicoService servicoService) {
        this.llmService = llmService;
        this.servicoService = servicoService;
    }

    @Override
    public String handle(String mensagem, String chatId, UserState userState) {
        String respostaLLM = llmService.classificarServico(mensagem);

        String[] partes = respostaLLM.split(";");
        String categoria = partes[0].trim();
        String nome = partes[1].trim();
        System.out.println("Categoria: " + categoria);
        System.out.println("Nome: " + nome);

        List<Servico> servicosEncontrados = servicoService.buscarServicosPorPrecoBase(categoria, nome);

        if(servicosEncontrados.isEmpty()){
            return "Ops! Não conseguimos encontrar serviços que correspondam à sua descrição. 😕\n\n" +
                    "Por favor, tente descrever o serviço de uma forma diferente ou verifique se você digitou a categoria e o nome corretamente.";
        }

        StringBuilder resposta = new StringBuilder();
        resposta.append("Veja o orçamento de alguns serviços encontrados com os critérios definidos:\n\n");

        for (Servico servico : servicosEncontrados) {
            resposta.append("*")
                    .append(servico.getNome())
                    .append("*\n")
                    .append("Preço: R$")
                    .append(servico.getPrecoBase())
                    .append("\n")
                    .append("Descrição: _")
                    .append(servico.getDescricao())
                    .append("_\n\n");
        }

        return resposta.toString();
    }
}

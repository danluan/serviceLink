package br.com.servicelink.service.waha;

import br.com.servicelink.service.llm.LlmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WahaService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String WAHA_URL = "http://localhost:3000/api/";

    public void sendMessage(String sessionId, String chatId, String resposta) {
        String url = String.format("%s/sendText", WAHA_URL);

        Map<String, String> body = new HashMap<>();
        body.put("chatId", chatId);
        body.put("text", resposta);
        body.put("session", sessionId);

        restTemplate.postForObject(url, body, Void.class);
    }

    public void sendSeen(){

    }
    //servico_domestico, orcamento, duvida_comum, elogio, reclamacao, agradecimento, saudacao, geral
    public String processarMensagem(String classificacao) {
        String resposta = "";
        switch (classificacao) {
            case "orcamento":
                resposta = "Ok! Para iniciar o processo de orçamento, por favor, me informe o tipo de serviço que você precisa (ex: hidráulico, elétrico, etc.).";
                fluxoOrcamento();
                break;
            case "duvida_comum":
                resposta = "Responder dúvida comum";
                fluxoDuvida();
                break;
            case "servico_domestico":
                resposta = "Responder domestico";
                fluxoServicoDomestico();
                break;
            case "elogio":
                resposta = "Agradecemos pelo elogio!";
                break;
            case "reclamacao":
                resposta = "Sua reclamação será acatada e iremos melhorar!";
                break;
            case "agradecimento":
                resposta = "Estamos sempre contentes em ajudar, disponha!";
                break;
            case "saudacao":
                resposta = "Olá! Você chamou o serviço de atendimento ao cliente da *ServiceLink*.\n\n";
                break;
            case "geral":
                resposta = "Só posso responder dúvidas sobre a Service Link.";
                break;
            default:
                resposta = "Olá! Você chamou o serviço de atendimento ao cliente da *ServiceLink*.\n";
                break;
        }
        return resposta;
    }
}

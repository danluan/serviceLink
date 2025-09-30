package br.com.servicelink.controller;

import br.com.servicelink.service.waha.WahaService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whatsapp")
public class WahaController {
    @Autowired
    WahaService wahaService;

    @PostMapping("/webhook")
    public void handleIncomingMessage(@RequestBody JsonNode payload) {
        String sessionId = payload.get("session").asText();
        JsonNode messagePayload = payload.get("payload");
        String senderId = messagePayload.get("from").asText();
        String messageText = messagePayload.get("body").asText();

        System.out.println("Mensagem recebida da sessão '" + sessionId + "' de " + senderId + ": " + messageText);
        //Aplicar lógica. Para testar vou só retornar a mesma mensagem por enquanto
        String respostaBot = "Você enviou " + messageText;

        wahaService.sendMessage(sessionId, senderId, respostaBot);
    }
}

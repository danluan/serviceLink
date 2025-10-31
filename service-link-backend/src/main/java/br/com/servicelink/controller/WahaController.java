package br.com.servicelink.controller;

import br.com.servicelink.chatbot.service.ChatOrchestrator;
import br.com.servicelink.chatbot.service.WahaService;
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

    @Autowired
    ChatOrchestrator chatOrchestrator;

    /**
     * Processa mensagens recebidas do WhatsApp via webhook.
     *
     * @param payload Dados da mensagem recebida do WhatsApp
     */
    @PostMapping("/webhook")
    public void handleIncomingMessage(@RequestBody JsonNode payload) {
        String sessionId = payload.get("session").asText();
        JsonNode messagePayload = payload.get("payload");
        String senderId = messagePayload.get("from").asText();
        String messageText = messagePayload.get("body").asText();

        if (senderId.contains("@g.us") || senderId.contains("@broadcast")) {
            System.out.println("Mensagem de grupo ignorada. Não irei responder.");
            return;
        }

        System.out.println("Mensagem recebida da sessão '" + sessionId + "' de " + senderId + ": " + messageText);
        String respostaBot = chatOrchestrator.processarMensagem(messageText, senderId);

        wahaService.sendMessage(sessionId, senderId, respostaBot);
    }
}

package br.com.servicelink.controller;

import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.RecomendacoesCliente;
import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.service.AbstractRecomendacoesClienteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacoesClienteController {

    private final AbstractRecomendacoesClienteService recomendacoesService;
    private final ClienteRepository clienteRepository;

    public RecomendacoesClienteController(
            AbstractRecomendacoesClienteService recomendacoesService,
            ClienteRepository clienteRepository
    ) {
        this.recomendacoesService = recomendacoesService;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Gera e salva uma nova recomendação para o cliente
     */
    @PostMapping("/cliente/{clienteId}")
    public RecomendacoesCliente gerar(@PathVariable Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId.longValue())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        return recomendacoesService.criarRecomendacao(cliente);
    }

    /**
     * Busca uma recomendação já salva
     */
    @GetMapping("/{id}")
    public RecomendacoesCliente buscar(@PathVariable Integer id) {
        return recomendacoesService.getRecomendacao(id);
    }
}

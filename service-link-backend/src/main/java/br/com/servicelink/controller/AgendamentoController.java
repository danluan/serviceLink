package br.com.servicelink.controller;

import br.com.servicelink.DTO.AgendamentoDTO;
import br.com.servicelink.DTO.AgendamentoListagemDTO;
import br.com.servicelink.DTO.AvaliacaoDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import br.com.servicelink.enumerations.AgendamentoStatus;
import br.com.servicelink.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {
    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public List<AgendamentoListagemDTO> findAll(){
        return agendamentoService.listarAgendamentos();
    }

    @GetMapping(value = "/{id}")
    public AgendamentoListagemDTO findById(@PathVariable Long id){
        return agendamentoService.buscarAgendamentosPorId(id);
    }

    @PostMapping
    public AgendamentoDTO save(@RequestBody AgendamentoDTO agendamentoDTO){
        AgendamentoDTO newAgendamento = agendamentoService.salvarAgendamento(agendamentoDTO);
        return newAgendamento;
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        agendamentoService.deletarAgendamento(id);
    }

    @GetMapping("/{prestadorId}/agendamentos")
    public List<AgendamentoListagemDTO> listarAgendamentosDoPrestador(@PathVariable Long prestadorId) {
        return agendamentoService.listarAgendamentosPorPrestador(prestadorId);
    }

    @GetMapping("/{id}/avaliacao")
    public AvaliacaoDTO avaliacaoPorAgendamento(@PathVariable Long id) {
        try {
            AvaliacaoDTO avaliacao = agendamentoService.avaliacaoPorAgendamentoId(id);
            return avaliacao;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada para o agendamento ID: " + id, e);
        }
    }

    @PostMapping("/{id}/avaliacao")
    public void avaliacao(@PathVariable Long id, @RequestBody AvaliacaoDTO avaliacaoDTO) {
        agendamentoService.adicionarAvaliacaoAoAgendamento(id, avaliacaoDTO);
    }

    @GetMapping("/{prestadorId}/agendamentos-hoje")
    public List<AgendamentoListagemDTO> listarAgendamentosDeHoje(@PathVariable Long prestadorId) {
        return agendamentoService.listarAgendamentosDoDia(prestadorId);
    }

    @GetMapping("/{prestadorId}/prox-agendamentos")
    public List<AgendamentoListagemDTO> listarAgendamentosFuturos(@PathVariable Long prestadorId) {
        return agendamentoService.listarProximos5Agendamentos(prestadorId);
    }

    @GetMapping("/{prestadorId}/faturamento")
    public BigDecimal listarFaturamentoMensal(
            @PathVariable Long prestadorId,
            @RequestParam Integer ano,
            @RequestParam Integer mes
    ) {
        return agendamentoService.calcularFaturamentoMensal(prestadorId, ano, mes);
    }

    @GetMapping("/{prestadorId}/agendamentos/mensal")
    public Map<Integer, List<AgendamentoListagemDTO>> listarAgendamentosPorMes(
            @PathVariable Long prestadorId,
            @RequestParam Integer ano,
            @RequestParam Integer mes) {
        return agendamentoService.buscarAgendamentosPorMes(prestadorId, ano, mes);
    }

    @GetMapping("/cliente/{clienteId}/agendamentos")
    public List<AgendamentoListagemDTO> listarAgendamentosDoCliente(@PathVariable Long clienteId) {
        return agendamentoService.listarAgendamentosPorCliente(clienteId);
    }

    @PutMapping("/{agendamentoId}/status/{status}")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long agendamentoId,
            @PathVariable AgendamentoStatus status
    ) {
        try{
            agendamentoService.editarStatusAgendamento(agendamentoId, status);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }

}

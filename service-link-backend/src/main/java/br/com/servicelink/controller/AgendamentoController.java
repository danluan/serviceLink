package br.com.servicelink.controller;

import br.com.servicelink.DTO.AgendamentoDTO;
import br.com.servicelink.DTO.AgendamentoListagemDTO;
import br.com.servicelink.entity.Agendamento;
import br.com.servicelink.entity.Avaliacao;
import br.com.servicelink.exceptions.BusinessException;
import br.com.servicelink.service.AgendamentoService;
import br.com.servicelink.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Agendamento save(@RequestBody AgendamentoDTO agendamentoDTO){
        return agendamentoService.salvarAgendamento(agendamentoDTO);
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
    public Avaliacao avaliacaoPorAgendamento(@PathVariable Long id) {
        try {
            Avaliacao avaliacao = agendamentoService.avaliacaoPorAgendamentoId(id);
            return avaliacao;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/{id}/avaliacao")
    public void avaliacao(@PathVariable Long id, @RequestBody Avaliacao avaliacao) {
        agendamentoService.adicionarAvaliacaoAoAgendamento(id, avaliacao);
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

}

package br.com.servicelink.controller;

import br.com.servicelink.DTO.AgendamentoDTO;
import br.com.servicelink.DTO.AgendamentoListagemDTO;
import br.com.servicelink.entity.Agendamento;
import br.com.servicelink.service.AgendamentoService;
import br.com.servicelink.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {
    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public List<AgendamentoListagemDTO> findAll(){
        return agendamentoService.listarAgendamentos();
    }

    @GetMapping(value = "/{id}")
    public Optional<AgendamentoListagemDTO> findById(@PathVariable Long id){
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
}

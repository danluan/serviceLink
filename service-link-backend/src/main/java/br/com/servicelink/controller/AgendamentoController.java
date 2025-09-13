package br.com.servicelink.controller;

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
    public List<Agendamento> findAll(){
        return agendamentoService.listarAgendamentos();
    }

    @GetMapping(value = "/{id}")
    public Optional<Agendamento> findById(@PathVariable Long id){
        return agendamentoService.buscarAgendamentosPorId(id);
    }

    @PostMapping
    public Agendamento save(@RequestBody Agendamento agendamento){
        return agendamentoService.salvarAgendamento(agendamento);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        agendamentoService.deletarAgendamento(id);
    }
}

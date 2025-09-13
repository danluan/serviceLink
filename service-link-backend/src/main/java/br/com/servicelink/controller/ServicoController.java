package br.com.servicelink.controller;

import br.com.servicelink.entity.Servico;
import br.com.servicelink.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/servico")
public class ServicoController {
    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public List<Servico> findAll(){
        return servicoService.listarServicos();
    }

    @GetMapping(value = "/{id}")
    public Optional<Servico> findById(@PathVariable Long id){
        return servicoService.buscarServicoPorId(id);
    }

    @PostMapping
    public Servico save(@RequestBody Servico servico){
        return servicoService.salvarServico(servico);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        servicoService.deletarServico(id);
    }
}

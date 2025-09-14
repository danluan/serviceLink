package br.com.servicelink.controller;

import br.com.servicelink.DTO.PrestadorCadastroDTO;
import br.com.servicelink.entity.Prestador;
import br.com.servicelink.service.PrestadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prestador")
public class PrestadorController {
    @Autowired
    private PrestadorService prestadorService;

    @GetMapping
    public List<Prestador> findAll(){
        return prestadorService.listarPrestadores();
    }

    @GetMapping(value = "/{id}")
    public Optional<Prestador> findById(@PathVariable Long id){
        return prestadorService.buscarPrestadorPorId(id);
    }

    @PostMapping
    public Prestador save(@RequestBody PrestadorCadastroDTO prestador){
        return prestadorService.salvarPrestador(prestador);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        prestadorService.deletarPrestador(id);
    }
}

package br.com.servicelink.controller;

import br.com.servicelink.DTO.ClienteCadastroDTO;
import br.com.servicelink.entity.Cliente;
import br.com.servicelink.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> findAll(){
        return clienteService.listarClientes();
    }

    @GetMapping(value = "/{id}")
    public Optional<Cliente> findById(@PathVariable Long id){
        return clienteService.buscarClientePorId(id);
    }

    @PostMapping
    public Cliente save(@RequestBody ClienteCadastroDTO cliente){
        return clienteService.salvarCliente(cliente);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        clienteService.deletarCliente(id);
    }
}

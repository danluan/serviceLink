package br.com.servicelink.controller;

import br.com.serviceframework.domain.DTO.ClienteDTO;
import br.com.serviceframework.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    /**
     * Lista todos os clientes cadastrados.
     *
     * @return Lista de clientes
     */
    @GetMapping
    public List<ClienteDTO> findAll(){
        return clienteService.listarClientes();
    }

    /**
     * Busca um cliente específico por ID.
     *
     * @param id ID do cliente
     * @return Dados do cliente
     */
    @GetMapping(value = "/{id}")
    public ClienteDTO findById(@PathVariable Long id){
        return clienteService.buscarClientePorId(id);
    }

    /**
     * Desativa específico um cliente.
     *
     * @param id ID do cliente a ser desativado
     */
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        clienteService.deletarCliente(id);
    }
}

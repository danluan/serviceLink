package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.ClienteCadastroDTO;
import br.com.servicelink.entity.Cliente;

public interface ClienteService {
    Cliente salvarCliente(ClienteCadastroDTO cliente);
    List<Cliente> listarClientes();
    Optional<Cliente> buscarClientePorId(Long id);
    void deletarCliente(Long id);
}

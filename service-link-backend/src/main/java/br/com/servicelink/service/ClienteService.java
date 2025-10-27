package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.ClienteCadastroDTO;
import br.com.servicelink.entity.Cliente;
import br.com.servicelink.entity.User;

public interface ClienteService {
    Cliente salvarCliente(ClienteCadastroDTO cliente);
    Cliente salvarCliente(User user);
    List<Cliente> listarClientes();
    Optional<Cliente> buscarClientePorId(Long id);
    void deletarCliente(Long id);
}

package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;
import br.com.servicelink.entity.Cliente;

public interface ClienteService {
    Cliente salvarCliente(Cliente cliente);
    List<Cliente> listarClientes();
    Optional<Cliente> buscarClientePorId(Long id);
    void deletarCliente(Long id);
}

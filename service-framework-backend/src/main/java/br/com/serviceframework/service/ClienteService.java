package br.com.serviceframework.service;

import java.util.List;

import br.com.serviceframework.DTO.ClienteDTO;
import br.com.serviceframework.entity.Cliente;
import br.com.serviceframework.entity.User;

public interface ClienteService {
    Cliente salvarCliente(User user);
    List<ClienteDTO> listarClientes();
    ClienteDTO buscarClientePorId(Long id);
    void deletarCliente(Long id);
}

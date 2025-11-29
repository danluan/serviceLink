package br.com.serviceframework.framework.service;

import java.util.List;

import br.com.serviceframework.framework.domain.DTO.ClienteDTO;
import br.com.serviceframework.framework.domain.entity.Cliente;
import br.com.serviceframework.framework.domain.entity.User;

public interface ClienteService {
    Cliente salvarCliente(User user);
    List<ClienteDTO> listarClientes();
    ClienteDTO buscarClientePorId(Long id);
    void deletarCliente(Long id);
}

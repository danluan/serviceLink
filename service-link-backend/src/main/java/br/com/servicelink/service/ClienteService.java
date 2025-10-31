package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.ClienteCadastroDTO;
import br.com.servicelink.DTO.ClienteDTO;
import br.com.servicelink.entity.Cliente;
import br.com.servicelink.entity.User;

public interface ClienteService {
    Cliente salvarCliente(User user);
    List<ClienteDTO> listarClientes();
    ClienteDTO buscarClientePorId(Long id);
    void deletarCliente(Long id);
}

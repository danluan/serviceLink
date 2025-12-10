package br.com.servicelink.service;

import java.util.List;

import br.com.serviceframework.domain.DTO.ClienteDTO;
import br.com.serviceframework.domain.entity.User;
import br.com.serviceframework.repository.UserRepository;
import br.com.serviceframework.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.service.ClienteService;
import br.com.serviceframework.domain.entity.Cliente;

@Service
public class ClienteServiceImpl extends ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    UserRepository userRepository;

    private AuthService authService;

    @Override
    public Cliente salvarCliente(User user) {
        Cliente cliente = new Cliente();

        cliente.setUser(user);

        return clienteRepository.save(cliente);
    }

    @Override
    public List<ClienteDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();

        return clientes.stream().map(cliente -> new ClienteDTO(
                cliente.getId(),
                cliente.getUser().getId(),
                cliente.getUser().getUsername(),
                cliente.getUser().getEmail()
        )).toList();
    }

    @Override
    public ClienteDTO buscarClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));

        return new ClienteDTO(
                cliente.getId(),
                cliente.getUser().getId(),
                cliente.getUser().getUsername(),
                cliente.getUser().getEmail()
        );
    }

    @Override
    public void deletarCliente(Long id) {
        // ClienteRepository.deleteById(id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));

        User user = cliente.getUser();
        user.setActive(false);
        userRepository.save(user);

        clienteRepository.save(cliente);
    }

    public Long getClienteIdByUserId(Long id) {
        Cliente cliente = clienteRepository.findByUserId(id);

        return cliente.getId();
    }
}

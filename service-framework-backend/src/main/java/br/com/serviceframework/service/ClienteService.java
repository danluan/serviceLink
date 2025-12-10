package br.com.serviceframework.service;

import br.com.serviceframework.domain.DTO.ClienteDTO;
import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.User;
import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.repository.UserRepository;
import br.com.serviceframework.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    UserRepository userRepository;

    private AuthService authService;

    public Cliente salvarCliente(User user) {
        Cliente cliente = new Cliente();

        cliente.setUser(user);

        return clienteRepository.save(cliente);
    }

    public List<ClienteDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();

        return clientes.stream().map(cliente -> new ClienteDTO(
                cliente.getId(),
                cliente.getUser().getId(),
                cliente.getUser().getNome(),
                cliente.getUser().getEmail(),
                cliente.getUser().getTelefone(),
                cliente.getUser().getCpfCnpj()
        )).toList();
    }

    public ClienteDTO buscarClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));

        return new ClienteDTO(
                cliente.getId(),
                cliente.getUser().getId(),
                cliente.getUser().getNome(),
                cliente.getUser().getEmail(),
                cliente.getUser().getTelefone(),
                cliente.getUser().getCpfCnpj()
        );
    }

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

package br.com.servicelink.service.impl;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.ClienteCadastroDTO;
import br.com.servicelink.DTO.ClienteDTO;
import br.com.servicelink.DTO.UserDTO;
import br.com.servicelink.DTO.UserRegisterDTO;
import br.com.servicelink.entity.Prestador;
import br.com.servicelink.entity.User;
import br.com.servicelink.enumerations.Perfis;
import br.com.servicelink.repository.UserRepository;
import br.com.servicelink.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.servicelink.repository.ClienteRepository;
import br.com.servicelink.service.ClienteService;
import br.com.servicelink.entity.Cliente;

@Service
public class ClienteServiceImpl implements ClienteService {

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
                cliente.getUser().getNome(),
                cliente.getUser().getEmail(),
                cliente.getUser().getTelefone(),
                cliente.getUser().getCpfCnpj()
        )).toList();
    }

    @Override
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

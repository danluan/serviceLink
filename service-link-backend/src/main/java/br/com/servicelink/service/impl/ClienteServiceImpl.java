package br.com.servicelink.service.impl;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.ClienteCadastroDTO;
import br.com.servicelink.entity.Prestador;
import br.com.servicelink.entity.User;
import br.com.servicelink.enumerations.Perfis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.servicelink.repository.ClienteRepository;
import br.com.servicelink.service.ClienteService;
import br.com.servicelink.entity.Cliente;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {    this.clienteRepository = clienteRepository;}

    @Override
    public Cliente salvarCliente(ClienteCadastroDTO clienteDTO) {
        User user = new User();
        user.setNome(clienteDTO.nome());
        user.setEmail(clienteDTO.email());
        user.setSenha(clienteDTO.senha());
        user.setCpfCnpj(clienteDTO.cpf());
        user.setPerfil(Perfis.PRESTADOR);

        Cliente cliente = new Cliente();

        cliente.setUser(user);

        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente salvarCliente(User user) {
        Cliente cliente = new Cliente();

        cliente.setUser(user);

        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public Long getClienteIdByUserId(Long id) {
        Cliente cliente = clienteRepository.findByUserId(id);

        return cliente.getId();
    }
}

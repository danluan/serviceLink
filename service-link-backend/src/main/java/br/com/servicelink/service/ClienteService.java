package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;
import br.com.servicelink.entity.Cliente;

public interface ClienteService {
    Cliente salvar(Cliente cliente);
    List<Cliente> buscarTodos();
    Optional<Cliente> buscarPorId(Long id);
    void deletar(Long id);
}

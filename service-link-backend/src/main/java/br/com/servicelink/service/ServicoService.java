package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;
import br.com.servicelink.entity.Servico;

public interface ServicoService {
    Servico salvar(Servico servico);
    List<Servico> buscarTodos();
    Optional<Servico> buscarPorId(Long id);
    void deletar(Long id);
    List<Servico> buscarPorNome(String nome);
}

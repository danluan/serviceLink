package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;
import br.com.servicelink.entity.Servico;

public interface ServicoService {
    Servico salvarServico(Servico servico);
    List<Servico> listarServicos();
    Optional<Servico> buscarServicoPorId(Long id);
    void deletarServico(Long id);
    List<Servico> buscarServicoPorNome(String nome);
}

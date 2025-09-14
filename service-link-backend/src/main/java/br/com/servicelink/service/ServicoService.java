package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.ServicoDTO;
import br.com.servicelink.entity.Servico;

public interface ServicoService {
    List<Servico> adicionarServicos(Long prestadorId, List<ServicoDTO> servicosDTO);
    List<Servico> listarServicos();
    Optional<Servico> buscarServicoPorId(Long id);
    void deletarServico(Long id);
    List<Servico> buscarServicoPorNome(String nome);
}

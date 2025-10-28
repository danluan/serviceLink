package br.com.servicelink.service;

import java.util.List;

import br.com.servicelink.DTO.ServicoDTO;
import br.com.servicelink.entity.Servico;
import org.apache.coyote.BadRequestException;

public interface ServicoService {
    List<Servico> adicionarServicos(Long prestadorId, List<ServicoDTO> servicosDTO) throws BadRequestException;
    Servico editarServico(Long servicoId, ServicoDTO servicosDTO) throws BadRequestException;
    List<Servico> listarServicos();
    Servico buscarServicoPorId(Long id);
    void deletarServico(Long id);
    List<Servico> buscarServicoPorNome(String nome);
    List<ServicoDTO> buscarServicosPorCategoria(String categoria);
    List<Servico> buscarServicosPorPrecoBase(String categoria, String nome);
    List<Servico> buscarServicosPorPrestadorId(Long prestadorId);
}

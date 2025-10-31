package br.com.servicelink.service;

import java.util.List;

import br.com.servicelink.DTO.BuscaServicosDTO;
import br.com.servicelink.DTO.ServicoDTO;
import br.com.servicelink.entity.Servico;
import org.apache.coyote.BadRequestException;

public interface ServicoService {
    List<Servico> adicionarServicos(Long prestadorId, List<ServicoDTO> servicosDTO) throws BadRequestException;
    Servico editarServico(Long servicoId, ServicoDTO servicosDTO) throws BadRequestException;
    List<Servico> listarServicos();
    void deletarServico(Long id);
    List<Servico> buscarServicosPorPrecoBase(String categoria, String nome);
    List<Servico> buscarServicosPorPrestadorId(Long prestadorId);
    List<Servico> buscarServico(BuscaServicosDTO servicoDTO) throws BadRequestException;
}

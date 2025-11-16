package br.com.serviceframework.service;

import java.util.List;

import br.com.serviceframework.DTO.BuscaServicosDTO;
import br.com.serviceframework.DTO.ServicoDTO;
import br.com.serviceframework.entity.Servico;
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

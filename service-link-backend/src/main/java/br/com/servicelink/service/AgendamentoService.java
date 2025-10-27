package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.AgendamentoDTO;
import br.com.servicelink.DTO.AgendamentoListagemDTO;
import br.com.servicelink.entity.Agendamento;
import br.com.servicelink.entity.Avaliacao;

public interface AgendamentoService {
    Agendamento salvarAgendamento(AgendamentoDTO agendamentoDTO);
    List<AgendamentoListagemDTO> listarAgendamentos();
    AgendamentoListagemDTO buscarAgendamentosPorId(Long id);
    void deletarAgendamento(Long id);
    List<AgendamentoListagemDTO> listarAgendamentosPorPrestador(Long prestadorId);
    Avaliacao avaliacaoPorAgendamentoId(Long id);
    void adicionarAvaliacaoAoAgendamento(Long id, Avaliacao avaliacao);
}


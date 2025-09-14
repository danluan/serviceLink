package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.AgendamentoDTO;
import br.com.servicelink.DTO.AgendamentoListagemDTO;
import br.com.servicelink.entity.Agendamento;

public interface AgendamentoService {
    Agendamento salvarAgendamento(AgendamentoDTO agendamentoDTO);
    List<AgendamentoListagemDTO> listarAgendamentos();
    Optional<AgendamentoListagemDTO> buscarAgendamentosPorId(Long id);
    void deletarAgendamento(Long id);
}


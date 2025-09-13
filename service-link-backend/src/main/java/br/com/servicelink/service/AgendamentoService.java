package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;
import br.com.servicelink.entity.Agendamento;

public interface AgendamentoService {
    Agendamento salvarAgendamento(Agendamento agendamento);
    List<Agendamento> listarAgendamentos();
    Optional<Agendamento> buscarAgendamentosPorId(Long id);
    void deletarAgendamento(Long id);
}


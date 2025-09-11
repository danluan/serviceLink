package br.com.servicelink.service;

import java.util.List;
import java.util.Optional;
import br.com.servicelink.entity.Agendamento;

public interface AgendamentoService {
    Agendamento salvar(Agendamento agendamento);
    List<Agendamento> buscarTodos();
    Optional<Agendamento> buscarPorId(Long id);
    void deletar(Long id);
}


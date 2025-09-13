package br.com.servicelink.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.servicelink.entity.Agendamento;
import br.com.servicelink.repository.AgendamentoRepository;
import br.com.servicelink.service.AgendamentoService;

public class AgendamentoSerivceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    @Autowired
    public AgendamentoSerivceImpl(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @Override
    public Agendamento salvarAgendamento(Agendamento agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    @Override
    public List<Agendamento> listarAgendamentos() {
        return agendamentoRepository.findAll();
    }

    @Override
    public java.util.Optional<Agendamento> buscarAgendamentosPorId(Long id) {
        return agendamentoRepository.findById(id);
    }

    @Override
    public void deletarAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }
}

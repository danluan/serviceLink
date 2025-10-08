package br.com.servicelink.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.servicelink.DTO.AgendamentoDTO;
import br.com.servicelink.DTO.AgendamentoListagemDTO;
import br.com.servicelink.entity.Cliente;
import br.com.servicelink.entity.Servico;
import br.com.servicelink.enumerations.Status;
import br.com.servicelink.repository.ClienteRepository;
import br.com.servicelink.repository.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.servicelink.entity.Agendamento;
import br.com.servicelink.repository.AgendamentoRepository;
import br.com.servicelink.service.AgendamentoService;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    @Autowired
    public AgendamentoServiceImpl(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;


    @Transactional
    @Override
    public Agendamento salvarAgendamento(AgendamentoDTO agendamentoDTO) {
        Cliente cliente = clienteRepository.findById(agendamentoDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + agendamentoDTO.getClienteId()));

        Servico servico = servicoRepository.findById(agendamentoDTO.getServicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + agendamentoDTO.getServicoId()));

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(agendamentoDTO.getDataHora());
        agendamento.setObservacao(agendamentoDTO.getObservacao());

        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setStatus(Status.PENDENTE);

        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    @Override
    public List<AgendamentoListagemDTO> listarAgendamentos() {
        return agendamentoRepository.findAll().stream()
                .map(agendamento -> new AgendamentoListagemDTO(
                        agendamento.getId(),
                        agendamento.getDataHora(),
                        agendamento.getStatus().toString(),
                        agendamento.getObservacao(),
                        agendamento.getCliente().getId(),
                        agendamento.getCliente().getNome(),
                        agendamento.getServico().getId(),
                        agendamento.getServico().getNome()
                )).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<AgendamentoListagemDTO> listarAgendamentosPorPrestador(Long prestadorId) {

        List<Agendamento> agendamentosDoPrestador = agendamentoRepository.findByServico_Prestador_Id(prestadorId);

        return agendamentosDoPrestador.stream()
                .map(agendamento -> new AgendamentoListagemDTO(
                        agendamento.getId(),
                        agendamento.getDataHora(),
                        agendamento.getStatus().toString(),
                        agendamento.getObservacao(),
                        agendamento.getCliente().getId(),
                        agendamento.getCliente().getNome(),
                        agendamento.getServico().getId(),
                        agendamento.getServico().getNome()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AgendamentoListagemDTO> buscarAgendamentosPorId(Long id) {
        return agendamentoRepository.findById(id)
                .map(agendamento -> new AgendamentoListagemDTO(
                        agendamento.getId(),
                        agendamento.getDataHora(),
                        agendamento.getStatus().toString(),
                        agendamento.getObservacao(),
                        agendamento.getCliente().getId(),
                        agendamento.getCliente().getNome(),
                        agendamento.getServico().getId(),
                        agendamento.getServico().getNome()
                ));
    }

    @Override
    public void deletarAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }
}

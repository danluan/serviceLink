package br.com.servicelink.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import br.com.servicelink.DTO.AgendamentoDTO;
import br.com.servicelink.DTO.AgendamentoListagemDTO;
import br.com.servicelink.entity.Avaliacao;
import br.com.servicelink.entity.Cliente;
import br.com.servicelink.entity.Servico;
import br.com.servicelink.enumerations.AgendamentoStatus;
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
        agendamento.setStatus(AgendamentoStatus.PENDENTE);

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
                        agendamento.getCliente().getUser().getNome(),
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
                        agendamento.getCliente().getUser().getNome(),
                        agendamento.getServico().getId(),
                        agendamento.getServico().getNome()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public AgendamentoListagemDTO buscarAgendamentosPorId(Long id) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado: " + id));
        AgendamentoListagemDTO dto = new AgendamentoListagemDTO(agendamento);

        return new AgendamentoListagemDTO(agendamento);
    }

    @Override
    public void deletarAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }

    @Override
    public Avaliacao avaliacaoPorAgendamentoId(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));

        return agendamento.getAvaliacao();
    }

    @Override
    public void adicionarAvaliacaoAoAgendamento(Long id, Avaliacao avaliacao) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));

        agendamento.setAvaliacao(avaliacao);
        agendamentoRepository.save(agendamento);
    }
}

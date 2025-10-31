package br.com.servicelink.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.servicelink.DTO.AgendamentoDTO;
import br.com.servicelink.DTO.AgendamentoListagemDTO;
import br.com.servicelink.DTO.AvaliacaoDTO;
import br.com.servicelink.entity.Agendamento;
import br.com.servicelink.entity.Avaliacao;
import br.com.servicelink.enumerations.AgendamentoStatus;
import org.apache.coyote.BadRequestException;

public interface AgendamentoService {
    AgendamentoDTO salvarAgendamento(AgendamentoDTO agendamentoDTO);
    AgendamentoDTO editarAgendamento(AgendamentoDTO agendamentoDTO, Long id) throws BadRequestException;
    List<AgendamentoListagemDTO> listarAgendamentos();
    AgendamentoListagemDTO buscarAgendamentosPorId(Long id);
    void deletarAgendamento(Long id);
    List<AgendamentoListagemDTO> listarAgendamentosPorPrestador(Long prestadorId);
    AvaliacaoDTO avaliacaoPorAgendamentoId(Long id);
    AgendamentoDTO adicionarAvaliacaoAoAgendamento(Long id, AvaliacaoDTO avaliacaoDTO);
    List<AgendamentoListagemDTO> listarAgendamentosDoDia(Long prestadorId);
    List<AgendamentoListagemDTO> listarProximos5Agendamentos(Long prestadorId);
    BigDecimal calcularFaturamentoMensal(Long prestadorId, int ano, int mes);
    Map<Integer, List<AgendamentoListagemDTO>> buscarAgendamentosPorMes(Long prestadorId, int ano, int mes);
    public List<AgendamentoListagemDTO> listarAgendamentosPorCliente(Long clienteId);
    AgendamentoDTO editarStatusAgendamento(Long id, AgendamentoStatus status) throws BadRequestException;
}


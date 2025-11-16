package br.com.serviceframework.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.com.serviceframework.DTO.*;
import br.com.serviceframework.entity.*;
import br.com.serviceframework.enumerations.AgendamentoStatus;
import br.com.serviceframework.repository.AgendamentoRepository;
import br.com.serviceframework.repository.AvaliacaoRepository;
import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.repository.ServicoRepository;
import br.com.serviceframework.service.AgendamentoService;
import br.com.serviceframework.service.auth.AuthService;
import br.com.serviceframework.service.validator.AgendamentoValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.serviceframework.enumerations.AgendamentoStatus.*;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;
    private final AgendamentoValidator agendamentoValidator;

    private static final AgendamentoStatus STATUS = CONCLUIDO;
    private final AuthService authService;

    @Autowired
    public AgendamentoServiceImpl(
            AgendamentoRepository agendamentoRepository,
            AvaliacaoRepository avaliacaoRepository,
            ClienteRepository clienteRepository,
            ServicoRepository servicoRepository,
            AgendamentoValidator agendamentoValidator, AuthService authService) {
        this.agendamentoRepository = agendamentoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.clienteRepository = clienteRepository;
        this.servicoRepository = servicoRepository;
        this.agendamentoValidator = agendamentoValidator;
        this.authService = authService;
    }

    @Transactional
    @Override
    public AgendamentoDTO salvarAgendamento(AgendamentoDTO agendamentoDTO) {

        Long usuarioId = agendamentoDTO.clienteId();

        Cliente cliente = clienteRepository.findByUserId(usuarioId);

        if (cliente == null) {
            throw new EntityNotFoundException("Cliente não encontrado para o Usuário com ID: " + usuarioId);
        }

        Servico servico = servicoRepository.findById(agendamentoDTO.servicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + agendamentoDTO.servicoId()));

        agendamentoValidator.validarNovoAgendamento(agendamentoDTO, servico);

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(agendamentoDTO.dataHora());
        agendamento.setObservacao(agendamentoDTO.observacao());

        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setStatus(AgendamentoStatus.PENDENTE);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        AgendamentoDTO dto = new AgendamentoDTO(
                agendamentoSalvo.getId(),
                agendamentoSalvo.getDataHora(),
                agendamentoSalvo.getObservacao(),
                agendamentoSalvo.getStatus().toString(),
                agendamentoSalvo.getCliente().getUser().getId(),
                agendamentoSalvo.getServico().getId(),
                null
        );
        return dto;
    }

    @Override
    public AgendamentoDTO editarAgendamento(AgendamentoDTO agendamentoDTO, Long id) throws BadRequestException {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));

        agendamento.setStatus(AgendamentoStatus.valueOf(agendamentoDTO.status()));
        agendamento.setDataHora(agendamentoDTO.dataHora());
        agendamento.setObservacao(agendamentoDTO.observacao());

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        return new AgendamentoDTO(
                agendamentoSalvo.getId(),
                agendamentoSalvo.getDataHora(),
                agendamentoSalvo.getObservacao(),
                agendamentoSalvo.getStatus().toString(),
                agendamentoSalvo.getCliente().getUser().getId(),
                agendamentoSalvo.getServico().getId(),
                null
        );
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
                        agendamento.getServico().getNome(),
                        agendamento.getServico().getPrestador().getUser().getNome()
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
                        agendamento.getServico().getNome(),
                        agendamento.getServico().getPrestador().getUser().getNome()
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
    public AvaliacaoDTO avaliacaoPorAgendamentoId(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));

        try {
            Avaliacao avaliacao = agendamento.getAvaliacao();
            AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO(
                    avaliacao.getId(),
                    avaliacao.getEstrelas(),
                    avaliacao.getComentario());
            return avaliacaoDTO;
        } catch (Exception e) {
            throw new EntityNotFoundException("Avaliação não encontrada para o Agendamento com o ID: " + id);
        }
    }

    @Override
    public AgendamentoDTO adicionarAvaliacaoAoAgendamento(Long agendamentoId, AvaliacaoDTO avaliacaoDTO) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + agendamentoId));

        if (agendamento.getAvaliacao() == null) {
            throw new RuntimeException("Este agendamento já foi avaliado.");
        }

        if (agendamento.getStatus() != CONCLUIDO) {
            throw new RuntimeException("Só é possível avaliar agendamentos concluídos.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setEstrelas(avaliacaoDTO.estrelas());
        avaliacao.setComentario(avaliacaoDTO.comentario());

        avaliacao = avaliacaoRepository.save(avaliacao);

        agendamento.setAvaliacao(avaliacao);
        agendamentoRepository.save(agendamento);

        return new AgendamentoDTO(
                agendamento.getId(),
                agendamento.getDataHora(),
                agendamento.getObservacao(),
                agendamento.getStatus().toString(),
                agendamento.getCliente().getId(),
                agendamento.getServico().getId(),
                new AvaliacaoDTO(
                        agendamento.getAvaliacao().getId(),
                        agendamento.getAvaliacao().getEstrelas(),
                        agendamento.getAvaliacao().getComentario()
                )
        );
    }

    @Transactional
    @Override
    public List<AgendamentoListagemDTO> listarAgendamentosDoDia(Long prestadorId) {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Agendamento> agendamentosDoPrestador = agendamentoRepository.findByServicoPrestadorIdAndDataHoraBetween(
                prestadorId,
                startOfDay,
                endOfDay
        );

        return agendamentosDoPrestador.stream()
                .map(agendamento -> new AgendamentoListagemDTO(
                        agendamento.getId(),
                        agendamento.getDataHora(),
                        agendamento.getStatus().toString(),
                        agendamento.getObservacao(),
                        agendamento.getCliente().getId(),
                        agendamento.getCliente().getUser().getNome(),
                        agendamento.getServico().getId(),
                        agendamento.getServico().getNome(),
                        agendamento.getServico().getPrestador().getUser().getNome()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<AgendamentoListagemDTO> listarProximos5Agendamentos(Long prestadorId) {

        LocalDateTime now = LocalDateTime.now();

        List<Agendamento> agendamentosFuturos = agendamentoRepository.findTop5ByServicoPrestadorIdAndDataHoraAfterOrderByDataHoraAsc(
                prestadorId,
                now
        );

        return agendamentosFuturos.stream()
                .map(agendamento -> new AgendamentoListagemDTO(
                        agendamento.getId(),
                        agendamento.getDataHora(),
                        agendamento.getStatus().toString(),
                        agendamento.getObservacao(),
                        agendamento.getCliente().getId(),
                        agendamento.getCliente().getUser().getNome(),
                        agendamento.getServico().getId(),
                        agendamento.getServico().getNome(),
                        agendamento.getServico().getPrestador().getUser().getNome()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calcularFaturamentoMensal(Long prestadorId, int ano, int mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);

        LocalDate primeiroDiaDoMes = yearMonth.atDay(1);
        LocalDateTime dataInicio = primeiroDiaDoMes.atStartOfDay();

        LocalDate ultimoDiaDoMes = yearMonth.atEndOfMonth();
        LocalDateTime dataFim = ultimoDiaDoMes.atTime(23, 59, 59, 999999999);


        BigDecimal faturamento = agendamentoRepository.calcularFaturamentoPorPeriodo(
                prestadorId,
                dataInicio,
                dataFim,
                CONCLUIDO
        );

        return faturamento != null ? faturamento : BigDecimal.ZERO;
    }

    @Transactional
    @Override
    public Map<Integer, List<AgendamentoListagemDTO>> buscarAgendamentosPorMes(Long prestadorId, int ano, int mes) {

        YearMonth yearMonth = YearMonth.of(ano, mes);

        LocalDateTime dataInicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime dataFim = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        List<Agendamento> agendamentos = agendamentoRepository.findByServicoPrestadorIdAndDataHoraBetween(
                prestadorId,
                dataInicio,
                dataFim
        );

        return agendamentos.stream()
                .map(agendamento -> new AgendamentoListagemDTO(
                        agendamento.getId(),
                        agendamento.getDataHora(),
                        agendamento.getStatus().toString(),
                        agendamento.getObservacao(),
                        agendamento.getCliente().getId(),
                        agendamento.getCliente().getUser().getNome(),
                        agendamento.getServico().getId(),
                        agendamento.getServico().getNome(),
                        agendamento.getServico().getPrestador().getUser().getNome()
                ))
                .collect(Collectors.groupingBy(
                        dto -> dto.getDataHora().getDayOfMonth(),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    public List<AgendamentoListagemDTO> listarAgendamentosPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId)
                .stream()
                .map(AgendamentoListagemDTO::new)
                .toList();
    }


    @Transactional
    @Override
    public AgendamentoDTO editarStatusAgendamento(Long id, AgendamentoStatus status) throws BadRequestException {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));

        validarTransicaoStatus(status, agendamento);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        return new AgendamentoDTO(
                agendamentoSalvo.getId(),
                agendamentoSalvo.getDataHora(),
                agendamentoSalvo.getObservacao(),
                agendamentoSalvo.getStatus().toString(),
                agendamentoSalvo.getCliente().getUser().getId(),
                agendamentoSalvo.getServico().getId(),
                null
        );
    }

    private static void validarTransicaoStatus(AgendamentoStatus status, Agendamento agendamento) throws BadRequestException {
        if (agendamento.getStatus().equals(CONCLUIDO) || agendamento.getStatus().equals(CANCELADO)) {
            throw new BadRequestException(
                    "Não é possível alterar o status de um agendamento que já está '" + agendamento.getStatus() + "' (finalizado)."
            );
        }

        if (agendamento.getStatus().equals(status)) {
            throw new BadRequestException(
                    "O status da requisição ('" + status + "') é o mesmo do status atual do agendamento."
            );
        }

        if (agendamento.getStatus().equals(PENDENTE) && status.equals(CONFIRMADO)) {
            agendamento.setStatus(CONFIRMADO);
        }
        else if (agendamento.getStatus().equals(PENDENTE) && status.equals(CANCELADO)) {
            agendamento.setStatus(CANCELADO);
        }
        else if (agendamento.getStatus().equals(CONFIRMADO) && status.equals(CONCLUIDO)) {
            if(agendamento.getDataHora().isBefore(LocalDateTime.now())) {
                agendamento.setStatus(CONCLUIDO);
            }else throw new BadRequestException("Não é possivel concluir um agendamento que ainda não aconteceu");
        }
        else {
            throw new BadRequestException(
                    "Transição de status inválida: De '" + agendamento.getStatus() + "' para '" + status + "' não é permitida."
            );
        }
    }
}

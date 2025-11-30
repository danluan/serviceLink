package br.com.servicelink.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import br.com.serviceframework.domain.DTO.AvaliacaoDTO;
import br.com.serviceframework.domain.entity.Agendamento;
import br.com.serviceframework.domain.entity.Avaliacao;
import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.Servico;
import br.com.serviceframework.domain.interfaces.AgendamentoStatus;
import br.com.servicelink.enumerations.AgendamentoStatusServiceLink;
import br.com.serviceframework.serviceLink.repository.AgendamentoServiceLinkRepository;
import br.com.serviceframework.repository.AvaliacaoRepository;
import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.repository.ServicoRepository;
import br.com.serviceframework.service.AbstractAgendamentoService;
import br.com.serviceframework.service.auth.AuthService;
import br.com.serviceframework.serviceLink.service.validator.AgendamentoValidator;
import br.com.servicelink.domain.DTO.AgendamentoDTO;
import br.com.servicelink.domain.DTO.AgendamentoListagemDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.servicelink.domain.entity.AgendamentoServiceLink;

@Service
public class AgendamentoServiceImpl
        extends AbstractAgendamentoService<AgendamentoServiceLink> {

    private final AgendamentoServiceLinkRepository agendamentoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;
    private final AgendamentoValidator agendamentoValidator;

    private final AuthService authService;

    @Autowired
    public AgendamentoServiceImpl(
            AgendamentoServiceLinkRepository agendamentoRepository,
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

    @Override
    protected void validarRegrasDeNegocio(AgendamentoServiceLink agendamento) {}

    @Override
    protected AgendamentoStatus getStatusInicial() { return AgendamentoStatusServiceLink.PENDENTE; }

    @Override
    protected void calcularPreco(AgendamentoServiceLink agendamento) {}

    @Override
    protected AgendamentoServiceLink salvarNoRepositorio(AgendamentoServiceLink agendamento) { return agendamentoRepository.save(agendamento); }
}

    @Transactional
    public AgendamentoDTO salvarAgendamento(AgendamentoDTO agendamentoDTO) {
        Long usuarioId = agendamentoDTO.clienteId();
        Cliente cliente = clienteRepository.findByUserId(usuarioId);

        if (cliente == null) {
            throw new EntityNotFoundException("Cliente não encontrado para o Usuário com ID: " + usuarioId);
        }

        Servico servico = servicoRepository.findById(agendamentoDTO.servicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + agendamentoDTO.servicoId()));

        agendamentoValidator.validarNovoAgendamento(agendamentoDTO, servico);

        AgendamentoServiceLink agendamento = new AgendamentoServiceLink();
        agendamento.setDataHora(agendamentoDTO.dataHora());
        agendamento.setObservacao(agendamentoDTO.observacao());
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setStatus(AgendamentoStatusServiceLink.PENDENTE);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        AgendamentoServiceLink agendamentoSalvo = super.criarAgendamento(agendamento);
    }

    @Override
    public AgendamentoDTO editarAgendamento(AgendamentoDTO agendamentoDTO, Long id) throws BadRequestException {
        AgendamentoServiceLink agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));

        agendamento.setStatus(AgendamentoStatusServiceLink.valueOf(agendamentoDTO.status()));
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

        List<AgendamentoServiceLink> agendamentosDoPrestador = agendamentoRepository.findByServico_Prestador_Id(prestadorId);

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
        AgendamentoServiceLink agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + agendamentoId));

        if (agendamento.getAvaliacao() == null) {
            throw new RuntimeException("Este agendamento já foi avaliado.");
        }

        if (agendamento.getStatus() != AgendamentoStatusServiceLink.CONCLUIDO) {
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

        List<AgendamentoServiceLink> agendamentosDoPrestador = agendamentoRepository.findByServicoPrestadorIdAndDataHoraBetween(
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

        List<AgendamentoServiceLink> agendamentosFuturos = agendamentoRepository.findTop5ByServicoPrestadorIdAndDataHoraAfterOrderByDataHoraAsc(
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
                AgendamentoStatusServiceLink.CONCLUIDO.getCodigoStatus()
        );

        return faturamento != null ? faturamento : BigDecimal.ZERO;
    }

    @Transactional
    @Override
    public Map<Integer, List<AgendamentoListagemDTO>> buscarAgendamentosPorMes(Long prestadorId, int ano, int mes) {

        YearMonth yearMonth = YearMonth.of(ano, mes);

        LocalDateTime dataInicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime dataFim = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        List<AgendamentoServiceLink> agendamentos = agendamentoRepository.findByServicoPrestadorIdAndDataHoraBetween(
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
    public AgendamentoDTO editarStatusAgendamento(Long id, AgendamentoStatusServiceLink status) throws BadRequestException {
        AgendamentoServiceLink agendamento = agendamentoRepository.findById(id)
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

    private static void validarTransicaoStatus(AgendamentoStatusServiceLink status, Agendamento agendamento) throws BadRequestException {
        if (agendamento.getStatus().equals(AgendamentoStatusServiceLink.CONCLUIDO) || agendamento.getStatus().equals(AgendamentoStatusServiceLink.CANCELADO)) {
            throw new BadRequestException(
                    "Não é possível alterar o status de um agendamento que já está '" + agendamento.getStatus() + "' (finalizado)."
            );
        }

        if (agendamento.getStatus().equals(status)) {
            throw new BadRequestException(
                    "O status da requisição ('" + status + "') é o mesmo do status atual do agendamento."
            );
        }

        if (agendamento.getStatus().equals(AgendamentoStatusServiceLink.PENDENTE) && status.equals(AgendamentoStatusServiceLink.CONFIRMADO)) {
            agendamento.setStatus(AgendamentoStatusServiceLink.CONFIRMADO);
        }
        else if (agendamento.getStatus().equals(AgendamentoStatusServiceLink.PENDENTE) && status.equals(AgendamentoStatusServiceLink.CANCELADO)) {
            agendamento.setStatus(AgendamentoStatusServiceLink.CANCELADO);
        }
        else if (agendamento.getStatus().equals(AgendamentoStatusServiceLink.CONFIRMADO) && status.equals(AgendamentoStatusServiceLink.CONCLUIDO)) {
            if(agendamento.getDataHora().isBefore(LocalDateTime.now())) {
                agendamento.setStatus(AgendamentoStatusServiceLink.CONCLUIDO);
            }else throw new BadRequestException("Não é possivel concluir um agendamento que ainda não aconteceu");
        }
        else {
            throw new BadRequestException(
                    "Transição de status inválida: De '" + agendamento.getStatus() + "' para '" + status + "' não é permitida."
            );
        }
    }

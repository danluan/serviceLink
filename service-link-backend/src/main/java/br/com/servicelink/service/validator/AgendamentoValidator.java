package br.com.servicelink.service.validator;

import br.com.servicelink.DTO.AgendamentoDTO;
import br.com.servicelink.entity.Agendamento;
import br.com.servicelink.entity.Servico;
import br.com.servicelink.exceptions.BusinessException;
import br.com.servicelink.repository.AgendamentoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AgendamentoValidator {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoValidator(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    /**
     * Valida os dados de um novo agendamento antes de salvá-lo.
     *
     * @param agendamentoDTO os dados do agendamento a ser validado
     * @param servico o serviço relacionado ao agendamento
     * @throws BusinessException se alguma validação falhar
     */
    public void validarNovoAgendamento(AgendamentoDTO agendamentoDTO, Servico servico) {
        validarDataHora(agendamentoDTO.dataHora());
        validarDisponibilidadePrestador(servico.getPrestador().getId(), agendamentoDTO.dataHora());
        validarDadosObrigatorios(agendamentoDTO);
    }

    /**
     * Valida se a data/hora do agendamento é válida (não está no passado).
     */
    private void validarDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new BusinessException("A data e hora do agendamento são obrigatórias.");
        }

        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível agendar um serviço para uma data no passado.");
        }
    }

    /**
     * Valida se o prestador já tem um agendamento no intervalo de 30 minutos.
     */
    private void validarDisponibilidadePrestador(Long prestadorId, LocalDateTime dataHora) {
        LocalDateTime inicioJanela = dataHora.minusMinutes(30);
        LocalDateTime fimJanela = dataHora.plusMinutes(30);

        List<Agendamento> agendamentosConflitantes = agendamentoRepository
                .findByServicoPrestadorIdAndDataHoraBetween(prestadorId, inicioJanela, fimJanela);

        if (!agendamentosConflitantes.isEmpty()) {
            throw new BusinessException(
                    "O prestador já possui um agendamento próximo a esse horário. " +
                    "Por favor, escolha outro horário."
            );
        }
    }

    /**
     * Valida se todos os dados obrigatórios estão presentes.
     */
    private void validarDadosObrigatorios(AgendamentoDTO agendamentoDTO) {
        if (agendamentoDTO.clienteId() == null) {
            throw new BusinessException("O ID do cliente é obrigatório.");
        }

        if (agendamentoDTO.servicoId() == null) {
            throw new BusinessException("O ID do serviço é obrigatório.");
        }
    }
}


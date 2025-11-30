package br.com.serviceframework.serviceLink.repository;

import br.com.serviceframework.framework.domain.entity.Agendamento;
import br.com.servicelink.domain.entity.AgendamentoServiceLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoServiceLinkRepository extends JpaRepository<AgendamentoServiceLink, Long> {
    List<Agendamento> findByDataHora(LocalDateTime data);
    List<Agendamento> findByServico_Prestador_Id(Long prestadorId);
    List<Agendamento> findByClienteId(Long clienteId);

    /**
     * Busca agendamentos por Prestador ID e restringe o intervalo de data/hora.
     * @param prestadorId O ID do Prestador.
     * @param startOfDay O início do dia (00:00:00).
     * @param endOfDay O fim do dia (23:59:59.999...).
     * @return Lista de agendamentos no dia especificado para o prestador.
     */
    List<Agendamento> findByServicoPrestadorIdAndDataHoraBetween(
            Long prestadorId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );


    /**
     * Busca os próximos 5 agendamentos futuros para um prestador específico.
     * @param prestadorId O ID do Prestador.
     * @param now O momento atual (LocalDateTime.now()).
     * @return Uma lista com até 5 agendamentos futuros.
     */
    List<Agendamento> findTop5ByServicoPrestadorIdAndDataHoraAfterOrderByDataHoraAsc(
            Long prestadorId,
            LocalDateTime now
    );

    /**
     * Calcula a soma do precoBase dos serviços concluídos (ou pagos) em um intervalo de datas para um prestador.
     * * @param prestadorId ID do Prestador.
     * @param dataInicio O início do período (ex: primeiro dia do mês às 00:00:00).
     * @param dataFim O fim do período (ex: último dia do mês às 23:59:59.999...).
     * @param statusConcluido O status que indica que o serviço foi concluído/pago (ex: Status.CONCLUIDO).
     * @return O valor total faturado no período.
     */
    @Query("SELECT SUM(s.precoBase) FROM AgendamentoServiceLink a " +
            "JOIN a.servico s " +
            "WHERE s.prestador.id = :prestadorId " +
            "AND a.dataHora BETWEEN :dataInicio AND :dataFim " +
            "AND a.statusCode = :statusConcluido")
    BigDecimal calcularFaturamentoPorPeriodo(
            @Param("prestadorId") Long prestadorId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("statusConcluido") Integer statusConcluido
    );
}

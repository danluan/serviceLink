package br.com.servicelink.repository;

import br.com.servicelink.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByDataHora(LocalDate data);
    List<Agendamento> findByServico_Prestador_Id(Long prestadorId);
}

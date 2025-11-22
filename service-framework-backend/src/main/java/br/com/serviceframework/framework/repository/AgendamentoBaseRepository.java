package br.com.serviceframework.framework.repository;

import br.com.serviceframework.framework.domain.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AgendamentoBaseRepository<T extends Agendamento> extends JpaRepository<T, Long> {

}

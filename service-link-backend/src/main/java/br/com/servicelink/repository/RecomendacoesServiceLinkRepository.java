package br.com.servicelink.repository;

import br.com.serviceframework.domain.entity.RecomendacoesCliente;
import br.com.servicelink.domain.entity.AgendamentoServiceLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecomendacoesServiceLinkRepository extends JpaRepository<RecomendacoesCliente, Long> {

}

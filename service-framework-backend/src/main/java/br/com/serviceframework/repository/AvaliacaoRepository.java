package br.com.serviceframework.framework.repository;

import br.com.serviceframework.framework.domain.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
}

package br.com.serviceframework.framework.repository;

import br.com.serviceframework.framework.domain.entity.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {

    Prestador findByUserId(Long id);
}

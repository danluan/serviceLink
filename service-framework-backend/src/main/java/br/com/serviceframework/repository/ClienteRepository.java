package br.com.serviceframework.framework.repository;

import br.com.serviceframework.framework.domain.entity.Cliente;
import br.com.serviceframework.framework.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByUserId(Long id);

    boolean existsByUser(User user);
}

package br.com.servicelink.repository;

import br.com.servicelink.entity.Cliente;
import br.com.servicelink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByUserId(Long id);

    boolean existsByUser(User user);
}

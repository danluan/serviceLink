package br.com.serviceframework.framework.repository;

import br.com.serviceframework.framework.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //UserDetails findUserDetailsByEmail(String email);
    User findUserDetailsByEmail(String email);
    User findUserByEmail(String email);
    User findUserByCpfCnpj(String cpfCnpj);
}

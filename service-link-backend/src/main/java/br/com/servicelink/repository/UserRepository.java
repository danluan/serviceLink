package br.com.servicelink.repository;

import br.com.servicelink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //UserDetails findUserDetailsByEmail(String email);
    User findUserDetailsByEmail(String email);
    User findUserByEmail(String email);
    User findUserByCpfCnpj(String cpfCnpj);
}

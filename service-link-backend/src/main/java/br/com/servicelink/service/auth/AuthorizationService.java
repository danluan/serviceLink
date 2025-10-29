package br.com.servicelink.service.auth;

import br.com.servicelink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = userRepository.findUserDetailsByEmail(email);

        System.out.println("\n--- DEBUG INÍCIO ---");
        System.out.println("Buscando usuário pelo email: " + email);
        String senhaHash = (user != null) ? user.getPassword() : "USUÁRIO NÃO ENCONTRADO";
        System.out.println("SENHA HASH RETORNADA DO BANCO: " + senhaHash);
        System.out.println("--- DEBUG FIM ---\n");

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado " + email);
        }

        return user;
    }
}

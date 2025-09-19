package br.com.servicelink.service.auth;

import br.com.servicelink.DTO.*;
import br.com.servicelink.entity.User;
import br.com.servicelink.repository.UserRepository;
import br.com.servicelink.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDTO login(AuthDTO authData) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(authData.getEmail(), authData.getSenha());
            var auth = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            User user = userRepository.findUserByEmail(authData.getEmail());
            UserDTO userDTO = new UserDTO(user);

            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setToken(token);
            authResponseDTO.setUser(userDTO);

            return authResponseDTO;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos.");
        }
    }


    public UserDTO registerUser(UserRegisterDTO registerData) {
        User user = registerData.toEntity();
        user = this.validateUser(user);
        if(userRepository.findUserDetailsByEmail(user.getEmail()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já está em uso.");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getSenha());
        user.setSenha(encryptedPassword);

        userRepository.save(user);

        return new UserDTO(user);
    }

    private User validateUser(User user) {
        if (user.getNome() == null || user.getNome().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome é obrigatório.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email é obrigatório.");
        }
        if (user.getSenha() == null || user.getSenha().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória.");
        }
        if (user.getSenha().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha deve ter no mínimo 6 caracteres.");
        }
        if (userRepository.findUserByEmail(user.getEmail()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já está em uso.");
        }
        if (user.getCpfCnpj() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF ou CNPJ é obrigatório.");
        }
        if (user.getCpfCnpj().length() >= 11 && user.getCpfCnpj().length() >= 14) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF ou CNPJ inválido.");
        }
        if (userRepository.findUserByCpfCnpj(user.getCpfCnpj()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF ou CNPJ já está em uso.");
        }
        return user;
    }
}

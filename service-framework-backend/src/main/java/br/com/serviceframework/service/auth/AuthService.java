package br.com.serviceframework.service.auth;

import br.com.serviceframework.domain.DTO.*;
import br.com.serviceframework.domain.entity.User;
import br.com.serviceframework.framework.domain.DTO.UserRegisterDTO;
import br.com.serviceframework.repository.UserRepository;
import br.com.serviceframework.service.ClienteService;
import br.com.serviceframework.service.PrestadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponseDTO login(AuthDTO authData) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(
                    authData.email(),
                    authData.senha()
            );

            var auth = authenticationManager.authenticate(authToken);

            User user = (User) auth.getPrincipal();

            String token = tokenService.generateToken(user);

            UserDTO userDTO = new UserDTO(user);

            if (user.getPerfil() == Perfis.PRESTADOR) {
                Long prestadorId = prestadorService.getPrestadorIdByUserId(user.getId());
                userDTO.setProfileId(prestadorId);
            } else if (user.getPerfil() == Perfis.CLIENTE) {
                Long clienteId = clienteService.getClienteIdByUserId(user.getId());
                userDTO.setProfileId(clienteId);
            }

            return new AuthResponseDTO(userDTO, token);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos.");
        }
    }

    public UserDTO registerUser(UserRegisterDTO registerData) {
        User user = registerData.toEntity();

        validateNewUser(user);

        String encryptedPassword = passwordEncoder.encode(user.getSenha());
        user.setSenha(encryptedPassword);

        userRepository.save(user);

        UserDTO userDTO = new UserDTO(user);

        if (user.getPerfil() == Perfis.PRESTADOR) {
            Prestador prestador = prestadorService.salvarPrestador(user);
            userDTO.setProfileId(prestador.getId());
        }
        else if (user.getPerfil() == Perfis.CLIENTE) {
            Cliente cliente = clienteService.salvarCliente(user);
            userDTO.setProfileId(cliente.getId());
        }

        return userDTO;
    }

    public UserDTO updateUser(User user) {
        validateExistingUser(user);

        String encryptedPassword = passwordEncoder.encode(user.getSenha());
        user.setSenha(encryptedPassword);

        userRepository.save(user);

        UserDTO userDTO = new UserDTO(user);

        if (user.getPerfil() == Perfis.PRESTADOR) {
            Long prestadorId = prestadorService.getPrestadorIdByUserId(user.getId());
            userDTO.setProfileId(prestadorId);
        }
        else if (user.getPerfil() == Perfis.CLIENTE) {
            Long clienteId = clienteService.getClienteIdByUserId(user.getId());
            userDTO.setProfileId(clienteId);
        }

        return userDTO;
    }

    private void validateNewUser(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserName é obrigatório.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email é obrigatório.");
        }
        if (user.getSenha() == null || user.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória.");
        }
        if (user.getSenha().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha deve ter no mínimo 6 caracteres.");
        }
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já está em uso.");
        }
    }

    private void validateExistingUser(User user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente.");
        }
        if (user.getSenha() == null || user.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória.");
        }
        if (user.getSenha().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha deve ter no mínimo 6 caracteres.");
        }
    }
}

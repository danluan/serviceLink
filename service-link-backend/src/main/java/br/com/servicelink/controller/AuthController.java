package br.com.servicelink.controller;

import br.com.servicelink.DTO.*;
import br.com.servicelink.entity.User;
import br.com.servicelink.security.TokenService;
import br.com.servicelink.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * Realiza a autenticação de um usuário.
     *
     * @param authData Dados de autenticação (email e senha)
     * @return Resposta com token de autenticação
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody  AuthDTO authData) {


        AuthResponseDTO loginResponse = authService.login(authData);

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Registra um novo usuário no sistema.
     *
     * @param userRegisterData Dados de cadastro do novo usuário
     * @return Dados do usuário cadastrado
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserRegisterDTO userRegisterData) {
        UserDTO userDTO = authService.registerUser(userRegisterData);

        return ResponseEntity.ok(userDTO);
    }
}

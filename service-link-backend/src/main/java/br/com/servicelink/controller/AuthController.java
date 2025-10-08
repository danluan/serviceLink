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

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody  AuthDTO authData) {

        AuthResponseDTO loginResponse = authService.login(authData);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserRegisterDTO userRegisterData) {
        UserDTO userDTO = authService.registerUser(userRegisterData);

        return ResponseEntity.ok(userDTO);
    }
}

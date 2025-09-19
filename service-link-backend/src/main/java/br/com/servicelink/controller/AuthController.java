package br.com.servicelink.controller;

import br.com.servicelink.DTO.AuthDTO;
import br.com.servicelink.DTO.LoginResponseDTO;
import br.com.servicelink.DTO.RegisterDTO;
import br.com.servicelink.entity.User;
import br.com.servicelink.repository.UserRepository;
import br.com.servicelink.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody  AuthDTO authData) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authData.email(), authData.senha());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerData) {
        if(userRepository.findByEmail(registerData.email()) != null){
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerData.senha());

        User user = new User(registerData.email(), encryptedPassword, registerData.perfil());

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}

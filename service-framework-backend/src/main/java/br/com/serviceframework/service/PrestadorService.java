package br.com.serviceframework.service;

import br.com.serviceframework.domain.DTO.PrestadorDTO;
import br.com.serviceframework.domain.entity.Perfil;
import br.com.serviceframework.domain.entity.Prestador;
import br.com.serviceframework.domain.entity.User;
import br.com.serviceframework.repository.PrestadorRepository;
import br.com.serviceframework.repository.UserRepository;
import br.com.serviceframework.framework.service.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestadorService{

    private static final Logger log = LoggerFactory.getLogger(PrestadorService.class);
    private final PrestadorRepository prestadorRepository;

    private AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public PrestadorService(PrestadorRepository prestadorRepository) {
        this.prestadorRepository = prestadorRepository;
    }


    public Prestador salvarPrestador(User user) {
        Prestador prestador = new Prestador();
        //prestador.setPerfilPrestador(new Perfil());
        prestador.setUser(user);

        return prestadorRepository.save(prestador);
    }

    public List<PrestadorDTO> listarPrestadores() {
        List<Prestador> prestadores = prestadorRepository.findAll();

        return prestadores.stream().map(prestador -> new PrestadorDTO(
                prestador.getId(),
                prestador.getUser().isActive(),
                prestador.getUser().getId(),
                prestador.getUser().getUsername(),
                prestador.getUser().getEmail(),
                prestador.getUser().getTelefone(),
                prestador.getUser().getCpfCnpj(),
                prestador.getPerfilPrestador().getBiografia()
        )).toList();
    }


    public PrestadorDTO buscarPrestadorPorId(Long id) {
        Prestador prestador = prestadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prestador não encontrado com o ID: " + id));

        System.out.println(prestador);

        return new PrestadorDTO(prestador.getId(),
                prestador.getUser().isActive(),
                prestador.getUser().getId(),
                prestador.getUser().getUsername(),
                prestador.getUser().getEmail(),
                prestador.getUser().getTelefone(),
                prestador.getUser().getCpfCnpj(),
                prestador.getPerfilPrestador().getBiografia());
    }


    public void deletarPrestador(Long id) {
        Prestador prestador = prestadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prestador não encontrado com o ID: " + id));

        User user = prestador.getUser();
        user.setActive(false);
         userRepository.save(user);
    }

    public Long getPrestadorIdByUserId(Long id) {
        Prestador prestador = prestadorRepository.findByUserId(id);

        return prestador.getId();
    }
}
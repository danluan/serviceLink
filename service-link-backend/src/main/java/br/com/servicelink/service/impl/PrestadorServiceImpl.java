package br.com.servicelink.service.impl;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.PrestadorCadastroDTO;
import br.com.servicelink.DTO.PrestadorDTO;
import br.com.servicelink.DTO.UserDTO;
import br.com.servicelink.DTO.UserRegisterDTO;
import br.com.servicelink.entity.PerfilPrestador;
import br.com.servicelink.entity.User;
import br.com.servicelink.enumerations.Perfis;
import br.com.servicelink.repository.UserRepository;
import br.com.servicelink.service.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import br.com.servicelink.entity.Prestador;
import br.com.servicelink.repository.PrestadorRepository;
import br.com.servicelink.service.PrestadorService;

@Service
public class PrestadorServiceImpl implements PrestadorService {

    private static final Logger log = LoggerFactory.getLogger(PrestadorServiceImpl.class);
    private final PrestadorRepository prestadorRepository;

    private AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public PrestadorServiceImpl(PrestadorRepository prestadorRepository) {
        this.prestadorRepository = prestadorRepository;
    }

    @Override
    public Prestador salvarPrestador(User user) {
        Prestador prestador = new Prestador();
        prestador.setPerfilPrestador(new PerfilPrestador());
        prestador.setUser(user);

        return prestadorRepository.save(prestador);
    }

    @Override
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

    @Override
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

    @Override
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
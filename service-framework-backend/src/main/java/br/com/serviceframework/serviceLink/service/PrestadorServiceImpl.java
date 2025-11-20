package br.com.serviceframework.serviceLink.service;

import java.util.List;

import br.com.serviceframework.framework.domain.DTO.PrestadorDTO;
import br.com.serviceframework.framework.domain.entity.PerfilPrestador;
import br.com.serviceframework.framework.domain.entity.User;
import br.com.serviceframework.framework.repository.UserRepository;
import br.com.serviceframework.framework.service.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serviceframework.framework.domain.entity.Prestador;
import br.com.serviceframework.framework.repository.PrestadorRepository;
import br.com.serviceframework.framework.service.PrestadorService;

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
package br.com.servicelink.service.impl;

import java.util.List;
import java.util.Optional;

import br.com.servicelink.DTO.PrestadorCadastroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.servicelink.entity.Prestador;
import br.com.servicelink.repository.PrestadorRepository;
import br.com.servicelink.service.PrestadorService;

@Service
public class PrestadorServiceImpl implements PrestadorService {

    private final PrestadorRepository prestadorRepository;

    @Autowired
    public PrestadorServiceImpl(PrestadorRepository prestadorRepository) {
        this.prestadorRepository = prestadorRepository;
    }

    @Override
    public Prestador salvarPrestador(PrestadorCadastroDTO prestadorDTO) {
        Prestador prestador = new Prestador();
        prestador.setNome(prestadorDTO.getNome());
        prestador.setEmail(prestadorDTO.getEmail());
        prestador.setTelefone(prestadorDTO.getTelefone());
        prestador.setDescricao(prestadorDTO.getDescricao());
        prestador.setDescricao(prestadorDTO.getDescricao());
        prestador.setCpfCnpj(prestadorDTO.getCpfCnpj());

        return prestadorRepository.save(prestador);
    }

    @Override
    public List<Prestador> listarPrestadores() {
        return prestadorRepository.findAll();
    }

    @Override
    public Optional<Prestador> buscarPrestadorPorId(Long id) {
        return prestadorRepository.findById(id);
    }

    @Override
    public void deletarPrestador(Long id) {
        prestadorRepository.deleteById(id);
    }
}

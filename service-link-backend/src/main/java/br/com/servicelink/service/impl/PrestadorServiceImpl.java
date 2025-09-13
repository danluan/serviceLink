package br.com.servicelink.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.servicelink.entity.Prestador;
import br.com.servicelink.repository.PrestadorRepository;
import br.com.servicelink.service.PrestadorSerivce;

@Service
public class PrestadorServiceImpl implements PrestadorSerivce{

    private final PrestadorRepository prestadorRepository;

    @Autowired
    public PrestadorServiceImpl(PrestadorRepository prestadorRepository) {
        this.prestadorRepository = prestadorRepository;
    }

    @Override
    public Prestador salvarPrestador(Prestador prestador) {
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

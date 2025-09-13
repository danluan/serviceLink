package br.com.servicelink.service;

import br.com.servicelink.entity.Prestador;
import java.util.List;
import java.util.Optional;

public interface PrestadorSerivce {
    Prestador salvarPrestador(Prestador prestador);
    List<Prestador> listarPrestadores();
    Optional<Prestador> buscarPrestadorPorId(Long id);
    void deletarPrestador(Long id);
}

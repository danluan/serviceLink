package br.com.servicelink.service;

import br.com.servicelink.entity.Prestador;
import java.util.List;
import java.util.Optional;

public interface PrestadorSerivce {
    Prestador salvar(Prestador prestador);
    List<Prestador> listaPrestadores();
    Optional<Prestador> buscarPorId(Long id);
    void deletar(Long id);
}

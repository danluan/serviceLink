package br.com.serviceframework.service;

import br.com.serviceframework.DTO.PrestadorDTO;
import br.com.serviceframework.entity.Prestador;
import br.com.serviceframework.entity.User;

import java.util.List;

public interface PrestadorService {
    Prestador salvarPrestador(User user);
    List<PrestadorDTO> listarPrestadores();
    PrestadorDTO buscarPrestadorPorId(Long id);
    void deletarPrestador(Long id);
}

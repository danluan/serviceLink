package br.com.servicelink.service;

import br.com.servicelink.DTO.PrestadorCadastroDTO;
import br.com.servicelink.DTO.PrestadorDTO;
import br.com.servicelink.entity.Prestador;
import br.com.servicelink.entity.User;

import java.util.List;
import java.util.Optional;

public interface PrestadorService {
    Prestador salvarPrestador(User user);
    List<PrestadorDTO> listarPrestadores();
    PrestadorDTO buscarPrestadorPorId(Long id);
    void deletarPrestador(Long id);
}

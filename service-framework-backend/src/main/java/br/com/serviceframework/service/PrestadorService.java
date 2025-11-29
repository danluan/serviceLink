package br.com.serviceframework.framework.service;

import br.com.serviceframework.framework.domain.DTO.PrestadorDTO;
import br.com.serviceframework.framework.domain.entity.Prestador;
import br.com.serviceframework.framework.domain.entity.User;

import java.util.List;

public interface PrestadorService {
    Prestador salvarPrestador(User user);
    List<PrestadorDTO> listarPrestadores();
    PrestadorDTO buscarPrestadorPorId(Long id);
    void deletarPrestador(Long id);
}

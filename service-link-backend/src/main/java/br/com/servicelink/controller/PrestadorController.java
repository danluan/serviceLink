package br.com.servicelink.controller;

import br.com.servicelink.DTO.PrestadorCadastroDTO;
import br.com.servicelink.DTO.PrestadorDTO;
import br.com.servicelink.entity.Prestador;
import br.com.servicelink.service.PrestadorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestador")
public class PrestadorController {
    @Autowired
    private PrestadorService prestadorService;

    /**
     * Lista todos os prestadores cadastrados.
     *
     * @return Lista de prestadores
     */
    @GetMapping
    public List<PrestadorDTO> findAll(){
        return prestadorService.listarPrestadores();
    }

    /**
     * Busca um prestador específico por ID.
     *
     * @param id ID do prestador
     * @return Dados do prestador
     */
    @GetMapping(value = "/{id}")
    public PrestadorDTO findById(@PathVariable Long id){
        try {
            return prestadorService.buscarPrestadorPorId(id);
        } catch (Exception e) {
            if (e instanceof EntityNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
        }
    }

    /**
     * Desativa um prestador.
     *
     * @param id ID do prestador a ser desativado
     */
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        prestadorService.deletarPrestador(id);
    }
}

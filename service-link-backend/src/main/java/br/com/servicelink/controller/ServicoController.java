package br.com.servicelink.controller;

import br.com.servicelink.DTO.ServicoDTO;
import br.com.servicelink.entity.Servico;
import br.com.servicelink.service.ServicoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/servico")
public class ServicoController {
    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public List<Servico> findAll(){
        return servicoService.listarServicos();
    }

    //TODO: Talvez implementar busca com parametros para não precisa de um endpoint específico
    @GetMapping(value = "/{id}")
    public Servico findById(@PathVariable Long id){
        return servicoService.buscarServicoPorId(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        servicoService.deletarServico(id);
    }

    @PostMapping("/prestador/{prestadorId}")
    public List<Servico> addServicos(@PathVariable Long prestadorId, @RequestBody @Valid List<ServicoDTO> servicosDTO) {
        try {
            return servicoService.adicionarServicos(prestadorId, servicosDTO);
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            } else if (e instanceof EntityNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            }
        }
    }

    @PutMapping("/{servicoId}")
    public Servico atualizarServicos(@RequestBody ServicoDTO servicoDTO, @PathVariable Long servicoId) {
        try {
            return servicoService.editarServico(servicoId, servicoDTO);
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            } else if (e instanceof EntityNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            }
        }
    }

    @GetMapping("/categoria")
    public List<ServicoDTO> buscarPorCategoria(@RequestParam String categoria) {
        return servicoService.buscarServicosPorCategoria(categoria);
    }

    @GetMapping("/prestador/{prestadorId}")
    public List<Servico> getServicosPrestador(@PathVariable Long prestadorId) {
        return servicoService.buscarServicosPorPrestadorId(prestadorId);
    }
}

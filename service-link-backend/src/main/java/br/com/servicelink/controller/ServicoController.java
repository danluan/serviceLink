package br.com.servicelink.controller;

import br.com.servicelink.DTO.ServicoDTO;
import br.com.servicelink.entity.Servico;
import br.com.servicelink.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/{id}")
    public Optional<Servico> findById(@PathVariable Long id){
        return servicoService.buscarServicoPorId(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        servicoService.deletarServico(id);
    }

    @PostMapping("/prestador/{prestadorId}")
    public List<Servico> addServicos(@PathVariable Long prestadorId, @RequestBody List<ServicoDTO> servicosDTO) {
        return servicoService.adicionarServicos(prestadorId, servicosDTO);
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

package br.com.servicelink.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.servicelink.DTO.ServicoDTO;
import br.com.servicelink.entity.Prestador;
import br.com.servicelink.repository.PrestadorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.servicelink.entity.Servico;
import br.com.servicelink.repository.ServicoRepository;
import br.com.servicelink.service.ServicoService;

@Service
public class ServicoServiceImpl implements ServicoService {

    private final ServicoRepository servicoRepository;

    @Autowired
    public ServicoServiceImpl(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Override
    @Transactional
    public List<Servico> adicionarServicos(Long prestadorId, List<ServicoDTO> servicosDTO) {

        Prestador prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new EntityNotFoundException("Prestador não encontrado com o ID: " + prestadorId));

        List<Servico> novosServicos = servicosDTO.stream()
                .map(dto -> {
                    Servico servico = new Servico();
                    servico.setNome(dto.getNome());
                    servico.setDescricao(dto.getDescricao());
                    servico.setPrecoBase(dto.getPrecoBase());
                    servico.setCategoria(dto.getCategoria());
                    servico.setImagemUrl(dto.getImagemUrl());

                    servico.setPrestador(prestador);
                    return servico;
                })
                .collect(Collectors.toList());


        return servicoRepository.saveAll(novosServicos);
    }

    public List<ServicoDTO> buscarServicosPorCategoria(String categoria) {

        List<Servico> servicos = servicoRepository.findByCategoria(categoria);

        // Mapeia a lista de entidades para a lista de DTOs
        return servicos.stream()
                .map(servico -> {
                    ServicoDTO dto = new ServicoDTO();
                    dto.setNome(servico.getNome());
                    dto.setDescricao(servico.getDescricao());
                    dto.setPrecoBase(servico.getPrecoBase());
                    dto.setCategoria(servico.getCategoria());
                    dto.setImagemUrl(servico.getImagemUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Servico> listarServicos() {
        return servicoRepository.findAll();
    }

    @Override
    public Optional<Servico> buscarServicoPorId(Long id) {
        return servicoRepository.findById(id);
    }

    @Override
    public void deletarServico(Long id) {
        servicoRepository.deleteById(id);
    }

    @Override
    public List<Servico> buscarServicoPorNome(String nome) {
        return servicoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    public List<Servico> buscarServicosPorPrecoBase(String categoria, String nome) {
        Optional<Servico> servicoMaisCaro = servicoRepository.findTop1ByOrderByPrecoBaseDesc(categoria, nome);
        Optional<Servico> servicoMaisBarato = servicoRepository.findTop1ByOrderByPrecoBaseAsc(categoria, nome);

        List<Servico> servicosEncontrados = new ArrayList<>();
        servicoMaisCaro.ifPresent(servicosEncontrados::add);
        servicoMaisBarato.ifPresent(servicosEncontrados::add);

        return servicosEncontrados;
    }

    @Override
    public List<Servico> buscarServicosPorPrestadorId(Long prestadorId) {
        List<Servico> servicos = servicoRepository.findByPrestadorId(prestadorId);
        return servicos;
    }
}

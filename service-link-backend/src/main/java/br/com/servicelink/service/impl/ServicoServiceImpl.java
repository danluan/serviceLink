package br.com.servicelink.service.impl;

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
}

package br.com.servicelink.service.impl;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Servico salvarServico(Servico servico) {
        return servicoRepository.save(servico);
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

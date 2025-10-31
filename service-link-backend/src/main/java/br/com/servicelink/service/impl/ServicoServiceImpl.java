package br.com.servicelink.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.servicelink.DTO.BuscaServicosDTO;
import br.com.servicelink.DTO.ServicoDTO;
import br.com.servicelink.entity.Prestador;
import br.com.servicelink.repository.PrestadorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
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
    public List<Servico> adicionarServicos(Long prestadorId, List<ServicoDTO> servicosDTO) throws BadRequestException {

        validarListaServicos(servicosDTO);
        validarPrestadorId(prestadorId);

        Prestador prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new EntityNotFoundException("Prestador não encontrado com o ID: " + prestadorId));

        for (ServicoDTO dto : servicosDTO) {
            validarServicoDTO(dto);
        }

        List<Servico> novosServicos = servicosDTO.stream()
                .map(dto -> criarServicoFromDTO(dto, prestador))
                .collect(Collectors.toList());

        return servicoRepository.saveAll(novosServicos);

    }

    @Override
    public Servico editarServico(Long servicoId, ServicoDTO newServicoDTO) throws BadRequestException {
        validarServicoDTO(newServicoDTO);

        Servico servicoAtual = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + servicoId));

        servicoAtual.setNome(newServicoDTO.nome().trim());
        servicoAtual.setDescricao(newServicoDTO.descricao().trim());
        servicoAtual.setPrecoBase(newServicoDTO.precoBase());
        servicoAtual.setCategoria(newServicoDTO.categoria().trim());
        servicoAtual.setImagemUrl(newServicoDTO.imagemUrl());

        return servicoRepository.save(servicoAtual);
    }

    private void validarListaServicos(List<ServicoDTO> servicosDTO) throws BadRequestException {
        if (servicosDTO == null || servicosDTO.isEmpty()) {
            throw new BadRequestException("A lista de serviços não pode estar vazia.");
        }
    }

    private void validarPrestadorId(Long prestadorId) throws BadRequestException {
        if (prestadorId == null || prestadorId <= 0) {
            throw new BadRequestException("ID do prestador inválido.");
        }
    }

    private void validarServicoDTO(ServicoDTO dto) throws BadRequestException {
        if (dto == null) {
            throw new BadRequestException("Serviço não pode ser nulo.");
        }

        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new BadRequestException("Nome do serviço é obrigatório.");
        }

        if (dto.descricao() == null || dto.descricao().trim().isEmpty()) {
            throw new BadRequestException("Descrição do serviço é obrigatória.");
        }

        if (dto.descricao().length() > 300) {
            throw new BadRequestException("Descrição do serviço não pode exceder 300 caracteres.");
        }

        if (dto.precoBase() == null) {
            throw new BadRequestException("Preço base do serviço é obrigatório.");
        }

        if (dto.precoBase().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Preço base deve ser maior que zero.");
        }

        if (dto.categoria() == null || dto.categoria().trim().isEmpty()) {
            throw new BadRequestException("Categoria do serviço é obrigatória.");
        }
    }

    private Servico criarServicoFromDTO(ServicoDTO dto, Prestador prestador) {
        Servico servico = new Servico();
        servico.setNome(dto.nome().trim());
        servico.setDescricao(dto.descricao().trim());
        servico.setPrecoBase(dto.precoBase());
        servico.setCategoria(dto.categoria().trim());
        servico.setImagemUrl(dto.imagemUrl());
        servico.setPrestador(prestador);
        return servico;
    }

    public List<ServicoDTO> buscarServicosPorCategoria(String categoria) {

        List<Servico> servicos = servicoRepository.findByCategoria(categoria);

        // Mapeia a lista de entidades para a lista de DTOs
        return servicos.stream()
                .map(servico -> new ServicoDTO(
                        servico.getId(),
                        servico.getNome(),
                        servico.getDescricao(),
                        servico.getPrecoBase(),
                        servico.getCategoria(),
                        servico.getImagemUrl()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Servico> listarServicos() {
        return servicoRepository.findAll();
    }

    @Override
    public Servico buscarServicoPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + id));
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

    public List<Servico> buscarServico(BuscaServicosDTO servicoDTO) {

        List<Servico> servicos = servicoRepository.buscarServicos(
                servicoDTO.id(),
                servicoDTO.nome(),
                servicoDTO.descricao(),
                servicoDTO.precoMin(),
                servicoDTO.precoMax(),
                servicoDTO.categoria()
        );

        return servicos;
    }
}

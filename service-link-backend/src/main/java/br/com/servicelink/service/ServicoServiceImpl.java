package br.com.servicelink.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.serviceframework.domain.DTO.BuscaServicosDTO;
import br.com.serviceframework.domain.interfaces.ICategoriaServicos;
import br.com.servicelink.domain.DTO.ServicoDTO;
import br.com.serviceframework.domain.entity.Prestador;
import br.com.serviceframework.repository.PrestadorRepository;
import br.com.servicelink.enumerations.CategoriaDomesticas;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serviceframework.domain.entity.Servico;
import br.com.serviceframework.repository.ServicoRepository;
import br.com.serviceframework.service.ServicoService;

@Service
public class ServicoServiceImpl extends ServicoService {

    private final ServicoRepository servicoRepository;

    @Autowired
    public ServicoServiceImpl(ServicoRepository servicoRepository) {
        super(servicoRepository);
        this.servicoRepository = servicoRepository;
    }

    @Autowired
    private PrestadorRepository prestadorRepository;

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
        ICategoriaServicos categoriaDoDominio = CategoriaDomesticas.ofId(newServicoDTO.categoriaId());

        if (categoriaDoDominio == null) {
            throw new IllegalArgumentException("ID de Categoria " + newServicoDTO.categoriaId() + " é inválido para este domínio.");
        }

        validarServicoId(servicoId);

        validarServicoDTO(newServicoDTO);

        Servico servicoAtual = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + servicoId));

        servicoAtual.setNome(newServicoDTO.nome().trim());
        servicoAtual.setDescricao(newServicoDTO.descricao().trim());
        servicoAtual.setPrecoBase(newServicoDTO.precoBase());
        servicoAtual.setCategoria(categoriaDoDominio);
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
    }

    private void validarBuscaServicoDTO(BuscaServicosDTO dto) throws BadRequestException {
        if (dto == null) {
            throw new BadRequestException("Serviço não pode ser nulo.");
        }

        if(dto.precoMax() != null && dto.precoMin() != null){
            if (dto.precoMax().compareTo(dto.precoMin()) < 1) {
                throw new BadRequestException("Preço max deve ser maior que preço min");
            }
        }
        if(dto.precoMax() !=null){
            if(dto.precoMax().compareTo(java.math.BigDecimal.ZERO) <= 0){
                throw new BadRequestException("Preco max deve ser maior que zero");
            }
        }
        if(dto.precoMin() !=null){
            if(dto.precoMin().compareTo(java.math.BigDecimal.ZERO) <= 0){
                throw new BadRequestException("Preco min deve ser maior que zero");
            }
        }
    }

    private Servico criarServicoFromDTO(ServicoDTO dto, Prestador prestador) {

        // **PASSO 1: TRADUÇÃO DO LONG PARA ICATEGORIA**
        // A aplicação usa seu enum específico para resolver o ID.
        ICategoriaServicos categoriaDoDominio = CategoriaDomesticas.ofId(dto.categoriaId());

        if (categoriaDoDominio == null) {
            throw new IllegalArgumentException("ID de Categoria " + dto.categoriaId() + " é inválido para este domínio.");
        }

        // **PASSO 2: MAPEAMENTO**
        Servico servico = new Servico();
        servico.setNome(dto.nome().trim());
        servico.setDescricao(dto.descricao().trim());
        servico.setPrecoBase(dto.precoBase());

        // **SOLUÇÃO:** Agora, o setter do Servico recebe a interface, com o objeto traduzido.
        servico.setCategoria(categoriaDoDominio);

        servico.setPrestador(prestador);

        return servico;
    }

    @Override
    public List<Servico> listarServicos() {
        return servicoRepository.findAll();
    }


    @Override
    public void deletarServico(Long id) {
        servicoRepository.deleteById(id);
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

    @Override
    public List<Servico> buscarServico(BuscaServicosDTO servicoDTO) throws BadRequestException {

        validarBuscaServicoDTO(servicoDTO);

        ICategoriaServicos categoriaDoDominio = CategoriaDomesticas.ofId(servicoDTO.id());

        if (categoriaDoDominio == null) {
            throw new IllegalArgumentException("ID de Categoria " + servicoDTO.id() + " é inválido para este domínio.");
        }

        List<Servico> servicos = servicoRepository.buscarServicos(
                servicoDTO.id(),
                servicoDTO.nome(),
                servicoDTO.descricao(),
                servicoDTO.precoMin(),
                servicoDTO.precoMax(),
                categoriaDoDominio
        );

        return servicos;
    }

    private void validarServicoId(Long servicoId) throws BadRequestException {
        if (servicoId == null || servicoId <= 0) {
            throw new BadRequestException("Id do Serviço inválido");
        }
    }
}

package br.com.servicelink.repository;

import br.com.servicelink.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByNomeContainingIgnoreCase(String nome);
    List<Servico> findByCategoria(String categoria);
    @Query("SELECT s FROM Servico s " +
            "WHERE s.categoria = :categoria AND LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
            "ORDER BY s.precoBase DESC " +
            "LIMIT 1")
    Optional<Servico> findTop1ByOrderByPrecoBaseDesc(@Param("categoria") String categoria, @Param("nome") String nome);

    @Query("SELECT s FROM Servico s " +
            "WHERE s.categoria = :categoria AND LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
            "ORDER BY s.precoBase ASC " +
            "LIMIT 1")
    Optional<Servico> findTop1ByOrderByPrecoBaseAsc(@Param("categoria") String categoria, @Param("nome") String nome);
}

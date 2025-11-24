package br.com.serviceframework.framework.repository;

import br.com.serviceframework.framework.domain.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@FrameworkRepository
public interface AgendamentoBaseRepository<T extends Agendamento>{
    T save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void deleteById(Long id);
}

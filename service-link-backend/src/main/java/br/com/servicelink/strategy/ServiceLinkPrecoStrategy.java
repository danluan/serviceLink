package br.com.servicelink.strategy;

import br.com.serviceframework.strategy.PrecoStrategy;
import br.com.servicelink.domain.entity.AgendamentoServiceLink;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ServiceLinkPrecoStrategy implements PrecoStrategy<AgendamentoServiceLink> {

    @Override
    public BigDecimal calcularPreco(AgendamentoServiceLink agendamento) {
        BigDecimal precoBase = agendamento.getServico().getPrecoBase();

        if (precoBase == null) {
            precoBase = BigDecimal.ZERO;
        }

        BigDecimal custoMateriais = calcularCustoMateriais(agendamento);

        return precoBase.add(custoMateriais);
    }

    public BigDecimal calcularCustoMateriais(AgendamentoServiceLink agendamento) {
        return BigDecimal.ZERO; // logica de materiais
    }
}

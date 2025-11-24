package br.com.serviceframework.serviceLink.domain;

import br.com.serviceframework.framework.domain.entity.Agendamento;
import br.com.serviceframework.framework.domain.interfaces.AgendamentoStatus;
import br.com.serviceframework.serviceLink.enumerations.AgendamentoStatusServiceLink;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "agendamento")
public class AgendamentoServiceLink extends Agendamento {

    @Override
    @Transient
    public AgendamentoStatus getStatus() {
        Integer codigo = this.getCodigoStatus();

        return AgendamentoStatusServiceLink.toEnum(codigo);
    }

    public void setStatus(AgendamentoStatus status) {
        super.setStatus(status);
    }
}

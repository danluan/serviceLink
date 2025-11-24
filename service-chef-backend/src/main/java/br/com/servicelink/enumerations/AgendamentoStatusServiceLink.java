package br.com.serviceframework.serviceLink.enumerations;

import br.com.serviceframework.framework.domain.interfaces.AgendamentoStatus;

public enum AgendamentoStatusServiceLink implements AgendamentoStatus {
    PENDENTE(1, "Pendente"),
    CONFIRMADO(2, "Confirmado"),
    CANCELADO(3, "Cancelado"),
    EM_ANDAMENTO(4, "Em Andamento"),
    CONCLUIDO(5, "Concluído");

    private final int codigo;
    private final String nome;

    AgendamentoStatusServiceLink(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    @Override
    public String getNomeStatus() {
        return nome;
    }

    @Override
    public int getCodigoStatus() {
        return codigo;
    }

    public static AgendamentoStatusServiceLink toEnum(Integer cod) {
        if (cod == null) return null;

        for (AgendamentoStatusServiceLink item : AgendamentoStatusServiceLink.values()) {
            if (cod.equals(item.getCodigoStatus())) return item;
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}

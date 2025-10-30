import { User } from './auth';

export interface Servico {
    id: string;
    nome: string;
    descricao: string;
    precoBase: number;
}

export interface Agendamento {
    id: number;
    dataHora: string;
    status: 'PENDENTE' | 'CONFIRMADO' | 'CONCLUIDO' | 'CANCELADO';
    observacao?: string;
    clienteId: number;
    nomeCliente: string;
    servicoId: number;
    nomeServico: string;
}

export interface AgendamentoRequest {
    clienteId: string;
    servicoId: string;
    dataHora: string;
    observacao?: string;
}
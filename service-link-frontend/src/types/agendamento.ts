import { User } from './auth';

// O que é um Serviço
export interface Servico {
    id: string;
    nome: string;
    descricao: string;
    precoBase: number;
}

// O que é um Agendamento (depois de criado)
export interface Agendamento {
    id: string;
    cliente: User;
    servico: Servico;
    dataHora: string; // Data e hora em formato ISO
    status: 'PENDENTE' | 'CONFIRMADO' | 'CONCLUIDO' | 'CANCELADO';
    createdAt: string;
}

// O que o formulário precisa enviar para a API (para criar um agendamento)
export interface AgendamentoRequest {
    clienteId: string;
    servicoId: string;
    dataHora: string;
    descricaoCliente?: string;
}
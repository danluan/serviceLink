import { Agendamento } from '@/types/agendamento';
import { MOCK_CLIENTS } from './clients.mock';
import { MOCK_SERVICOS } from './services.mock';

// Cria um histórico de agendamentos falsos
export const MOCK_AGENDAMENTOS: Agendamento[] = [
    {
        id: 'ag-1',
        cliente: MOCK_CLIENTS[0], // Klyfithon
        servico: MOCK_SERVICOS[0], // Limpeza Padrão
        dataHora: '2025-10-25T14:00:00.000Z', // Data no futuro
        status: 'CONFIRMADO',
        createdAt: new Date().toISOString(),
    },
    {
        id: 'ag-2',
        cliente: MOCK_CLIENTS[0], // Klyfithon
        servico: MOCK_SERVICOS[2], // Montagem de Móveis
        dataHora: '2025-09-15T09:00:00.000Z', // Data no passado
        status: 'CONCLUIDO',
        createdAt: new Date().toISOString(),
    },
    {
        id: 'ag-3',
        cliente: MOCK_CLIENTS[1], // Maria
        servico: MOCK_SERVICOS[1], // Instalação de Ar
        dataHora: '2025-10-26T10:00:00.000Z',
        status: 'PENDENTE',
        createdAt: new Date().toISOString(),
    },
    {
        id: 'ag-4',
        cliente: MOCK_CLIENTS[0], // Klyfithon
        servico: MOCK_SERVICOS[1], // Instalação de Ar
        dataHora: '2025-08-20T13:00:00.000Z', // Data no passado
        status: 'CANCELADO',
        createdAt: new Date().toISOString(),
    },
];
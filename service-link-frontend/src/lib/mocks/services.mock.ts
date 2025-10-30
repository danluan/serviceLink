import { Servico } from '@/types/agendamento';

export const MOCK_SERVICOS: Servico[] = [
    {
        id: 'serv-1',
        nome: 'Limpeza Padrão Residencial',
        descricao: 'Limpeza completa de até 2 quartos, 1 banheiro, sala e cozinha.',
        precoBase: 150.0,
    },
    {
        id: 'serv-2',
        nome: 'Instalação de Ar Condicionado',
        descricao: 'Instalação de unidade split de até 12.000 BTUs.',
        precoBase: 350.0,
    },
    {
        id: 'serv-3',
        nome: 'Montagem de Móveis',
        descricao: 'Montagem de 1 item de móvel (guarda-roupa, estante, etc).',
        precoBase: 120.0,
    },
];
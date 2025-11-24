import { User } from '@/types/auth';

export const MOCK_CLIENTS: User[] = [
    {
        id: 'uuid-1',
        nome: 'Klyfithon Paulo',
        email: 'klyfithon@email.com',
        telefone: '84999998888',
        cpfCnpj: '111.222.333-44',
        perfil: 'CLIENTE',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
    },
    {
        id: 'uuid-2',
        nome: 'Maria Silva',
        email: 'maria.silva@email.com',
        telefone: '84988887777',
        cpfCnpj: '222.333.444-55',
        perfil: 'CLIENTE',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
    },
    {
        id: 'uuid-3',
        nome: 'João Costa',
        email: 'joao.costa@email.com',
        telefone: '84977776666',
        cpfCnpj: '333.444.555-66',
        perfil: 'CLIENTE',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
    },
];
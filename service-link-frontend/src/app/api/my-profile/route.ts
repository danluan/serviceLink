import { NextResponse } from 'next/server';
import { User } from '@/types/auth';
import { MOCK_CLIENTS } from '@/lib/mocks/clients.mock'; // Para simular atualização

// Vamos precisar do tipo parcial para o PUT (nem todos os campos são editáveis)
type UpdateProfileRequest = Partial<Pick<User, 'nome' | 'email' | 'telefone' | 'cpfCnpj'>>;

/**
 * Função PUT para atualizar o perfil do usuário logado (simulado)
 */
export async function PUT(request: Request) {
    try {
        // No mundo real, pegaríamos o ID do usuário autenticado pelo token/sessão
        // No mock, vamos assumir que é sempre o 'uuid-1' (Klyfithon)
        const userIdToUpdate = 'uuid-1';

        const body: UpdateProfileRequest = await request.json();
        console.log('API MOCK [PUT /api/meu-perfil] RECEBEU:', body);

        // Validação básica (simulada)
        if (!body.nome || !body.email) {
            return NextResponse.json({ message: 'Nome e Email são obrigatórios' }, { status: 400 });
        }

        // Simula a atualização no array de mocks (isso é temporário, some se reiniciar o dev server)
        const clientIndex = MOCK_CLIENTS.findIndex(c => c.id === userIdToUpdate);
        let updatedUser: User | null = null;
        if (clientIndex !== -1) {
            MOCK_CLIENTS[clientIndex] = {
                ...MOCK_CLIENTS[clientIndex],
                ...body, // Sobrescreve os campos com os dados recebidos
                updatedAt: new Date().toISOString()
            };
            updatedUser = MOCK_CLIENTS[clientIndex];
            console.log('API MOCK: Usuário atualizado (em memória):', updatedUser);
        } else {
            console.error('API MOCK: Usuário mock não encontrado para atualizar');
            // Poderia retornar 404 aqui, mas vamos simular sucesso parcial
            updatedUser = { // Retorna um usuário "atualizado" de mentira
                id: userIdToUpdate,
                nome: body.nome,
                email: body.email,
                telefone: body.telefone,
                cpfCnpj: body.cpfCnpj,
                perfil: 'CLIENTE', // Assume perfil
                createdAt: new Date().toISOString(), // Data falsa
                updatedAt: new Date().toISOString()
            }
        }


        // Simula demora
        await new Promise((resolve) => setTimeout(resolve, 1000));

        // Retorna sucesso com o usuário "atualizado"
        return NextResponse.json({ message: 'Perfil atualizado com sucesso!', user: updatedUser });

    } catch (error) {
        console.error('Erro na API mock PUT /api/meu-perfil:', error);
        return NextResponse.json({ message: 'Erro interno no servidor' }, { status: 500 });
    }
}
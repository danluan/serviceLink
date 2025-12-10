// src/app/api/clientes/[id]/route.ts

import { NextResponse } from 'next/server';
// Importa nossa lista de mocks que criamos na Tarefa 2
import { MOCK_CLIENTS } from '@/lib/mocks/clients.mock';
// Importa o tipo 'User' que Daniel criou
import { User } from '@/types/auth';

// Esta função 'GET' será chamada quando o frontend acessar /api/clientes/[id]
export async function GET(
    request: Request,
    { params }: { params: { id: string } }
) {
    const { id } = params; // Pega o ID que veio da URL

    // Procura o cliente na lista de mock
    const client: User | undefined = MOCK_CLIENTS.find((c) => c.id === id);

    if (!client) {
        // Se não encontrar, retorna um erro 404 (Not Found)
        return NextResponse.json({ message: 'Cliente não encontrado' }, { status: 404 });
    }

    // Se encontrar, retorna o JSON do cliente com status 200 (OK)
    return NextResponse.json(client);
}
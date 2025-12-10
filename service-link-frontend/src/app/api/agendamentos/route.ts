import { NextResponse } from 'next/server';
import { AgendamentoRequest, Agendamento } from '@/types/agendamento';
import { MOCK_AGENDAMENTOS } from '@/lib/mocks/agendamentos.mock';

/**
 * Esta função 'POST' será chamada quando o formulário for enviado
 * para /api/agendamentos
 */
export async function POST(request: Request) {
    try {
        const body: AgendamentoRequest = await request.json();

        // No mundo real, aqui nós chamaría o backend Java.
        // No mock, apenas simula que recebe e valida.
        console.log('API DE MOCK RECEBEU:', body);

        if (!body.clienteId || !body.servicoId || !body.dataHora) {
            // Simula um erro de validação
            return NextResponse.json({ message: 'Dados incompletos' }, { status: 400 });
        }

        // Simula uma demora de 1 segundo (para ver o loading no formulário)
        await new Promise((resolve) => setTimeout(resolve, 1000));

        // Simula uma resposta de sucesso do backend
        return NextResponse.json(
            {
                message: 'Agendamento criado com sucesso!',
                agendamento: {
                    id: `ag-${Math.floor(Math.random() * 1000)}`, // Cria um ID falso
                    ...body,
                    status: 'PENDENTE',
                },
            },
            { status: 201 } // 201 = 'Created'
        );
    } catch (error) {
        console.error('Erro na API de mock:', error);
        return NextResponse.json({ message: 'Erro interno no servidor' }, { status: 500 });
    }
}

export async function GET(request: Request) {
    const { searchParams } = new URL(request.url);
    // Vamos permitir filtrar por clienteId, ex: /api/agendamentos?clienteId=uuid-1
    const clienteId = searchParams.get('clienteId');

    let agendamentos: Agendamento[] = MOCK_AGENDAMENTOS;

    if (clienteId) {
        // Filtra os agendamentos para mostrar apenas os do cliente
        agendamentos = MOCK_AGENDAMENTOS.filter(
            (ag) => ag.cliente.id === clienteId
        );
    }

    // Simula uma demora de 1 segundo
    await new Promise((resolve) => setTimeout(resolve, 1000));

    return NextResponse.json(agendamentos);
}


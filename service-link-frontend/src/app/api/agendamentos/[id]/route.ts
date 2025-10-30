import { NextResponse } from 'next/server';
import { MOCK_AGENDAMENTOS } from '@/lib/mocks/agendamentos.mock';
import { Agendamento, AgendamentoRequest } from '@/types/agendamento';

// Esta função GET será chamada para /api/agendamentos/[algum-id]
export async function GET(
    request: Request,
    { params }: { params: { id: string } }
) {
    const { id } = params;

    // Procura o agendamento na nossa lista de mock
    const agendamento: Agendamento | undefined = MOCK_AGENDAMENTOS.find(
        (ag) => ag.id === id
    );

    // Simula uma pequena demora
    await new Promise((resolve) => setTimeout(resolve, 500));

    if (!agendamento) {
        return NextResponse.json({ message: 'Agendamento não encontrado' }, { status: 404 });
    }

    // Retorna o JSON do agendamento encontrado
    return NextResponse.json(agendamento);
}

export async function PUT(
    request: Request,
    { params }: { params: { id: string } }
) {
    const { id } = params; // ID do agendamento a ser atualizado

    try {
        const body: AgendamentoRequest = await request.json(); // Dados que vieram do formulário
        console.log(`API MOCK [PUT /api/agendamentos/${id}] RECEBEU:`, body);

        // Validação básica (simulada)
        if (!body.servicoId || !body.dataHora) { // Ainda checamos se vieram os dados esperados
            return NextResponse.json({ message: 'Dados incompletos para atualização' }, { status: 400 });
        }

        // Encontra o índice do agendamento no array de mock
        const agIndex = MOCK_AGENDAMENTOS.findIndex((ag) => ag.id === id);

        if (agIndex === -1) {
            return NextResponse.json({ message: 'Agendamento não encontrado para atualizar' }, { status: 404 });
        }

        // --- Simula a Atualização (em memória) - CORRIGIDO ---
        // Atualiza SOMENTE a dataHora no mock para simplificar.
        MOCK_AGENDAMENTOS[agIndex] = {
            ...MOCK_AGENDAMENTOS[agIndex], // Mantém os dados antigos (incluindo o serviço original)
            dataHora: body.dataHora,      // Atualiza SOMENTE dataHora
        };
        // --- Fim da correção ---

        const updatedAgendamento = MOCK_AGENDAMENTOS[agIndex];
        console.log('API MOCK: Agendamento atualizado (em memória):', updatedAgendamento);

        // Simula demora
        await new Promise((resolve) => setTimeout(resolve, 1000));

        // Retorna sucesso com o agendamento "atualizado"
        return NextResponse.json({ message: 'Agendamento atualizado com sucesso!', agendamento: updatedAgendamento });

    } catch (error) {
        console.error(`Erro na API mock PUT /api/agendamentos/${id}:`, error);
        return NextResponse.json({ message: 'Erro interno no servidor' }, { status: 500 });
    }
}
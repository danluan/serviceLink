'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Agendamento } from '@/types/agendamento';
import { useAuth } from '@/contexts/AuthContext';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { Button } from '@/components/ui/button';
import WhatsAppButton from "@/components/WhatsAppButton";

// --- Componentes SHADCN/UI para a Tabela ---
import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table';
import { Card } from '@/components/ui/card';
import { Loader2 } from 'lucide-react';
import { Badge } from '@/components/ui/badge';

export default function MeusAgendamentosPage() {
    const { user, logout } = useAuth(); // Pega o usuário logado e o logout

    const [agendamentos, setAgendamentos] = useState<Agendamento[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    // --- useEffect CORRIGIDO (COM GUARD CLAUSE) ---
    useEffect(() => {
        // 1. Guard Clause: Se 'user' não existe ou não tem ID, sai.
        if (!user || !user.id) {
            setIsLoading(false);
            return;
        }

        const userId = user.id;

        async function fetchAgendamentos() {
            setIsLoading(true);
            try {
                const response = await fetch(`/api/agendamentos?clienteId=${userId}`);
                if (!response.ok) {
                    throw new Error('Falha ao buscar agendamentos');
                }
                const data: Agendamento[] = await response.json();
                setAgendamentos(data);
            } catch (error) {
                console.error(error);
            } finally {
                setIsLoading(false);
            }
        }

        fetchAgendamentos();

    }, [user]);


    // Funções de formatação
    const formatarData = (dataIso: string) => {
        return new Date(dataIso).toLocaleString('pt-BR', {
            day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit',
        });
    };

    const getStatusVariant = (status: Agendamento['status']): "default" | "secondary" | "destructive" | "outline" => {
        switch(status) {
            case 'CONCLUIDO': return 'default';
            case 'CONFIRMADO': return 'secondary';
            case 'PENDENTE': return 'outline';
            case 'CANCELADO': return 'destructive';
            default: return 'secondary';
        }
    };

    return (
        <ProtectedRoute requiredRole="CLIENTE">
            <div className="min-h-screen bg-gray-50">
                <header className="bg-white shadow">
                    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
                        <div>
                            <h1 className="text-2xl font-bold text-gray-900">ServiceLink</h1>
                            <p className="text-sm text-gray-600">Área do Cliente</p>
                        </div>
                        <div className="flex items-center gap-4">
                            <div className="text-right">
                                <p className="text-sm font-medium text-gray-900">{user?.nome || 'Usuário'}</p>
                                <p className="text-xs text-gray-600">{user?.email}</p>
                            </div>
                            <Button variant="outline" onClick={logout}>
                                Sair
                            </Button>
                        </div>
                    </div>
                </header>

                <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <div className="mb-8">
                        <h2 className="text-3xl font-bold text-gray-900 mb-4">
                            Meus Agendamentos
                        </h2>
                        <p className="text-gray-600">
                            Acompanhe aqui o histórico de todos os seus serviços.
                        </p>
                    </div>

                    {isLoading ? (
                        <div className="flex items-center justify-center h-64">
                            <Loader2 className="h-12 w-12 animate-spin text-gray-400" />
                        </div>
                    ) : (
                        <Card> {/* Envolvendo a tabela em um Card para ficar bonito */}
                            <Table>
                                <TableCaption>
                                    {agendamentos.length === 0
                                        ? 'Nenhum agendamento encontrado.'
                                        : 'Lista dos seus agendamentos passados e futuros.'}
                                </TableCaption>
                                <TableHeader>
                                    <TableRow>
                                        <TableHead>Serviço</TableHead>
                                        <TableHead>Data e Hora</TableHead>
                                        <TableHead>Status</TableHead>
                                        <TableHead className="text-right">Ações</TableHead>
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {agendamentos.map((ag) => (
                                        <TableRow key={ag.id}>
                                            <TableCell className="font-medium">
                                                {ag.servico.nome}
                                            </TableCell>
                                            <TableCell>{formatarData(ag.dataHora)}</TableCell>
                                            <TableCell>
                                                <Badge variant={getStatusVariant(ag.status)}>
                                                    {ag.status}
                                                </Badge>
                                            </TableCell>
                                            <TableCell className="text-right">
                                                {(ag.status === 'PENDENTE' || ag.status === 'CONFIRMADO') && (
                                                    <Button asChild variant="outline" size="sm">
                                                        <Link href={`/client/appointments/${ag.id}/edit`}>
                                                            Editar
                                                        </Link>
                                                    </Button>
                                                )}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </Card>
                    )}
                </main>
                <WhatsAppButton/>
            </div>
        </ProtectedRoute>
    );
}
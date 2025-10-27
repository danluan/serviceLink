'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Agendamento, AgendamentoRequest, Servico } from '@/types/agendamento';
import { MOCK_SERVICOS } from '@/lib/mocks/services.mock';
import { User } from '@/types/auth';
import { useAuth } from '@/contexts/AuthContext';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { Button } from '@/components/ui/button';
import WhatsAppButton from "@/components/WhatsAppButton";

// --- Componentes SHADCN/UI ---
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Loader2 } from 'lucide-react';


const servicos: Servico[] = MOCK_SERVICOS;

export default function EditarAgendamentoPage({ params }: { params: { id: string } }) {
    const { id: agendamentoId } = params;
    const { user, logout } = useAuth(); // Pega o usuário logado E o logout
    const router = useRouter();

    const [formData, setFormData] = useState<Omit<AgendamentoRequest, 'clienteId'>>({
        servicoId: '',
        dataHora: '',
    });
    const [isLoading, setIsLoading] = useState(true);
    const [isSaving, setIsSaving] = useState(false);
    const [formMessage, setFormMessage] = useState<string | null>(null);

    useEffect(() => {
        async function fetchAgendamento() {
            setIsLoading(true);
            setFormMessage(null);
            try {
                const response = await fetch(`/api/agendamentos/${agendamentoId}`);
                if (!response.ok) {
                    throw new Error('Agendamento não encontrado');
                }
                const data: Agendamento = await response.json();
                if (data.cliente.id !== user?.id) {
                    throw new Error('Você não tem permissão para editar este agendamento.');
                }
                setFormData({
                    servicoId: data.servico.id,
                    dataHora: data.dataHora.substring(0, 16),
                });
            } catch (error: any) {
                setFormMessage(`Erro: ${error.message}`);
            } finally {
                setIsLoading(false);
            }
        }
        if (agendamentoId && user?.id) {
            fetchAgendamento();
        } else if (!user) {
            setIsLoading(false);
        }
    }, [agendamentoId, user]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };
    const handleSelectChange = (value: string) => {
        setFormData((prev) => ({ ...prev, servicoId: value }));
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsSaving(true);
        setFormMessage(null);

        // Dados do formulário prontos para enviar
        const dadosParaEnviar: AgendamentoRequest = {
            ...formData,
            clienteId: user?.id || '', // Cliente ID continua sendo o do usuário logado
        };

        try {
            // 1. Chamamos nossa API MOCK PUT /api/agendamentos/[id]
            const response = await fetch(`/api/agendamentos/${agendamentoId}`, { // Usa o ID do agendamento na URL
                method: 'PUT', // Método PUT para atualização
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dadosParaEnviar),
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || 'Erro ao atualizar agendamento');
            }

            // 2. SUCESSO!
            setFormMessage('Agendamento atualizado com sucesso! ✅ Redirecionando...');
            console.log('Agendamento atualizado:', result);

            // 3. Redireciona de volta para a página de histórico após 1,5 segundos
            setTimeout(() => {
                router.push('/client/appointments');
            }, 1500);

        } catch (error: any) {
            console.error('Erro ao salvar agendamento:', error);
            setFormMessage(`Erro: ${error.message}`);
            setIsSaving(false); // Permite tentar salvar de novo se der erro
        }
    };

    return (
        <ProtectedRoute requiredRole="CLIENTE">
            <div className="min-h-screen bg-gray-50">

                {/* --- CABEÇALHO DA HOME (COPIADO E COLADO AQUI) --- */}
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
                {/* --- FIM DO CABEÇALHO --- */}

                <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    {isLoading ? (
                        <div className="flex items-center justify-center h-64">
                            <Loader2 className="h-12 w-12 animate-spin text-gray-400" />
                        </div>
                    ) : formMessage && formMessage.includes('Erro') ? (
                        <div className="flex items-center justify-center h-64">
                            <p className="text-red-600 font-semibold">{formMessage}</p>
                        </div>
                    ) : (
                        <Card className="max-w-lg mx-auto">
                            <CardHeader>
                                <CardTitle className="text-2xl">Editar Agendamento</CardTitle>
                                <CardDescription>
                                    Altere os dados do seu agendamento abaixo.
                                </CardDescription>
                            </CardHeader>
                            <CardContent>
                                <form onSubmit={handleSubmit} className="space-y-6">
                                    {/* Campo Cliente (desabilitado) */}
                                    <div className="space-y-2">
                                        <Label htmlFor="cliente">Cliente</Label>
                                        <Input id="cliente" value={user?.nome || 'Carregando...'} disabled />
                                    </div>

                                    {/* Campo Serviço (Select) */}
                                    <div className="space-y-2">
                                        <Label htmlFor="servicoId">Serviço</Label>
                                        <Select
                                            name="servicoId"
                                            value={formData.servicoId}
                                            onValueChange={handleSelectChange}
                                            disabled={isSaving}
                                        >
                                            <SelectTrigger id="servicoId">
                                                <SelectValue placeholder="Selecione um serviço..." />
                                            </SelectTrigger>
                                            <SelectContent>
                                                {servicos.map((servico) => (
                                                    <SelectItem key={servico.id} value={servico.id}>
                                                        {servico.nome} (R$ {servico.precoBase})
                                                    </SelectItem>
                                                ))}
                                            </SelectContent>
                                        </Select>
                                    </div>

                                    {/* Campo Data/Hora */}
                                    <div className="space-y-2">
                                        <Label htmlFor="dataHora">Data e Hora</Label>
                                        <Input
                                            id="dataHora"
                                            name="dataHora"
                                            type="datetime-local"
                                            value={formData.dataHora}
                                            onChange={handleChange}
                                            disabled={isSaving}
                                        />
                                    </div>

                                    {/* Botão de Salvar */}
                                    <Button type="submit" className="w-full" disabled={isSaving}>
                                        {isSaving ? (
                                            <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                                        ) : (
                                            'Salvar Alterações'
                                        )}
                                    </Button>

                                    {/* Mensagem de Erro/Sucesso Temporária */}
                                    {formMessage && !formMessage.includes('Erro') && (
                                        <p className="text-sm text-center text-blue-600">{formMessage}</p>
                                    )}
                                </form>
                            </CardContent>
                        </Card>
                    )}
                </main>
                <WhatsAppButton/>
            </div>
        </ProtectedRoute>
    );
}
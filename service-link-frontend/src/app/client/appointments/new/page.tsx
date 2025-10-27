// src/app/agendamentos/page.tsx
'use client';

import { useState } from 'react';
import { AgendamentoRequest, Servico } from '@/types/agendamento';
import { MOCK_SERVICOS } from '@/lib/mocks/services.mock';
import { User } from '@/types/auth';
import { useAuth } from '@/contexts/AuthContext';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import WhatsAppButton from "@/components/WhatsAppButton";

// --- Componentes SHADCN/UI ---
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Loader2 } from 'lucide-react';
import { Textarea } from "@/components/ui/textarea";

// No mundo real, buscaríamos isso de uma API
const servicos: Servico[] = MOCK_SERVICOS;

export default function AgendamentoPage() {
    // --- Lógica de Autenticação (igual a Home) ---
    const { user, logout } = useAuth();

    const [formData, setFormData] = useState<Omit<AgendamentoRequest, 'clienteId'>>({
        servicoId: '',
        dataHora: '',
        descricaoCliente: '',
    });
    const [isLoading, setIsLoading] = useState(false);
    const [formMessage, setFormMessage] = useState<string | null>(null);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSelectChange = (value: string) => {
        setFormData((prev) => ({ ...prev, servicoId: value }));
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true);
        setFormMessage(null);

        // --- MUDANÇA IMPORTANTE ---
        // Agora usamos o ID do usuário logado (do useAuth)
        const dadosParaEnviar: AgendamentoRequest = {
            ...formData,
            clienteId: user?.id || '', // Usando o ID do usuário do contexto!
        };

        try {
            const response = await fetch('/api/agendamentos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dadosParaEnviar),
            });
            // ... (resto da lógica de submit continua igual) ...
            const result = await response.json();
            if (!response.ok) throw new Error(result.message || 'Erro ao agendar');
            setFormMessage('Agendamento criado com sucesso! ✅');
            setFormData({ servicoId: '', dataHora: '' });
        } catch (error: any) {
            console.error('Erro no formulário:', error);
            setFormMessage(`Erro: ${error.message}`);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <ProtectedRoute requiredRole="CLIENTE">
            <div className="min-h-screen bg-gray-50">
                {/* Header (Copiado da Home) */}
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

                {/* Conteúdo Principal (Nosso formulário) */}
                <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <Card className="max-w-lg mx-auto">
                        <CardHeader>
                            <CardTitle className="text-2xl">Novo Agendamento</CardTitle>
                            <CardDescription>
                                Preencha os dados abaixo para criar um novo agendamento.
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <form onSubmit={handleSubmit} className="space-y-6">
                                {/* Campo Cliente (Agora usa o nome do user logado) */}
                                <div className="space-y-2">
                                    <Label htmlFor="cliente">Cliente</Label>
                                    <Input id="cliente" value={user?.nome || 'Carregando...'} disabled />
                                </div>

                                {/* --- O resto do formulário continua igual --- */}

                                <div className="space-y-2">
                                    <Label htmlFor="servicoId">Serviço</Label>
                                    <Select
                                        name="servicoId"
                                        value={formData.servicoId}
                                        onValueChange={handleSelectChange}
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

                                <div className="space-y-2">
                                    <Label htmlFor="dataHora">Data e Hora</Label>
                                    <Input
                                        id="dataHora"
                                        name="dataHora"
                                        type="datetime-local"
                                        value={formData.dataHora}
                                        onChange={handleChange}
                                    />
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="descricaoCliente">Observações (Opcional)</Label>
                                    <Textarea
                                        id="descricaoCliente"
                                        name="descricaoCliente" // Deve ser igual ao nome no useState
                                        placeholder="Alguma instrução especial para o prestador? (Ex: Chave na portaria, Cuidado com o cachorro, etc.)"
                                        value={formData.descricaoCliente}
                                        onChange={(e) => setFormData(prev => ({ ...prev, descricaoCliente: e.target.value }))} // Atualiza o estado
                                        disabled={isLoading}
                                        rows={3} // Define a altura inicial
                                    />
                                </div>

                                <Button type="submit" className="w-full" disabled={isLoading}>
                                    {isLoading ? (
                                        <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                                    ) : (
                                        'Confirmar Agendamento'
                                    )}
                                </Button>

                                {formMessage && (
                                    <div
                                        className={`p-3 rounded-md text-sm ${
                                            formMessage.includes('Erro')
                                                ? 'bg-red-100 text-red-800'
                                                : 'bg-green-100 text-green-800'
                                        }`}
                                    >
                                        {formMessage}
                                    </div>
                                )}
                            </form>
                        </CardContent>
                    </Card>
                </main>
                <WhatsAppButton/>
            </div>
        </ProtectedRoute>
    );
}
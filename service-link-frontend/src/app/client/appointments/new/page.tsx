'use client';

import { useState, useEffect, FormEvent, ChangeEvent } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { AgendamentoRequest, Servico } from '@/types/agendamento';
import { useAuth } from '@/contexts/AuthContext';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import WhatsAppButton from "@/components/WhatsAppButton";
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Loader2 } from 'lucide-react';
import { Textarea } from "@/components/ui/textarea";
import { Alert, AlertDescription } from '@/components/ui/alert';
import LogadoNavbar from "@/components/Client/LogHeader";

type NewAppointmentFormData = Omit<AgendamentoRequest, 'clienteId'>;

export default function AgendamentoPage() {
    const { user, token } = useAuth(); // Pega user e token
    const router = useRouter();
    const searchParams = useSearchParams();
    const initialServiceId = searchParams.get('serviceId');

    const [formData, setFormData] = useState<NewAppointmentFormData>({
        servicoId: initialServiceId || '',
        dataHora: '',
        observacao: '',
    });

    // Estados para os dados da página
    const [listaServicos, setListaServicos] = useState<Servico[]>([]);
    const [isLoadingServicos, setIsLoadingServicos] = useState(true); // Loading do <Select>

    const [isLoading, setIsLoading] = useState(false); // Loading do botão Submit
    const [formMessage, setFormMessage] = useState<string | null>(null); // Mensagens de feedback

    useEffect(() => {
        const fetchServicos = async () => {
            setIsLoadingServicos(true);

            if (!token) {
                console.warn("Fetch de serviços em espera: token ainda não disponível.");
                return; // Aborta a função se não houver token
            }

            console.log("Iniciando fetch de serviços com o token:", token);

            try {
                const response = await fetch('http://localhost:8080/api/servico', {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (!response.ok) {
                    if (response.status === 403) {
                        throw new Error('Você não tem permissão para ver esta lista.');
                    }
                    throw new Error('Falha ao carregar a lista de serviços.');
                }

                const data: Servico[] = await response.json();
                console.log("Serviços carregados do backend:", data);
                setListaServicos(data);

            } catch (error: unknown) {
                console.error('Erro ao buscar serviços:', error);
                let errorMessage = 'Não foi possível carregar os serviços.';
                if (error instanceof Error) { errorMessage = error.message; }
                setFormMessage(`Erro: ${errorMessage}`);
            } finally {
                setIsLoadingServicos(false);
            }
        };
        fetchServicos();
    }, [token]);

    // Pré-seleciona o serviço se veio da URL
    useEffect(() => {
        if (initialServiceId) {
            setFormData(prev => ({ ...prev, servicoId: initialServiceId }));
        }
    }, [initialServiceId]);

    // Handlers para os campos
    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => { // Aceita Input e Textarea
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };
    const handleSelectChange = (value: string) => {
        setFormData((prev) => ({ ...prev, servicoId: value }));
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (!token || !user?.id) { // Precisa do token E do ID do usuário
            setFormMessage("Erro: Autenticação inválida.");
            return;
        }
        if (!formData.servicoId || !formData.dataHora) {
            setFormMessage("Erro: Serviço e Data/Hora são obrigatórios.");
            return;
        }


        setIsLoading(true);
        setFormMessage(null);

        const dadosParaEnviar = {
            clienteId: user.id, // ID do usuário logado
            servicoId: formData.servicoId,
            // Formata a data/hora local para ISO string UTC (padrão)
            dataHora: new Date(formData.dataHora).toISOString(),
            observacao: formData.observacao || null // Usa 'observacao'
        };

        try {
            console.log("Enviando POST /api/agendamento:", dadosParaEnviar);
            // --- CHAMADA AO BACKEND REAL ---
            const response = await fetch('http://localhost:8080/api/agendamento', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // Envia o Token
                },
                body: JSON.stringify(dadosParaEnviar),
            });

            const result = await response.json();
            if (!response.ok) {
                throw new Error(result.message || `Erro ${response.status}`);
            }

            console.log("Agendamento criado:", result);
            setFormMessage('Agendamento criado com sucesso! ✅ Redirecionando...');
            setFormData({ servicoId: '', dataHora: '', observacao: '' }); // Limpa o form

            setTimeout(() => { router.push('/client/appointments'); }, 2000);

        } catch (error: unknown) {
            console.error('Erro no formulário:', error);
            let errorMessage = 'Erro ao criar agendamento.';
            if (error instanceof Error) { errorMessage = error.message; }
            setFormMessage(`Erro: ${errorMessage}`);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <ProtectedRoute requiredRole="CLIENTE">
            <div className="min-h-screen bg-gray-50">
                <LogadoNavbar/>

                <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <Card className="max-w-lg mx-auto">
                        <CardHeader>
                            <CardTitle className="text-2xl">Novo Agendamento</CardTitle>
                            <CardDescription>Preencha os dados.</CardDescription>
                        </CardHeader>
                        <CardContent>
                            {formMessage && (
                                <Alert variant={formMessage.includes('Erro') ? 'destructive' : 'default'} className={`mb-4 ${formMessage.includes('sucesso') ? 'bg-green-100 text-green-800 border-green-300' : ''}`}>
                                    <AlertDescription>{formMessage}</AlertDescription>
                                </Alert>
                            )}
                            <form onSubmit={handleSubmit} className="space-y-6">
                                <div className="space-y-2"> <Label htmlFor="cliente">Cliente</Label> <Input id="cliente" value={user?.nome || '...'} disabled /> </div>

                                <div className="space-y-2"> <Label htmlFor="servicoId">Serviço *</Label>
                                    <Select
                                        name="servicoId"
                                        value={formData.servicoId}
                                        onValueChange={handleSelectChange}
                                        required
                                        disabled={isLoading || isLoadingServicos}
                                    >
                                        <SelectTrigger id="servicoId"> <SelectValue placeholder="Selecione..." /> </SelectTrigger>

                                        <SelectContent>
                                            {isLoadingServicos ? (
                                                <SelectItem value="loading" disabled>Carregando...</SelectItem>
                                            ) : (
                                                listaServicos.map((s) => ( // <-- CORRIGIDO (era 'servicos')
                                                    <SelectItem key={s.id} value={String(s.id)}>
                                                        {s.nome} (R$ {s.precoBase})
                                                    </SelectItem>
                                                ))
                                            )}
                                        </SelectContent>
                                    </Select> </div>
                                <div className="space-y-2"> <Label htmlFor="dataHora">Data e Hora *</Label> <Input id="dataHora" name="dataHora" type="datetime-local" value={formData.dataHora} onChange={handleChange} required disabled={isLoading} /> </div>
                                <div className="space-y-2"> <Label htmlFor="observacao">Observações (Opcional)</Label> <Textarea id="observacao" name="observacao" placeholder="Instruções..." value={formData.observacao} onChange={handleChange} disabled={isLoading} rows={3} /> </div>

                                <Button type="submit" className="w-full" disabled={isLoading}> {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : 'Confirmar Agendamento'} </Button>
                            </form>
                        </CardContent>
                    </Card>
                </main>
                <WhatsAppButton/>
            </div>
        </ProtectedRoute>
    );
}
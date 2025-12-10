'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import { ProtectedRoute } from '@/components/ProtectedRoute';

import { Agendamento } from '@/types/agendamento';

import LogadoNavbar from "@/components/Client/LogHeader";
import WhatsAppButton from "@/components/WhatsAppButton";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Loader2, Calendar, User, ClipboardCheck, Clock } from 'lucide-react';

export default function AppointmentsPage() {
    const { user, token } = useAuth();
    const router = useRouter();

    const [appointments, setAppointments] = useState<Agendamento[]>([]);
    const [loading, setLoading] = useState(true);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);

    const fetchAppointments = async () => {
        const apiUrl = 'http://localhost:8080';
        console.log('=== DEBUG AGENDAMENTOS ===');
        console.log('1. User:', user);
        console.log('2. User ID:', user?.profileId);
        console.log('3. Token:', token ? 'EXISTE' : 'NÃO EXISTE');
        console.log('4. API URL:', apiUrl);

        if (!token || !user?.profileId) {
            setErrorMsg("Erro: autenticação inválida.");
            setLoading(false);
            return;
        }

        const urlCompleta = `${apiUrl}/api/agendamento/cliente/${user?.profileId}/agendamentos`;
        console.log('5. URL COMPLETA:', urlCompleta);
        console.log('======================');

        try {
            const response = await fetch(
                urlCompleta,
                {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                const status = response.status; // Pega o código HTTP

                let errorMessage = `Erro ${status}: Falha ao buscar agendamentos.`;

                if (errorData.message) {
                    errorMessage += ` Detalhe: ${errorData.message}`;
                } else if (status === 401 || status === 403) {
                    errorMessage = "Acesso Negado. Sua sessão pode ter expirado.";
                } else if (status === 404) {
                    errorMessage = "Endpoint não encontrado. Verifique o URL do backend.";
                }

                throw new Error(errorMessage);
            }

            const data: Agendamento[] = await response.json();
            setAppointments(data);
        } catch (err) {
            setErrorMsg(err instanceof Error ? err.message : "Erro inesperado");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (token && user?.profileId) {
            fetchAppointments();
        } else {
            setLoading(false);
        }
    }, [token, user?.profileId]);

    // formata data/hora estilo BR
    const formatDate = (iso: string) =>
        new Date(iso).toLocaleString("pt-BR", {
            dateStyle: "short",
            timeStyle: "short"
        });

    return (
        <ProtectedRoute requiredRole="CLIENTE">
            <div className="min-h-screen bg-gray-50">
                <LogadoNavbar />

                <main className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

                    <div className="flex justify-between mb-6">
                        <h2 className="text-2xl font-bold">Meus Agendamentos</h2>
                        <Button onClick={() => router.push("/client/appointments/new")}>
                            + Novo Agendamento
                        </Button>
                    </div>

                    {errorMsg && (
                        <Alert variant="destructive" className="mb-4">
                            <AlertDescription>{errorMsg}</AlertDescription>
                        </Alert>
                    )}

                    {loading ? (
                        <div className="flex justify-center py-8">
                            <Loader2 className="h-6 w-6 animate-spin" />
                        </div>
                    ) : appointments.length === 0 ? (
                        <Card className="p-6 text-center">
                            <p className="text-gray-600 mb-4">Você ainda não possui agendamentos.</p>
                            <Button onClick={() => router.push("/client/appointments/new")}>
                                Agendar agora
                            </Button>
                        </Card>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            {appointments.map((ag) => (
                                <Card key={ag.id}>
                                    <CardHeader>
                                        <CardTitle className="flex items-center gap-2 text-lg">
                                            <Calendar className="h-5 w-5" />
                                            {ag.nomeServico}
                                        </CardTitle>
                                    </CardHeader>

                                    <CardContent className="space-y-2 text-sm text-gray-700">
                                        <p className="flex items-center gap-2">
                                            <Clock className="h-4 w-4" /> {formatDate(ag.dataHora)}
                                        </p>

                                        <p className="flex items-center gap-2">
                                            <User className="h-4 w-4" /> {ag.nomePrestador}
                                        </p>

                                        <p className="flex items-center gap-2">
                                            <ClipboardCheck className="h-4 w-4" /> Status:{" "}
                                            <span className="font-semibold">{ag.status}</span>
                                        </p>
                                    </CardContent>
                                </Card>
                            ))}
                        </div>
                    )}

                </main>

                <WhatsAppButton />
            </div>
        </ProtectedRoute>
    );
}

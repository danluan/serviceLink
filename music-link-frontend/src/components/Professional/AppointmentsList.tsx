'use client';

import { useState, useEffect } from "react";
import { Calendar, Clock, User, Loader2 } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { toast } from "sonner"; // Usando sonner para notificações

interface AgendamentoListagemDTO {
    id: number;
    dataHora: string;
    status: string;
    observacao: string;
    clienteId: number;
    nomeCliente: string;
    servicoId: number;
    nomeServico: string;
}

const AppointmentsList = () => {
    const [appointments, setAppointments] = useState<AgendamentoListagemDTO[]>([]);
    const [loading, setLoading] = useState(true);

    const getAuthData = () => {
        const authDataString = localStorage.getItem('@servicelink:auth');
        const token = localStorage.getItem('idToken') || localStorage.getItem('@servicelink:token'); // Buscando token

        if (!authDataString || !token) return null;
        try {
            const authData = JSON.parse(authDataString);
            return { prestadorId: authData.profileId, token };
        } catch (e) {
            return null;
        }
    };

    const fetchAppointments = async () => {
        const auth = getAuthData();
        if (!auth) {
            toast.error("Sessão inválida. Faça login novamente.");
            setLoading(false);
            return;
        }

        try {
            setLoading(true);

            const response = await fetch(`http://localhost:8080/api/agendamento/${auth.prestadorId}/prox-agendamentos`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${auth.token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('Falha ao carregar agendamentos.');
            }

            const data: AgendamentoListagemDTO[] = await response.json();
            setAppointments(data);

        } catch (error) {
            console.error("Erro ao buscar agendamentos:", error);
            toast.error("Não foi possível carregar os próximos agendamentos.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAppointments();
    }, []);

    const formatDateTime = (dataHora: string) => {
        try {
            const date = new Date(dataHora);
            const day = date.getDate().toString().padStart(2, '0');
            const month = (date.getMonth() + 1).toString().padStart(2, '0');
            const time = date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });

            // Retorna a data no formato 'DD/MM' e a hora no formato 'HH:MM'
            return { date: `${day}/${month}`, time };
        } catch {
            return { date: 'Data Inválida', time: 'HH:MM' };
        }
    };

    const getStatusColor = (status: string) => {
        // Padronize os nomes dos status conforme seu backend (ex: PENDENTE, CONFIRMADO)
        const lowerStatus = status.toLowerCase();

        if (lowerStatus === "confirmado") {
            return "bg-green-500/10 text-green-700 border-green-500/20";
        }
        if (lowerStatus === "pendente") {
            return "bg-yellow-500/10 text-yellow-700 border-yellow-500/20";
        }
        if (lowerStatus === "cancelado") {
            return "bg-red-500/10 text-red-700 border-red-500/20";
        }
        return "bg-gray-500/10 text-gray-700 border-gray-500/20";
    };

    return (
        <Card className="shadow-card border-border">
            <CardHeader>
                <CardTitle style={{ color: "#000000" }} className="text-xl font-semibold">
                    Próximos Agendamentos
                </CardTitle>
            </CardHeader>
            <CardContent>
                {loading ? (
                    <div className="flex justify-center py-6">
                        <Loader2 className="h-6 w-6 animate-spin text-primary" />
                    </div>
                ) : appointments.length === 0 ? (
                    <p className="text-center text-muted-foreground py-6">
                        Nenhum agendamento futuro encontrado.
                    </p>
                ) : (
                    <div className="space-y-4">
                        {appointments.map((appointment) => {
                            const { date, time } = formatDateTime(appointment.dataHora);

                            return (
                                <div
                                    key={appointment.id}
                                    className="flex items-start gap-4 p-4 rounded-lg border border-border hover:shadow-lg transition-shadow bg-card"
                                >
                                    <div className="flex flex-col items-center justify-center min-w-[60px] py-2 px-3 rounded-lg bg-primary/10">
                                        {/* Exibe o Mês e o Dia */}
                                        <span className="text-xs font-medium text-primary uppercase">
                        {date.split('/')[1]}
                      </span>
                                        <span className="text-xl font-bold text-primary">
                        {date.split('/')[0]}
                      </span>
                                    </div>
                                    <div className="flex-1 min-w-0">
                                        <div className="flex items-start justify-between gap-2 mb-2">
                                            {/* Nome do Serviço */}
                                            <h4 className="font-semibold text-foreground truncate">
                                                {appointment.nomeServico}
                                            </h4>
                                            <Badge
                                                variant="outline"
                                                className={`${getStatusColor(appointment.status)} shrink-0`}
                                            >
                                                {appointment.status}
                                            </Badge>
                                        </div>
                                        <div className="space-y-1">
                                            {/* Hora */}
                                            <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                                <Clock className="h-4 w-4" />
                                                <span>{time}</span>
                                            </div>
                                            {/* Nome do Cliente */}
                                            <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                                <User className="h-4 w-4" />
                                                <span className="truncate">{appointment.nomeCliente}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </CardContent>
        </Card>
    );
};

export default AppointmentsList;

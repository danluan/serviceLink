'use client';

import { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { toast } from 'sonner';
import { Calendar, DollarSign, Clock } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";

interface AgendamentoDTO {
    id: number;
    dataHora: string;
}

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

const API_BASE_URL = "http://localhost:8080";

const KPICards = () => {
    const { user } = useAuth();
    const [agendamentosDoDia, setAgendamentosDoDia] = useState<AgendamentoDTO[]>([]);
    const [appointments, setAppointments] = useState<AgendamentoListagemDTO[]>([]);
    const [faturamento, setFaturamento] = useState<number | null>(null);
    const [loading, setLoading] = useState(true);


    const getAuthData = () => {
        const authDataString = localStorage.getItem('@servicelink:auth');
        const token = localStorage.getItem('@servicelink:token');
        if (!authDataString || !token) return null;
        try {
            const authData = JSON.parse(authDataString);
            return { prestadorId: authData.profileId, token };
        } catch (e) {
            return null;
        }
    };

    const formatDateTime = (dataHora: string) => {
        try {
            const date = new Date(dataHora);
            const dateStr = date.toLocaleDateString('pt-BR');
            const timeStr = date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
            return { dateStr, timeStr };
        } catch {
            return { dateStr: 'Data Inválida', timeStr: 'HH:MM' };
        }
    };

    const fetchFaturamento = async (prestadorId: number) => {
        const today = new Date();
        const currentYear = today.getFullYear();
        const currentMonth = today.getMonth() + 1;
        const auth = getAuthData();
        if (!auth) {
            setLoading(false);
            return;
        }

        const url = `${API_BASE_URL}/api/agendamento/${prestadorId}/faturamento?ano=${currentYear}&mes=${currentMonth}`;

        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${auth.token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                console.error("Erro ao buscar faturamento:", response.status);
                toast.error("Falha ao carregar faturamento.");
                return;
            }

            const valor = await response.json();
            setFaturamento(valor);
        } catch (error) {
            console.error("Erro de rede ao buscar faturamento:", error);
            toast.error("Erro de rede ao buscar faturamento.");
        }
    }

    const fetchDashboardData = async () => {
        const auth = getAuthData();
        if (!auth) {
            setLoading(false);
            return;
        }

        let success = true;

        try {
            const responseHoje = await fetch(`${API_BASE_URL}/api/agendamento/${auth.prestadorId}/agendamentos-hoje`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${auth.token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (responseHoje.ok) {
                const data: AgendamentoDTO[] = await responseHoje.json();
                setAgendamentosDoDia(data);
            } else {
                success = false;
                console.log("Falha ao buscar agendamentos de hoje:", responseHoje);
            }

            const responseProx = await fetch(`${API_BASE_URL}/api/agendamento/${auth.prestadorId}/prox-agendamentos`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${auth.token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (responseProx.ok) {
                const dataProx: AgendamentoListagemDTO[] = await responseProx.json();
                setAppointments(dataProx);
            } else {
                success = false;
                console.log("Falha ao buscar próximos agendamentos:", responseProx);
            }

            await fetchFaturamento(auth.prestadorId);

        } catch (error) {
            success = false;
            toast.error("Erro de rede ao buscar dados do dashboard.");
            console.error(error);
        } finally {
            setLoading(false);
            if (!success) {
                toast.error("Alguns dados do dashboard não puderam ser carregados.");
            }
        }
    };

    useEffect(() => {
        setLoading(true);
        fetchDashboardData();
    }, []);

    const proximoServico = appointments.length > 0 ? appointments[0] : null;
    const proximoServicoFormatado = proximoServico ? formatDateTime(proximoServico.dataHora) : null;

    const kpis = [
        {
            icon: Calendar,
            label: "Serviços Agendados Hoje",
            value: loading ? "..." : agendamentosDoDia.length.toString(),
            color: "#0080FF",
            bgColor: "rgba(0,128,255,0.2)",
        },
        {
            icon: DollarSign,
            label: "Faturamento Últimos 30 dias",
            value: loading ? "..." : faturamento ? `R$ ${faturamento.toLocaleString('pt-BR')}` : "R$ 0,00",
            color: "#00ff33",
            bgColor: "rgba(26,255,0,0.2)",
        },
        {
            icon: Clock,
            label: "Próximo Serviço",
            // 4. VALORES DINÂMICOS
            value: loading
                ? "..."
                : proximoServico
                    ? proximoServico.nomeServico
                    : "Nenhum Agendamento",

            subtitle: loading
                ? "..."
                : proximoServico
                    ? `${proximoServicoFormatado?.dateStr} às ${proximoServicoFormatado?.timeStr} (${proximoServico.nomeCliente})`
                    : "---",

            color: "#FFB900FF",
            bgColor: "rgba(255,185,0,0.2)",
        },
    ];


    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {kpis.map((kpi, index) => {
                const Icon = kpi.icon;
                return (
                    <Card
                        key={index}
                        className="overflow-hidden border-border shadow-card hover:shadow-hover transition-shadow"
                    >
                        <CardContent className="p-6">
                            <div className="flex items-start justify-between">
                                <div className="flex-1">
                                    <p className="text-sm font-medium text-muted-foreground mb-2">
                                        {kpi.label}
                                    </p>
                                    <p className="text-2xl font-bold text-foreground mb-1">
                                        {kpi.value}
                                    </p>
                                    {kpi.subtitle && (
                                        <p className="text-sm text-muted-foreground">
                                            {kpi.subtitle}
                                        </p>
                                    )}
                                </div>
                                <div
                                    style={{
                                        color: `${kpi.color}`,
                                        backgroundColor: `${kpi.bgColor}`,
                                    }}
                                    className="p-3 rounded-xl flex items-center justify-center"
                                >
                                    <Icon className="h-6 w-6" />
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                );
            })}
        </div>
    );
};

export default KPICards;
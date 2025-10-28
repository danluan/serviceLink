'use client';
import { useEffect, useState } from "react";
import { useAuth } from "@/contexts/AuthContext";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
} from "recharts";
import { toast } from "sonner";

const API_BASE_URL = "http://localhost:8080";

// nomes dos meses
const MESES_PT = ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"];

const FinanceChart = () => {
    const { user } = useAuth();
    const [data, setData] = useState<{ month: string; revenue: number }[]>([]);
    const [loading, setLoading] = useState(true);

    const getAuthData = () => {
        const authDataString = localStorage.getItem('@servicelink:auth');
        const token = localStorage.getItem('@servicelink:token');
        if (!authDataString || !token) return null;
        try {
            const authData = JSON.parse(authDataString);
            return { prestadorId: authData.profileId, token };
        } catch {
            return null;
        }
    };

    const fetchFaturamentoSemestre = async () => {
        const auth = getAuthData();
        if (!auth) {
            setLoading(false);
            return;
        }

        const today = new Date();
        const anoAtual = today.getFullYear();
        const mesAtual = today.getMonth() + 1;

        // define o semestre atual
        const inicioSemestre = mesAtual <= 6 ? 1 : 7;
        const fimSemestre = mesAtual <= 6 ? 6 : 12;

        const mesesDoSemestre = Array.from({ length: fimSemestre - inicioSemestre + 1 }, (_, i) => inicioSemestre + i);

        try {
            const resultados = await Promise.all(
                mesesDoSemestre.map(async (mes) => {
                    const url = `${API_BASE_URL}/api/agendamento/${auth.prestadorId}/faturamento?ano=${anoAtual}&mes=${mes}`;
                    try {
                        const response = await fetch(url, {
                            method: "GET",
                            headers: {
                                "Authorization": `Bearer ${auth.token}`,
                                "Content-Type": "application/json",
                            },
                        });
                        if (response.ok) {
                            const valor = await response.json();
                            return { month: MESES_PT[mes - 1], revenue: Number(valor) || 0 };
                        } else {
                            return { month: MESES_PT[mes - 1], revenue: 0 };
                        }
                    } catch {
                        return { month: MESES_PT[mes - 1], revenue: 0 };
                    }
                })
            );

            setData(resultados);
        } catch (error) {
            console.error("Erro ao buscar faturamento do semestre:", error);
            toast.error("Falha ao carregar faturamento do semestre.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchFaturamentoSemestre();
    }, []);

    return (
        <Card className="shadow-card border-border">
            <CardHeader>
                <CardTitle className="text-xl font-semibold">
                    Faturamento Semestral (R$)
                </CardTitle>
            </CardHeader>
            <CardContent>
                {loading ? (
                    <p className="text-muted-foreground text-sm">Carregando...</p>
                ) : data.length === 0 ? (
                    <p className="text-muted-foreground text-sm">Sem dados disponíveis</p>
                ) : (
                    <ResponsiveContainer width="100%" height={300}>
                        <BarChart data={data} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
                            <CartesianGrid strokeDasharray="3 3" stroke="var(--border)" />
                            <XAxis
                                dataKey="month"
                                stroke="var(--muted-foreground)"
                                fontSize={12}
                                tickLine={false}
                            />
                            <YAxis
                                stroke="var(--muted-foreground)"
                                fontSize={12}
                                tickLine={false}
                                axisLine={false}
                                tickFormatter={(value) => `${value}`}
                            />
                            <Tooltip
                                contentStyle={{
                                    backgroundColor: "var(--popover)",
                                    border: "1px solid var(--border)",
                                    borderRadius: "0.5rem",
                                }}
                                labelStyle={{ color: "var(--foreground)" }}
                                cursor={{ fill: "var(--muted)" }}
                            />
                            <Bar
                                dataKey="revenue"
                                fill="var(--primary)"
                                radius={[8, 8, 0, 0]}
                            />
                        </BarChart>
                    </ResponsiveContainer>
                )}
            </CardContent>
        </Card>
    );
};

export default FinanceChart;

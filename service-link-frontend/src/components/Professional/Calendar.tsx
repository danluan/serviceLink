'use client';
import { useState, useEffect, useMemo, useCallback } from "react";
import { ChevronLeft, ChevronRight, CheckCircle, XCircle, Clock, Loader2 } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
    Sheet,
    SheetContent,
    SheetDescription,
    SheetHeader,
    SheetTitle,
} from "@/components/ui/sheet";
import { Badge } from "@/components/ui/badge";
import { toast } from "sonner";

const API_BASE_URL = "http://localhost:8080";

interface AppointmentDTO {
    id: number;
    dataHora: string;
    status: string;
    observacao: string;
    clienteId: number;
    nomeCliente: string;
    servicoId: number;
    nomeServico: string;
}

type MonthlyAppointments = Record<string, AppointmentDTO[]>;


const CalendarComponent = () => {
    const [currentDate, setCurrentDate] = useState(new Date());
    const [selectedDay, setSelectedDay] = useState<string | null>(null);
    const [isSheetOpen, setIsSheetOpen] = useState(false);

    // Loading para a BUSCA do calendário
    const [loading, setLoading] = useState(false);

    // Loading para a ATUALIZAÇÃO de status (guarda o ID do agendamento sendo atualizado)
    const [updatingId, setUpdatingId] = useState<number | null>(null);

    const [appointmentsData, setAppointmentsData] = useState<MonthlyAppointments>({});


    const getAuthData = () => {
        const authDataString = localStorage.getItem('@servicelink:auth');
        const token = localStorage.getItem('@servicelink:token');
        if (!authDataString || !token) return null;
        try {
            const authData = JSON.parse(authDataString);
            // Corrigido: Assegura que profileId é usado para prestadorId se a API espera isso
            return { prestadorId: authData.profileId, token };
        } catch (e) {
            return null;
        }
    };

    // ... (formatTime e getStatusColor mantidos) ...
    const formatTime = (dataHora: string): string => {
        try {
            return new Date(dataHora).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
        } catch {
            return 'N/A';
        }
    };

    const getStatusColor = (status: string) => {
        switch (status.toLowerCase()) {
            case "confirmado":
                return "bg-green-500/10 text-green-700 border-green-500/20";
            case "pendente":
                return "bg-yellow-500/10 text-yellow-700 border-yellow-500/20";
            case "concluido":
                return "bg-gray-500/10 text-gray-700 border-gray-500/20";
            default:
                return "bg-muted text-muted-foreground";
        }
    };


    const { year, month, daysInMonth, firstDayOfMonth, monthName } = useMemo(() => {
        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();
        const monthName = currentDate.toLocaleDateString("pt-BR", {
            month: "long",
            year: "numeric",
        });

        const daysInMonth = new Date(year, month + 1, 0).getDate();

        const firstDayOfMonth = new Date(year, month, 1).getDay();

        return { year, month, daysInMonth, firstDayOfMonth, monthName };
    }, [currentDate]);

    const weekDays = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];

    // --- BUSCA DE DADOS ---

    const fetchAppointments = useCallback(async () => {
        const auth = getAuthData();
        if (!auth) {
            toast.error("Sessão inválida. Faça login novamente.");
            return;
        }

        const yearParam = year;
        const monthParam = month + 1;

        try {
            setLoading(true);

            // Corrigido o endpoint para usar API_BASE_URL
            const response = await fetch(`${API_BASE_URL}/api/agendamento/${auth.prestadorId}/agendamentos/mensal?ano=${yearParam}&mes=${monthParam}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${auth.token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                // Tenta ler o erro do corpo, se houver
                const errorData = await response.json().catch(() => ({ message: "Erro desconhecido" }));
                throw new Error(errorData.message || "Erro ao buscar agendamentos do mês.");
            }

            const data: MonthlyAppointments = await response.json();
            setAppointmentsData(data);

        } catch (error) {
            console.error("Erro ao buscar agendamentos:", error);
            toast.error((error as Error).message || "Não foi possível carregar a agenda.");
        } finally {
            setLoading(false);
        }
    }, [year, month]); // Depende do mês e ano atualizados

    const handleUpdateStatus = useCallback(async (appointment: AppointmentDTO, newStatus: string) => {
        const auth = getAuthData();
        if (!auth) {
            toast.error("Sessão inválida. Faça login novamente.");
            return;
        }

        setUpdatingId(appointment.id); // Inicia o loading para ESTE agendamento

        try {
            const url = `${API_BASE_URL}/api/agendamento/${appointment.id}/status/${newStatus}`;

            const response = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.token}`
                }
            });

            // 1. TRATAMENTO DE ERRO (Códigos 4xx e 5xx)
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: response.statusText }));
                // Lança o erro para o bloco 'catch'
                throw new Error(errorData.message || `Erro ao atualizar o status: ${response.statusText}`);
            }

            let updatedAppointment: AppointmentDTO;

            // 2. TRATAMENTO DE SUCESSO (Códigos 2xx)

            // Verifica se a resposta tem corpo (ex: 200 OK)
            if (response.status !== 204 && response.headers.get('content-length') !== '0') {
                // Tenta ler o JSON se houver corpo
                updatedAppointment = await response.json().catch(() => ({
                    ...appointment,
                    status: newStatus.toUpperCase() // Fallback
                }));
            } else {
                // Se for 204 No Content ou sem corpo, cria o objeto atualizado localmente
                updatedAppointment = {
                    ...appointment,
                    status: newStatus.toUpperCase()
                };
            }

            // 3. ATUALIZAÇÃO DO ESTADO LOCAL:
            setAppointmentsData(prevData => {
                const dayKey = String(new Date(appointment.dataHora).getDate());

                // Mapeia a lista de agendamentos para o dia e substitui o item atualizado
                const updatedDayAppointments = (prevData[dayKey] || []).map(item =>
                    item.id === appointment.id
                        ? updatedAppointment // Usa o objeto final, seja da API ou local
                        : item
                );

                return {
                    ...prevData,
                    [dayKey]: updatedDayAppointments,
                };
            });

            toast.success(`Status de ${appointment.nomeServico} alterado para ${newStatus.toUpperCase()} com sucesso!`);

        } catch (error) {
            console.error("Falha ao atualizar o status:", error);
            toast.error((error as Error).message || "Falha ao atualizar o status.");
        } finally {
            setUpdatingId(null); // Finaliza o loading
        }
    }, []);

    // ... (useEffect e Handlers de navegação mantidos) ...

    useEffect(() => {
        fetchAppointments();
    }, [fetchAppointments]); // Rebusca sempre que o mês/ano mudar

    const handleMonthChange = (direction: 'prev' | 'next') => {
        setCurrentDate(prevDate => {
            const newDate = new Date(prevDate.getTime());
            newDate.setMonth(prevDate.getMonth() + (direction === 'next' ? 1 : -1));
            return newDate;
        });
    };

    const handleDayClick = (day: number) => {
        const dayKey = String(day);
        if (appointmentsData[dayKey] && appointmentsData[dayKey].length > 0) {
            setSelectedDay(dayKey);
            setIsSheetOpen(true);
        }
    };

    // --- RENDERIZAÇÃO ---

    const appointmentsForSelectedDay = selectedDay ? appointmentsData[selectedDay] : [];

    return (
        <>
            {/* ... (Renderização do Calendário e Sheet) ... */}
            <Card className="shadow-card border-border">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-4">
                    <CardTitle className="text-xl font-semibold">Agenda</CardTitle>
                    <div className="flex items-center gap-2">
                        <Button variant="outline" size="icon" className="h-8 w-8" onClick={() => handleMonthChange('prev')}>
                            <ChevronLeft className="h-4 w-4" />
                        </Button>
                        <span className="text-sm font-medium capitalize min-w-[150px] text-center">
              {monthName}
            </span>
                        <Button variant="outline" size="icon" className="h-8 w-8" onClick={() => handleMonthChange('next')}>
                            <ChevronRight className="h-4 w-4" />
                        </Button>
                    </div>
                </CardHeader>
                <CardContent>
                    {loading ? (
                        <div className="flex justify-center py-10">
                            <Loader2 className="h-6 w-6 animate-spin text-primary" />
                        </div>
                    ) : (
                        <>
                            <div className="grid grid-cols-7 gap-2 mb-2">
                                {weekDays.map((day) => (
                                    <div
                                        key={day}
                                        className="text-center text-sm font-medium text-muted-foreground py-2"
                                    >
                                        {day}
                                    </div>
                                ))}
                            </div>
                            <div className="grid grid-cols-7 gap-2">
                                {Array.from({ length: firstDayOfMonth }).map((_, index) => (
                                    <div key={`empty-${index}`} className="aspect-square" />
                                ))}
                                {Array.from({ length: daysInMonth }).map((_, index) => {
                                    const day = index + 1;
                                    const dayKey = String(day);
                                    // 2. Usar o estado REAL da API para verificar agendamentos
                                    const hasAppointments = appointmentsData[dayKey] && appointmentsData[dayKey].length > 0;

                                    // Simula hoje (para fins de estilo, ajustado para o mês atual)
                                    const isToday = day === new Date().getDate() &&
                                        month === new Date().getMonth() &&
                                        year === new Date().getFullYear();

                                    return (
                                        <button
                                            key={day}
                                            onClick={() => handleDayClick(day)}
                                            disabled={!hasAppointments} // Desabilita clique se não houver agendamentos
                                            className={`
                        aspect-square rounded-lg flex flex-col items-center justify-center
                        transition-all relative text-sm
                        ${hasAppointments
                                                ? "hover:shadow-lg cursor-pointer border border-primary/20"
                                                : "opacity-50"}
                        ${isToday
                                                ? "bg-primary text-primary-foreground font-semibold"
                                                : "hover:bg-muted"}
                      `}
                                        >
                                            <span>{day}</span>
                                            {hasAppointments && (
                                                <div className="flex gap-0.5 mt-1">
                                                    {/* Renderiza um ponto para indicar agendamento */}
                                                    <div
                                                        className={`w-1.5 h-1.5 rounded-full ${
                                                            isToday ? "bg-primary-foreground" : "bg-primary"
                                                        }`}
                                                    />
                                                </div>
                                            )}
                                        </button>
                                    );
                                })}
                            </div>
                        </>
                    )}
                </CardContent>
            </Card>

            {/* Sheet de Detalhes dos Agendamentos */}
            <Sheet open={isSheetOpen} onOpenChange={setIsSheetOpen}>
                <SheetContent className="w-full sm:max-w-md bg-background overflow-y-auto">
                    <SheetHeader>
                        <SheetTitle>
                            Agendamentos - {selectedDay} de {monthName.split(" ")[0]}
                        </SheetTitle>
                        <SheetDescription>
                            Gerenciar os serviços agendados para este dia
                        </SheetDescription>
                    </SheetHeader>
                    <div className="mt-6 space-y-4">
                        {appointmentsForSelectedDay.map((appointment) => {
                            // Define o estado de loading para este item
                            const isUpdating = updatingId === appointment.id;

                            return (
                                <Card key={appointment.id} className="border-border">
                                    <CardContent className="p-4">
                                        <div className="flex items-start justify-between mb-3">
                                            <div className="flex items-center gap-2">
                                                <Clock className="h-4 w-4 text-muted-foreground" />
                                                <span className="font-semibold text-foreground">
                              {formatTime(appointment.dataHora)}
                            </span>
                                            </div>
                                            <Badge className={getStatusColor(appointment.status)}>
                                                {appointment.status}
                                            </Badge>
                                        </div>
                                        <h4 className="font-medium text-foreground mb-1">
                                            {appointment.nomeServico}
                                        </h4>
                                        <p className="text-sm text-muted-foreground mb-4">
                                            Cliente: {appointment.nomeCliente}
                                        </p>
                                        <p className="text-xs text-muted-foreground italic mb-4">
                                            Obs: {appointment.observacao || 'Nenhuma observação.'}
                                        </p>

                                        {/* Botões de Ação */}
                                        <div className="flex gap-2">
                                            {appointment.status.toLowerCase() === "pendente" && (
                                                <Button
                                                    size="sm"
                                                    className="flex-1"
                                                    variant="default"
                                                    onClick={() => handleUpdateStatus(appointment, "CONFIRMADO")}
                                                    disabled={isUpdating} // Usa o loading específico
                                                >
                                                    {isUpdating ? <Loader2 className="h-4 w-4 animate-spin" /> : <CheckCircle className="h-4 w-4 mr-1" />}
                                                    {isUpdating ? 'Confirmando...' : 'Confirmar'}
                                                </Button>
                                            )}
                                            {appointment.status.toLowerCase() !== "concluido" && (
                                                <Button
                                                    size="sm"
                                                    variant="outline"
                                                    className="flex-1"
                                                    onClick={() => handleUpdateStatus(appointment, "CONCLUIDO")}
                                                    disabled={isUpdating}
                                                >
                                                    {isUpdating ? <Loader2 className="h-4 w-4 animate-spin" /> : 'Concluir'}
                                                </Button>
                                            )}
                                            <Button
                                                size="sm"
                                                variant="destructive"
                                                className="flex-1"
                                                onClick={() => handleUpdateStatus(appointment, "CANCELADO")}
                                                disabled={isUpdating}
                                            >
                                                {isUpdating ? <Loader2 className="h-4 w-4 animate-spin" /> : <XCircle className="h-4 w-4 mr-1" />}
                                                {isUpdating ? 'Cancelando...' : 'Cancelar'}
                                            </Button>
                                        </div>
                                    </CardContent>
                                </Card>
                            )}
                        )}
                    </div>
                </SheetContent>
            </Sheet>
        </>
    );
};

export default CalendarComponent;
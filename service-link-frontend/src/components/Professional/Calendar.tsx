'use client';
import { useState } from "react";
import { ChevronLeft, ChevronRight, CheckCircle, XCircle, Clock } from "lucide-react";
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

interface Appointment {
  id: number;
  time: string;
  service: string;
  client: string;
  status: "pendente" | "confirmado" | "concluido";
}

const mockAppointments: Record<number, Appointment[]> = {
  5: [
    { id: 1, time: "09:00", service: "Limpeza de Casa", client: "Maria Silva", status: "confirmado" },
    { id: 2, time: "14:00", service: "Jardinagem", client: "João Santos", status: "pendente" },
  ],
  12: [
    { id: 3, time: "10:00", service: "Pintura", client: "Ana Costa", status: "pendente" },
  ],
  18: [
    { id: 4, time: "15:00", service: "Reparos Elétricos", client: "Carlos Souza", status: "confirmado" },
    { id: 5, time: "16:30", service: "Encanamento", client: "Paula Oliveira", status: "pendente" },
  ],
  25: [
    { id: 6, time: "11:00", service: "Limpeza de Escritório", client: "Tech Solutions", status: "confirmado" },
  ],
};

const CalendarComponent = () => {
  const [currentMonth] = useState(new Date(2025, 0, 1)); // Janeiro 2025
  const [selectedDay, setSelectedDay] = useState<number | null>(null);
  const [isSheetOpen, setIsSheetOpen] = useState(false);

  const daysInMonth = new Date(
    currentMonth.getFullYear(),
    currentMonth.getMonth() + 1,
    0
  ).getDate();
  const firstDayOfMonth = new Date(
    currentMonth.getFullYear(),
    currentMonth.getMonth(),
    1
  ).getDay();

  const monthName = currentMonth.toLocaleDateString("pt-BR", {
    month: "long",
    year: "numeric",
  });

  const weekDays = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];

  const handleDayClick = (day: number) => {
    if (mockAppointments[day]) {
      setSelectedDay(day);
      setIsSheetOpen(true);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "confirmado":
        return "bg-success text-success-foreground";
      case "pendente":
        return "bg-warning text-warning-foreground";
      case "concluido":
        return "bg-muted text-muted-foreground";
      default:
        return "bg-muted text-muted-foreground";
    }
  };

  return (
    <>
      <Card className="shadow-card border-border">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-4">
          <CardTitle className="text-xl font-semibold">Agenda</CardTitle>
          <div className="flex items-center gap-2">
            <Button variant="outline" size="icon" className="h-8 w-8">
              <ChevronLeft className="h-4 w-4" />
            </Button>
            <span className="text-sm font-medium capitalize min-w-[150px] text-center">
              {monthName}
            </span>
            <Button variant="outline" size="icon" className="h-8 w-8">
              <ChevronRight className="h-4 w-4" />
            </Button>
          </div>
        </CardHeader>
        <CardContent>
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
              const hasAppointments = mockAppointments[day];
              const isToday = day === 5; // Simulando hoje como dia 5

              return (
                <button
                  key={day}
                  onClick={() => handleDayClick(day)}
                  className={`
                    aspect-square rounded-lg flex flex-col items-center justify-center
                    transition-all relative
                    ${hasAppointments ? "hover:shadow-hover cursor-pointer" : ""}
                    ${isToday ? "bg-primary text-primary-foreground font-semibold" : "hover:bg-muted"}
                  `}
                >
                  <span className="text-sm">{day}</span>
                  {hasAppointments && (
                    <div className="flex gap-0.5 mt-1">
                      {hasAppointments.map((_, i) => (
                        <div
                          key={i}
                          className={`w-1.5 h-1.5 rounded-full ${
                            isToday ? "bg-primary-foreground" : "bg-primary"
                          }`}
                        />
                      ))}
                    </div>
                  )}
                </button>
              );
            })}
          </div>
        </CardContent>
      </Card>

      <Sheet open={isSheetOpen} onOpenChange={setIsSheetOpen}>
        <SheetContent className="w-full sm:max-w-md bg-background">
          <SheetHeader>
            <SheetTitle>
              Agendamentos - {selectedDay} de {monthName.split(" ")[0]}
            </SheetTitle>
            <SheetDescription>
              Gerenciar os serviços agendados para este dia
            </SheetDescription>
          </SheetHeader>
          <div className="mt-6 space-y-4">
            {selectedDay &&
              mockAppointments[selectedDay]?.map((appointment) => (
                <Card key={appointment.id} className="border-border">
                  <CardContent className="p-4">
                    <div className="flex items-start justify-between mb-3">
                      <div className="flex items-center gap-2">
                        <Clock className="h-4 w-4 text-muted-foreground" />
                        <span className="font-semibold text-foreground">
                          {appointment.time}
                        </span>
                      </div>
                      <Badge className={getStatusColor(appointment.status)}>
                        {appointment.status}
                      </Badge>
                    </div>
                    <h4 className="font-medium text-foreground mb-1">
                      {appointment.service}
                    </h4>
                    <p className="text-sm text-muted-foreground mb-4">
                      Cliente: {appointment.client}
                    </p>
                    <div className="flex gap-2">
                      {appointment.status === "pendente" && (
                        <Button size="sm" className="flex-1 bg-success hover:bg-success/90">
                          <CheckCircle className="h-4 w-4 mr-1" />
                          Confirmar
                        </Button>
                      )}
                      {appointment.status !== "concluido" && (
                        <Button
                          size="sm"
                          variant="outline"
                          className="flex-1"
                        >
                          Concluir
                        </Button>
                      )}
                      <Button
                        size="sm"
                        variant="destructive"
                        className="flex-1"
                      >
                        <XCircle className="h-4 w-4 mr-1" />
                        Cancelar
                      </Button>
                    </div>
                  </CardContent>
                </Card>
              ))}
          </div>
        </SheetContent>
      </Sheet>
    </>
  );
};

export default CalendarComponent;

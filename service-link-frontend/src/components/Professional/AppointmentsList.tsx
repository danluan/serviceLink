import { Calendar, Clock, User } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

const AppointmentsList = () => {
  const upcomingAppointments = [
    {
      id: 1,
      date: "05 Jan",
      time: "14:00",
      service: "Jardinagem",
      client: "João Santos",
      status: "pendente",
    },
    {
      id: 2,
      date: "12 Jan",
      time: "10:00",
      service: "Pintura",
      client: "Ana Costa",
      status: "pendente",
    },
    {
      id: 3,
      date: "18 Jan",
      time: "15:00",
      service: "Reparos Elétricos",
      client: "Carlos Souza",
      status: "confirmado",
    },
    {
      id: 4,
      date: "25 Jan",
      time: "11:00",
      service: "Limpeza de Escritório",
      client: "Tech Solutions",
      status: "confirmado",
    },
  ];

  const getStatusColor = (status: string) => {
    return status === "confirmado"
      ? "bg-success/10 text-success border-success/20"
      : "bg-warning/10 text-warning border-warning/20";
  };

  return (
    <Card className="shadow-card border-border">
      <CardHeader>
        <CardTitle
            style={{color:"#000000"}}
            className="text-xl font-semibold">
          Próximos Agendamentos
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {upcomingAppointments.map((appointment) => (
            <div
              key={appointment.id}
              className="flex items-start gap-4 p-4 rounded-lg border border-border hover:shadow-hover transition-shadow bg-card"
            >
              <div className="flex flex-col items-center justify-center min-w-[60px] py-2 px-3 rounded-lg bg-primary/10">
                <span className="text-xs font-medium text-primary uppercase">
                  {appointment.date.split(" ")[1]}
                </span>
                <span className="text-xl font-bold text-primary">
                  {appointment.date.split(" ")[0]}
                </span>
              </div>
              <div className="flex-1 min-w-0">
                <div className="flex items-start justify-between gap-2 mb-2">
                  <h4 className="font-semibold text-foreground truncate">
                    {appointment.service}
                  </h4>
                  <Badge
                    variant="outline"
                    className={`${getStatusColor(appointment.status)} shrink-0`}
                  >
                    {appointment.status}
                  </Badge>
                </div>
                <div className="space-y-1">
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <Clock className="h-4 w-4" />
                    <span>{appointment.time}</span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <User className="h-4 w-4" />
                    <span className="truncate">{appointment.client}</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  );
};

export default AppointmentsList;

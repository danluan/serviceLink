import { Calendar, DollarSign, Clock } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";

const KPICards = () => {
  const kpis = [
    {
      icon: Calendar,
      label: "Serviços Agendados Hoje",
      value: "3",
      color: "#0080FF",
      bgColor: "rgba(0,128,255,0.2)",
    },
    {
      icon: DollarSign,
      label: "Faturamento Últimos 30 dias",
      value: "R$ 1.250",
      color: "#00ff33",
      bgColor: "rgba(26,255,0,0.2)",
    },
    {
      icon: Clock,
      label: "Próximo Serviço",
      value: "Limpeza de Casa",
      subtitle: "14:00",
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
                          backgroundColor: `${kpi.bgColor}`, // Cor de Fundo
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

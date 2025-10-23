'use client';
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

const FinanceChart = () => {
  const data = [
    { month: "Ago", revenue: 980 },
    { month: "Set", revenue: 1150 },
    { month: "Out", revenue: 890 },
    { month: "Nov", revenue: 1320 },
    { month: "Dez", revenue: 1100 },
    { month: "Jan", revenue: 1250 },
  ];

  return (
    <Card className="shadow-card border-border">
      <CardHeader>
        <CardTitle className="text-xl font-semibold">
          Faturamento Mensal (R$)
        </CardTitle>
      </CardHeader>
      <CardContent>
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
      </CardContent>
    </Card>
  );
};

export default FinanceChart;

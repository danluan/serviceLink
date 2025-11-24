'use client';

import LogadoNavbar from "@/components/Professional/LogadoNavbar";
import KPICards from "@/components/Professional/KPICards";
import CalendarComponent from "@/components/Professional/Calendar";
import AppointmentsList from "@/components/Professional/AppointmentsList";
import FinanceChart from "@/components/Professional/FinanceChart";
import WhatsAppButton from "@/components/WhatsAppButton";
const Index = () => {
    return (
        <div className="min-h-screen bg-background">
            <LogadoNavbar />

            <main className="container mx-auto px-4 py-6 space-y-6">
                {/* KPI Cards */}
                <section>
                    <KPICards />
                </section>

                {/* Main Grid */}
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                    {/* Calendar - Takes 2 columns on large screens */}
                    <div className="lg:col-span-2">
                        <CalendarComponent />
                    </div>

                    {/* Appointments List - Takes 1 column */}
                    <div className="lg:col-span-1">
                        <AppointmentsList />
                    </div>
                </div>

                {/* Finance Chart */}
                <section>
                    <FinanceChart />
                </section>
            </main>
            <WhatsAppButton/>
        </div>
    );
};

export default Index;

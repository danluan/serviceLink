'use client';

import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { useAuth } from '@/contexts/AuthContext';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import WhatsAppButton from "@/components/WhatsAppButton";
import LogadoNavbar from "@/components/Professional/LogadoNavbar";
import Link from 'next/link';


const HomePage = () => {
    const { user, logout } = useAuth();

    return (
        <ProtectedRoute requiredRole="PRESTADOR">
            <div className="min-h-screen bg-gray-50">
                <LogadoNavbar/>

                {/* Main Content */}
                <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <div className="mb-8">
                        <h2 className="text-3xl font-bold text-gray-900 mb-2">
                            Bem-vindo, {user?.nome?.split(' ')[0]}! 👋
                        </h2>
                        <p className="text-gray-600">
                            Explore os serviços disponíveis ou gerencie suas solicitações.
                        </p>
                    </div>

                    {/* Cards Grid */}
                    <div className="flex flex-row gap-12 justify-center mb-8">
                        <Card>
                            <CardHeader>
                                <CardTitle>Gerenciar Serviços</CardTitle>
                                <CardDescription>
                                    Gerencie seus serviços disponibilizados no ServiceLink!
                                </CardDescription>
                            </CardHeader>
                            <CardContent>
                                <Link href="/professional/services">
                                <Button className="w-full">
                                    Gerenciar Serviços
                                </Button>
                                </Link>
                            </CardContent>
                        </Card>

                        <Card>
                            <CardHeader>
                                <CardTitle>Dashboard</CardTitle>
                                <CardDescription>
                                    Visualize as informações de destaque!
                                </CardDescription>
                            </CardHeader>
                            <CardContent>
                                <Link href="/professional/dashboard">
                                <Button variant="outline" className="w-full">
                                    Ver Dashboard
                                </Button>
                                </Link>
                            </CardContent>
                        </Card>
                    </div>

                    {/* Info Section */}
                    <div className="mt-8 bg-blue-50 border border-blue-200 rounded-lg p-6">
                        <h3 className="text-lg font-semibold text-blue-900 mb-2">
                            ℹ️ Informações da Conta
                        </h3>
                        <div className="space-y-2 text-sm text-blue-800">
                            <p><strong>Nome:</strong> {user?.nome}</p>
                            <p><strong>Email:</strong> {user?.email}</p>
                            <p><strong>Telefone:</strong> {user?.telefone || 'Não informado'}</p>
                            <p><strong>CPF/CNPJ:</strong> {user?.cpfCnpj || 'Não informado'}</p>
                            <p><strong>Perfil:</strong> {user?.perfil}</p>
                        </div>
                    </div>
                </main>
                <WhatsAppButton/>

            </div>
        </ProtectedRoute>
    );
};

export default HomePage;
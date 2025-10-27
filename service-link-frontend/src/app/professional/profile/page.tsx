'use client';

import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { useAuth } from '@/contexts/AuthContext';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import WhatsAppButton from "@/components/WhatsAppButton";

const HomePage = () => {
    const { user, logout } = useAuth();

    return (
        <ProtectedRoute requiredRole="PRESTADOR">
            <div className="min-h-screen bg-gray-50">
                {/* Header */}
                <header className="bg-white shadow">
                    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
                        <div>
                            <h1 className="text-2xl font-bold text-gray-900">ServiceLink</h1>
                            <p className="text-sm text-gray-600">Área do Cliente</p>
                        </div>
                        <div className="flex items-center gap-4">
                            <div className="text-right">
                                <p className="text-sm font-medium text-gray-900">{user?.nome || 'Usuário'}</p>
                                <p className="text-xs text-gray-600">{user?.email}</p>
                            </div>
                            <Button variant="outline" onClick={logout}>
                                Sair
                            </Button>
                        </div>
                    </div>
                </header>

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
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        <Card>
                            <CardHeader>
                                <CardTitle>Buscar Serviços</CardTitle>
                                <CardDescription>
                                    Encontre profissionais qualificados
                                </CardDescription>
                            </CardHeader>
                            <CardContent>
                                <Button className="w-full">
                                    Explorar Serviços
                                </Button>
                            </CardContent>
                        </Card>

                        <Card>
                            <CardHeader>
                                <CardTitle>Minhas Solicitações</CardTitle>
                                <CardDescription>
                                    Acompanhe o status dos seus pedidos
                                </CardDescription>
                            </CardHeader>
                            <CardContent>
                                <Button variant="outline" className="w-full">
                                    Ver Solicitações
                                </Button>
                            </CardContent>
                        </Card>

                        <Card>
                            <CardHeader>
                                <CardTitle>Meu Perfil</CardTitle>
                                <CardDescription>
                                    Gerencie suas informações
                                </CardDescription>
                            </CardHeader>
                            <CardContent>
                                <Button variant="outline" className="w-full">
                                    Editar Perfil
                                </Button>
                            </CardContent>
                        </Card>
                    </div>

                    {/* Info Section */}
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
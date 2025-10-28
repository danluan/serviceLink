// src/app/meu-perfil/editar/page.tsx
'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { User } from '@/types/auth';
import WhatsAppButton from "@/components/WhatsAppButton";
import { useAuth } from '@/contexts/AuthContext';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { Button } from '@/components/ui/button';

// --- Componentes SHADCN/UI ---
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Loader2 } from 'lucide-react';

type ProfileFormData = Pick<User, 'nome' | 'email' | 'telefone' | 'cpfCnpj'>;

export default function EditarPerfilPage() {
    const { user, logout, updateUser } = useAuth(); // Pegamos o updateUser do contexto!
    const router = useRouter();

    const [formData, setFormData] = useState<ProfileFormData>({
        nome: '',
        email: '',
        telefone: '',
        cpfCnpj: '',
    });
    const [isLoading, setIsLoading] = useState(true);
    const [isSaving, setIsSaving] = useState(false);
    const [formMessage, setFormMessage] = useState<string | null>(null);

    useEffect(() => {
        if (user) {
            setFormData({
                nome: user.nome || '',
                email: user.email || '',
                telefone: user.telefone || '',
                cpfCnpj: user.cpfCnpj || '',
            });
            setIsLoading(false);
        }
        // Se user for null, o isLoading continua true até o user carregar
    }, [user]); // Roda quando o 'user' do AuthContext mudar

    // Função para atualizar o estado do formulário
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    // Função para SALVAR as alterações
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsSaving(true);
        setFormMessage(null);

        // Filtra para mandar só os campos que foram realmente preenchidos
        const dadosParaEnviar = Object.entries(formData).reduce((acc, [key, value]) => {
            if (value) { // Só inclui se tiver valor
                acc[key as keyof ProfileFormData] = value;
            }
            return acc;
        }, {} as Partial<ProfileFormData>);


        try {
            const response = await fetch('/api/my-profile', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dadosParaEnviar),
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || 'Erro ao atualizar perfil');
            }

            setFormMessage('Perfil atualizado com sucesso! ✅');

            if (result.user) {
                updateUser(result.user);
            }

            setTimeout(() => router.push('/client/home'), 1500);

        } catch (error: any) {
            console.error('Erro ao salvar perfil:', error);
            setFormMessage(`Erro: ${error.message}`);
        } finally {
            setIsSaving(false);
        }
    };

    return (
        <ProtectedRoute requiredRole="CLIENTE">
            <div className="min-h-screen bg-gray-50">
                {/* --- CABEÇALHO (Use o AppHeader ou cole o código aqui) --- */}
                <header className="bg-white shadow">
                    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
                        <div> <h1 className="text-2xl font-bold text-gray-900">ServiceLink</h1> <p className="text-sm text-gray-600">Área do Cliente</p> </div>
                        <div className="flex items-center gap-4"> <div className="text-right"> <p className="text-sm font-medium text-gray-900">{user?.nome || 'Usuário'}</p> <p className="text-xs text-gray-600">{user?.email}</p> </div> <Button variant="outline" onClick={logout}> Sair </Button> </div>
                    </div>
                </header>
                {/* --- FIM DO CABEÇALHO --- */}

                <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    {isLoading ? (
                        <div className="flex items-center justify-center h-64">
                            <Loader2 className="h-12 w-12 animate-spin text-gray-400" />
                        </div>
                    ) : (
                        <Card className="max-w-lg mx-auto">
                            <CardHeader>
                                <CardTitle className="text-2xl">Editar Perfil</CardTitle>
                                <CardDescription>
                                    Atualize suas informações pessoais.
                                </CardDescription>
                            </CardHeader>
                            <CardContent>
                                <form onSubmit={handleSubmit} className="space-y-4">
                                    {/* Campo Nome */}
                                    <div className="space-y-2">
                                        <Label htmlFor="nome">Nome Completo</Label>
                                        <Input
                                            id="nome"
                                            name="nome"
                                            value={formData.nome}
                                            onChange={handleChange}
                                            disabled={isSaving}
                                            required
                                        />
                                    </div>
                                    {/* Campo Email */}
                                    <div className="space-y-2">
                                        <Label htmlFor="email">Email</Label>
                                        <Input
                                            id="email"
                                            name="email"
                                            type="email"
                                            value={formData.email}
                                            onChange={handleChange}
                                            disabled={isSaving}
                                            required
                                        />
                                    </div>
                                    {/* Campo Telefone */}
                                    <div className="space-y-2">
                                        <Label htmlFor="telefone">Telefone</Label>
                                        <Input
                                            id="telefone"
                                            name="telefone"
                                            value={formData.telefone}
                                            onChange={handleChange}
                                            disabled={isSaving}
                                        />
                                    </div>
                                    {/* Campo CPF/CNPJ */}
                                    <div className="space-y-2">
                                        <Label htmlFor="cpfCnpj">CPF/CNPJ</Label>
                                        <Input
                                            id="cpfCnpj"
                                            name="cpfCnpj"
                                            value={formData.cpfCnpj}
                                            onChange={handleChange}
                                            disabled={isSaving}
                                        />
                                    </div>

                                    {/* Botão de Salvar */}
                                    <Button type="submit" className="w-full" disabled={isSaving}>
                                        {isSaving ? (
                                            <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                                        ) : (
                                            'Salvar Alterações'
                                        )}
                                    </Button>

                                    {/* Mensagem de Sucesso/Erro */}
                                    {formMessage && (
                                        <div
                                            className={`p-3 rounded-md text-sm ${
                                                formMessage.includes('Erro')
                                                    ? 'bg-red-100 text-red-800'
                                                    : 'bg-green-100 text-green-800'
                                            }`}
                                        >
                                            {formMessage}
                                        </div>
                                    )}
                                </form>
                            </CardContent>
                        </Card>
                    )}
                </main>
                <WhatsAppButton/>
            </div>
        </ProtectedRoute>
    );
}
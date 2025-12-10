'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { useFormValidation } from '@/hooks/useFormValidation';
import { registerProfessionalSchema, RegisterProfessionalData } from '@/lib/validations';
import { AuthService } from '@/services/auth.service';
import { ApiError } from '@/lib/api-error';

export default function ProfessionalRegister() {
  const router = useRouter();
  const [apiError, setApiError] = useState<string>('');
  const [successMessage, setSuccessMessage] = useState<string>('');

  const {
    data,
    errors,
    isSubmitting,
    updateField,
    handleSubmit,
  } = useFormValidation<RegisterProfessionalData>({
    schema: registerProfessionalSchema,
    onSubmit: async (formData) => {
      try {
        setApiError('');
        setSuccessMessage('');

        await AuthService.register({
          ...formData,
          perfil: 'PRESTADOR',
        });

        setSuccessMessage('Cadastro realizado com sucesso! Redirecionando...');
        setTimeout(() => router.push('/login'), 2000);
      } catch (error) {
        if (error instanceof ApiError) {
          setApiError(error.message);
        } else {
          setApiError('Erro ao realizar cadastro. Tente novamente.');
        }
      }
    },
  });

  return (
    <div className="min-h-screen flex items-center justify-center px-4 py-12 bg-gray-50">
      <div className="w-full max-w-md space-y-8">
        <div className="text-center">
          <h1 className="text-3xl font-bold tracking-tight">Cadastro de Professor</h1>
          <p className="mt-2 text-sm text-gray-600">
            Crie sua conta para oferecer suas aulas
          </p>
        </div>

        {apiError && (
          <Alert variant="destructive">
            <AlertDescription>{apiError}</AlertDescription>
          </Alert>
        )}

        {successMessage && (
          <Alert className="border-green-500 bg-green-50">
            <AlertDescription className="text-green-800">{successMessage}</AlertDescription>
          </Alert>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-4">
            <div>
              <Label htmlFor="nome">Nome completo</Label>
              <Input
                id="nome"
                type="text"
                placeholder="Digite seu nome completo"
                value={data.nome || ''}
                onChange={(e) => updateField('nome', e.target.value)}
                className={errors.nome ? 'border-red-500' : ''}
                disabled={isSubmitting}
              />
              {errors.nome && (
                <p className="text-sm text-red-500 mt-1">{errors.nome}</p>
              )}
            </div>

            <div>
              <Label htmlFor="email">E-mail</Label>
              <Input
                id="email"
                type="email"
                placeholder="seu@email.com"
                value={data.email || ''}
                onChange={(e) => updateField('email', e.target.value)}
                className={errors.email ? 'border-red-500' : ''}
                disabled={isSubmitting}
              />
              {errors.email && (
                <p className="text-sm text-red-500 mt-1">{errors.email}</p>
              )}
            </div>

            <div>
              <Label htmlFor="telefone">Telefone</Label>
              <Input
                id="telefone"
                type="tel"
                placeholder="84996615141"
                value={data.telefone || ''}
                onChange={(e) => updateField('telefone', e.target.value.replace(/\D/g, ''))}
                className={errors.telefone ? 'border-red-500' : ''}
                disabled={isSubmitting}
                maxLength={15}
              />
              {errors.telefone && (
                <p className="text-sm text-red-500 mt-1">{errors.telefone}</p>
              )}
              <p className="text-xs text-gray-500 mt-1">Apenas números</p>
            </div>

            <div>
              <Label htmlFor="cpfCnpj">CPF/CNPJ</Label>
              <Input
                id="cpfCnpj"
                type="text"
                placeholder="12345678902"
                value={data.cpfCnpj || ''}
                onChange={(e) => updateField('cpfCnpj', e.target.value.replace(/\D/g, ''))}
                className={errors.cpfCnpj ? 'border-red-500' : ''}
                disabled={isSubmitting}
                maxLength={14}
              />
              {errors.cpfCnpj && (
                <p className="text-sm text-red-500 mt-1">{errors.cpfCnpj}</p>
              )}
              <p className="text-xs text-gray-500 mt-1">11 dígitos para CPF ou 14 para CNPJ</p>
            </div>

            <div>
              <Label htmlFor="senha">Senha</Label>
              <Input
                id="senha"
                type="password"
                placeholder="Mínimo 6 caracteres"
                value={data.senha || ''}
                onChange={(e) => updateField('senha', e.target.value)}
                className={errors.senha ? 'border-red-500' : ''}
                disabled={isSubmitting}
              />
              {errors.senha && (
                <p className="text-sm text-red-500 mt-1">{errors.senha}</p>
              )}
            </div>
          </div>

          <Button
            type="submit"
            className="w-full"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Cadastrando...' : 'Cadastrar como Profissional'}
          </Button>
        </form>

        <div className="text-center space-y-2">
          <p className="text-sm text-gray-600">Já possui uma conta?</p>
          <Button variant="outline" className="w-full" asChild>
            <Link href="/login">Fazer Login</Link>
          </Button>
        </div>

        <div className="text-center">
          <Link
            href="/register/client"
            className="text-sm text-blue-600 hover:text-blue-800 hover:underline"
          >
            Cadastrar como cliente
          </Link>
        </div>
      </div>
    </div>
  );
}
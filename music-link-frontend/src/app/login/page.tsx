'use client';

import { useState } from 'react';
import { z } from 'zod';
import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { useFormValidation } from '@/hooks/useFormValidation';
import { loginSchema, LoginData } from '@/lib/validations';
import { useAuth } from '@/contexts/AuthContext';
import { ApiError } from '@/lib/api-error';

const LoginPage = () => {
  const [apiError, setApiError] = useState<string>('');
  const { login } = useAuth();

  const {
    data,
    errors,
    isSubmitting,
    updateField,
    handleSubmit,
  } = useFormValidation<LoginData>({
    schema: loginSchema,
    onSubmit: async (formData) => {
      try {
        setApiError('');
        await login(formData);
      } catch (error) {
          if (error instanceof ApiError) {
              setApiError(error.message);

          } else if (error instanceof z.ZodError) {
              setApiError('Erro de validação inesperado. Tente novamente.');

          } else {
              setApiError('Erro ao realizar login. Tente novamente.');
          }
      }
    },
  });

  return (
    <div className="min-h-screen flex items-center justify-center px-4 py-12 bg-gray-50">
      <div className="w-full max-w-md space-y-8">
        <div className="text-center">
          <h1 className="text-3xl font-bold tracking-tight">Entrar</h1>
          <p className="mt-2 text-sm text-gray-600">
            Acesse sua conta para continuar
          </p>
        </div>

        {apiError && (
          <Alert variant="destructive">
            <AlertDescription>{apiError}</AlertDescription>
          </Alert>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-4">
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
                autoComplete="email"
              />
              {errors.email && (
                <p className="text-sm text-red-500 mt-1">{errors.email}</p>
              )}
            </div>

            <div>
              <Label htmlFor="senha">Senha</Label>
              <Input
                id="senha"
                type="password"
                placeholder="Digite sua senha"
                value={data.senha || ''}
                onChange={(e) => updateField('senha', e.target.value)}
                className={errors.senha ? 'border-red-500' : ''}
                disabled={isSubmitting}
                autoComplete="current-password"
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
            {isSubmitting ? 'Entrando...' : 'Entrar'}
          </Button>
        </form>

        <div className="space-y-4">
          <div className="relative">
            <div className="absolute inset-0 flex items-center">
              <span className="w-full border-t" />
            </div>
            <div className="relative flex justify-center text-xs uppercase">
              <span className="bg-gray-50 px-2 text-gray-500">
                Não possui uma conta?
              </span>
            </div>
          </div>

          <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
            <Button variant="outline" asChild>
              <Link href="/register/client">
                Cadastrar como Aluno
              </Link>
            </Button>
            <Button variant="outline" asChild>
              <Link href="/register/professional">
                Cadastrar como Professor
              </Link>
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
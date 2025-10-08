import { z } from 'zod';

/**
 * Gambiarra para mapear os erros do Zod
 */
export function formatZodError(error: z.ZodError): Record<string, string> {
  const formattedErrors: Record<string, string> = {};

  error.errors.forEach((err) => {
    if (err.path && err.path.length > 0) {
      const fieldName = err.path[0] as string;
      
      let message = err.message;
      
      if (message.includes('Invalid input: expected string, received undefined')) {
        message = 'Campo obrigatório';
      } else if (message.includes('Invalid input:')) {
        message = 'Campo obrigatório';
      } else if (message.includes('Expected string, received')) {
        message = 'Campo obrigatório';
      } else if (message.includes('Required')) {
        message = 'Campo obrigatório';
      } else if (message.includes('Invalid email')) {
        message = 'E-mail inválido';
      }
      
      formattedErrors[fieldName] = message;
    }
  });

  return formattedErrors;
}

/**
 * Extrai mensagem de erro para um campo específico
 */
export function getFieldError(error: z.ZodError, fieldName: string): string | undefined {
  const fieldError = error.errors.find(err => err.path[0] === fieldName);
  
  if (!fieldError) return undefined;
  
  const message = fieldError.message;
  
  if (message.includes('Invalid input: expected string, received undefined')) {
    return 'Campo obrigatório';
  } else if (message.includes('Invalid input:')) {
    return 'Campo obrigatório';
  } else if (message.includes('Expected string, received')) {
    return 'Campo obrigatório';
  } else if (message.includes('Required')) {
    return 'Campo obrigatório';
  } else if (message.includes('Invalid email')) {
    return 'E-mail inválido';
  }
  
  return message;
}

/**
 * Mapeamento de nomes de campos para português
 */
export const fieldLabels: Record<string, string> = {
  nome: 'Nome',
  email: 'E-mail',
  senha: 'Senha',
  telefone: 'Telefone',
  cpfCnpj: 'CPF/CNPJ',
};

/**
 * Obtém o label de um campo em português
 */
export function getFieldLabel(fieldName: string): string {
  return fieldLabels[fieldName] || fieldName;
}

import { z } from 'zod';

const customErrorMap: z.ZodErrorMap = (issue, ctx) => {
  if (issue.code === z.ZodIssueCode.invalid_type) {
    if (issue.expected === 'string') {
      return { message: 'Campo obrigatório' };
    }
  }
  if (issue.code === z.ZodIssueCode.too_small) {
    if (issue.type === 'string') {
      return { message: `Mínimo ${issue.minimum} caracteres` };
    }
  }
  if (issue.code === z.ZodIssueCode.too_big) {
    if (issue.type === 'string') {
      return { message: `Máximo ${issue.maximum} caracteres` };
    }
  }
  return { message: ctx.defaultError };
};

z.setErrorMap(customErrorMap);

const cpfCnpjRegex = /^\d{11}$|^\d{14}$/;

export const loginSchema = z.object({
  email: z
    .string({ required_error: 'E-mail é obrigatório' })
    .min(1, 'E-mail é obrigatório')
    .email('E-mail inválido'),
  senha: z
    .string({ required_error: 'Senha é obrigatória' })
    .min(6, 'Senha deve ter pelo menos 6 caracteres'),
});

export type LoginData = z.infer<typeof loginSchema>;

export const registerClientSchema = z.object({
  nome: z
    .string({ required_error: 'Nome é obrigatório' })
    .min(2, 'Nome deve ter pelo menos 2 caracteres')
    .max(100, 'Nome deve ter no máximo 100 caracteres'),
  email: z
    .string({ required_error: 'E-mail é obrigatório' })
    .min(1, 'E-mail é obrigatório')
    .email('E-mail inválido'),
  telefone: z
    .string({ required_error: 'Telefone é obrigatório' })
    .min(10, 'Telefone deve ter pelo menos 10 dígitos')
    .max(15, 'Telefone inválido')
    .regex(/^\d+$/, 'Telefone deve conter apenas números'),
  cpfCnpj: z
    .string({ required_error: 'CPF/CNPJ é obrigatório' })
    .min(11, 'CPF/CNPJ inválido')
    .regex(cpfCnpjRegex, 'CPF deve ter 11 dígitos ou CNPJ deve ter 14 dígitos'),
  senha: z
    .string({ required_error: 'Senha é obrigatória' })
    .min(6, 'Senha deve ter pelo menos 6 caracteres')
    .max(50, 'Senha deve ter no máximo 50 caracteres'),
});

export type RegisterClientData = z.infer<typeof registerClientSchema>;

export const registerProfessionalSchema = z.object({
  nome: z
    .string({ required_error: 'Nome é obrigatório' })
    .min(2, 'Nome deve ter pelo menos 2 caracteres')
    .max(100, 'Nome deve ter no máximo 100 caracteres'),
  email: z
    .string({ required_error: 'E-mail é obrigatório' })
    .min(1, 'E-mail é obrigatório')
    .email('E-mail inválido'),
  telefone: z
    .string({ required_error: 'Telefone é obrigatório' })
    .min(10, 'Telefone deve ter pelo menos 10 dígitos')
    .max(15, 'Telefone inválido')
    .regex(/^\d+$/, 'Telefone deve conter apenas números'),
  cpfCnpj: z
    .string({ required_error: 'CPF/CNPJ é obrigatório' })
    .min(11, 'CPF/CNPJ inválido')
    .regex(cpfCnpjRegex, 'CPF deve ter 11 dígitos ou CNPJ deve ter 14 dígitos'),
  senha: z
    .string({ required_error: 'Senha é obrigatória' })
    .min(6, 'Senha deve ter pelo menos 6 caracteres')
    .max(50, 'Senha deve ter no máximo 50 caracteres'),
});

export type RegisterProfessionalData = z.infer<typeof registerProfessionalSchema>;

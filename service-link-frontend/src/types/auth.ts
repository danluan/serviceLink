export interface User {
  id: string;
  nome: string;
  email: string;
  telefone?: string;
  cpfCnpj?: string;
  perfil: 'CLIENTE' | 'PRESTADOR';
  createdAt?: string;
  updatedAt?: string;
}

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token?: string;
  user: User;
  message?: string;
}

export interface RegisterClientRequest {
  nome: string;
  email: string;
  telefone: string;
  cpfCnpj: string;
  senha: string;
  perfil: 'CLIENTE';
}

export interface RegisterProfessionalRequest {
  nome: string;
  email: string;
  telefone: string;
  cpfCnpj: string;
  senha: string;
  perfil: 'PRESTADOR';
}

export type RegisterRequest = RegisterClientRequest | RegisterProfessionalRequest;

export interface AuthResponse {
  user?: User;
  token?: string;
  message?: string;
}

export interface ApiErrorResponse {
  message: string;
  code?: string;
  field?: string;
  status?: number;
}

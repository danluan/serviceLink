import { apiClient } from '@/lib/api-client';
import {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  AuthResponse,
  User,
} from '@/types/auth';

export class AuthService {
  /**
   * Realiza login do usuário
   */
  static async login(data: LoginRequest): Promise<LoginResponse> {
    const response = await apiClient.post<LoginResponse>(
      '/api/auth/login',
      data,
      { skipAuth: true }
    );

    return response;
  }

  /**
   * Registra um novo usuário (cliente ou profissional)
   */
  static async register(data: RegisterRequest): Promise<AuthResponse> {
    const endpoint = data.perfil === 'PRESTADOR' 
      ? '/api/prestador' 
      : '/api/cliente';

    const response = await apiClient.post<AuthResponse>(
      endpoint,
      data,
      { skipAuth: true }
    );

    return response;
  }

  /**
   * Valida token e retorna dados do usuário
   */
  static async validateToken(token: string): Promise<User> {
    return apiClient.get<User>('/api/auth/validate', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  /**
   * Obtém token do localStorage
   */
  static getToken(): string | null {
    if (typeof window === 'undefined') return null;
    return localStorage.getItem('@servicelink:token');
  }
}

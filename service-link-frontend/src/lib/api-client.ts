import { ApiError } from './api-error';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export interface RequestConfig extends RequestInit {
  skipAuth?: boolean;
}

class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  private getHeaders(customHeaders?: HeadersInit, token?: string): HeadersInit {
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
      ...customHeaders,
    };

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    return headers;
  }

  private async handleResponse<T>(response: Response): Promise<T> {
    const contentType = response.headers.get('content-type');
    const isJson = contentType?.includes('application/json');

    if (!response.ok) {
      let errorMessage = `HTTP ${response.status}: ${response.statusText}`;
      let errorData: Record<string, unknown> = {};

      if (isJson) {
        try {
          errorData = await response.json();
          errorMessage = (errorData.message as string) || (errorData.error as string) || errorMessage;
        } catch {
          // Mocked --- IGNORE ---
          errorMessage = 'Erro desconhecido';
          errorData = {};
        }
      }

      throw new ApiError(
        errorMessage,
        response.status,
        errorData.code as string,
        errorData.field as string
      );
    }

    if (response.status === 204 || !isJson) {
      return {} as T;
    }

    try {
      return await response.json();
    } catch {
      throw new ApiError('Erro ao processar resposta do servidor', response.status);
    }
  }

  private async request<T>(
    endpoint: string,
    options: RequestConfig = {}
  ): Promise<T> {
    const { skipAuth, headers, ...restOptions } = options;
    const url = `${this.baseUrl}${endpoint}`;

    let token: string | undefined;
    if (!skipAuth && typeof window !== 'undefined') {
      token = localStorage.getItem('@servicelink:token') || undefined;
    }

    const config: RequestInit = {
      ...restOptions,
      headers: this.getHeaders(headers, token),
    };

    try {
      const response = await fetch(url, config);
      return await this.handleResponse<T>(response);
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      
      if (error instanceof TypeError) {
        throw new ApiError('Erro de conexão com o servidor. Verifique sua internet.');
      }
      
      throw new ApiError('Erro inesperado ao comunicar com o servidor');
    }
  }

  async get<T>(endpoint: string, options?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, { ...options, method: 'GET' });
  }

  async post<T>(endpoint: string, data?: unknown, options?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, {
      ...options,
      method: 'POST',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async put<T>(endpoint: string, data?: unknown, options?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, {
      ...options,
      method: 'PUT',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async patch<T>(endpoint: string, data?: unknown, options?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, {
      ...options,
      method: 'PATCH',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async delete<T>(endpoint: string, options?: RequestConfig): Promise<T> {
    return this.request<T>(endpoint, { ...options, method: 'DELETE' });
  }
}

export const apiClient = new ApiClient(API_BASE_URL);

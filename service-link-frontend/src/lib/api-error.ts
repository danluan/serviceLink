import { ApiErrorResponse } from '@/types/auth';

export class ApiError extends Error {
  public status?: number;
  public code?: string;
  public field?: string;

  constructor(message: string, status?: number, code?: string, field?: string) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.code = code;
    this.field = field;
  }

  static fromResponse(data: ApiErrorResponse): ApiError {
    return new ApiError(
      data.message || 'Erro desconhecido',
      data.status,
      data.code,
      data.field
    );
  }
}

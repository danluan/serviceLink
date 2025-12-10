'use client';

import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { useRouter } from 'next/navigation';
import { AuthService } from '@/services/auth.service';
import { LoginRequest, User } from '@/types/auth';

interface AuthContextData {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
  updateUser: (user: User) => void;
}

const AuthContext = createContext<AuthContextData>({} as AuthContextData);

const AUTH_STORAGE_KEY = '@servicelink:auth';
const TOKEN_STORAGE_KEY = '@servicelink:token';

interface AuthProviderProps {
  children: React.ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const loadStoredAuth = () => {
      try {
        if (typeof window === 'undefined') return;

        const storedToken = localStorage.getItem(TOKEN_STORAGE_KEY);
        const storedUser = localStorage.getItem(AUTH_STORAGE_KEY);

        if (storedToken && storedUser) {
          setToken(storedToken);
          setUser(JSON.parse(storedUser));
        }
      } catch (error) {
        console.error('Erro ao carregar dados de autenticação:', error);
        localStorage.removeItem(TOKEN_STORAGE_KEY);
        localStorage.removeItem(AUTH_STORAGE_KEY);
      } finally {
        setIsLoading(false);
      }
    };

    loadStoredAuth();
  }, []);

  useEffect(() => {
    if (typeof window === 'undefined') return;

    if (user && token) {
      localStorage.setItem(TOKEN_STORAGE_KEY, token);
      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(user));
    } else {
      localStorage.removeItem(TOKEN_STORAGE_KEY);
      localStorage.removeItem(AUTH_STORAGE_KEY);
    }
  }, [user, token]);

  const login = useCallback(async (credentials: LoginRequest) => {
    try {
      setIsLoading(true);
      const response = await AuthService.login(credentials);

      if (response.user) {
        setUser(response.user);
        setToken(response.token || '');

        if (response.user.perfil === 'PRESTADOR') {
          router.push('/professional/dashboard');
        } else {
          router.push('/client/home');
        }
      }
    } catch (error) {
      setUser(null);
      setToken(null);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [router]);

  const logout = useCallback(() => {
    setUser(null);
    setToken(null);
    
    if (typeof window !== 'undefined') {
      localStorage.removeItem(TOKEN_STORAGE_KEY);
      localStorage.removeItem(AUTH_STORAGE_KEY);
    }

    router.push('/login');
  }, [router]);

  const updateUser = useCallback((updatedUser: User) => {
    setUser(updatedUser);
  }, []);

  const value = {
    user,
    token,
    isAuthenticated: !!user && !!token,
    isLoading,
    login,
    logout,
    updateUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }

  return context;
}

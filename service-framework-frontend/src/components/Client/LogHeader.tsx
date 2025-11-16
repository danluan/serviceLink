'use client';
import { Bell} from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { useAuth } from '@/contexts/AuthContext';
import Link from 'next/link';


const ClienteNavbarSimples = () => {
    const { user, logout } = useAuth();

    return (
        <header className="bg-white shadow border-b border-border sticky top-0 z-10">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-3 flex justify-between items-center">

                {/* Lado Esquerdo: Apenas a Logo (Nome do Aplicativo) */}
                <div>
                    <h1 className="text-2xl font-bold text-foreground">ServiceLink</h1>
                </div>

                {/* Lado Direito: Notificações e Perfil Dropdown */}
                <div className="flex items-center gap-4">

                    {/* 1. Botão de Notificações */}
                    <Button
                        variant="ghost"
                        size="icon"
                        className="relative text-foreground hover:bg-muted/50"
                    >
                        <Bell className="h-5 w-5" />
                        {/* Badge de notificação com count fixo de 3 */}
                        <Badge className="absolute -top-1 -right-1 h-5 w-5 flex items-center justify-center p-0 bg-destructive text-destructive-foreground text-xs">
                            3
                        </Badge>
                    </Button>

                    {/* 2. Profile Dropdown */}
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button
                                variant="ghost"
                                className="relative h-10 w-10 rounded-full p-0 hover:ring-2 hover:ring-primary/50 transition-all"
                            >
                                <Avatar className="h-10 w-10">
                                    <AvatarImage alt="Perfil" />
                                    {/* Avatar Fallback com ícone de @ */}
                                    <AvatarFallback className="bg-primary text-primary-foreground">
                                        @
                                    </AvatarFallback>
                                </Avatar>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end" className="w-56 bg-popover">

                            {/* Informações do Usuário no Menu */}
                            <DropdownMenuLabel>
                                <div className="flex flex-col space-y-1">
                                    <p className="text-sm font-medium">{user?.nome}</p>
                                    <p className="text-xs text-muted-foreground">
                                        {user?.email}
                                    </p>
                                </div>
                            </DropdownMenuLabel>
                            <DropdownMenuSeparator />

                            {/* Itens de Navegação */}
                            <Link href="/professional/profile">
                                <DropdownMenuItem className="cursor-pointer">
                                    Meu Perfil
                                </DropdownMenuItem>
                            </Link>
                            <DropdownMenuItem className="cursor-pointer">
                                Configurações
                            </DropdownMenuItem>
                            <DropdownMenuItem className="cursor-pointer">
                                Ajuda
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />

                            {/* Botão Sair */}
                            <DropdownMenuItem
                                onClick={logout}
                                className="cursor-pointer text-destructive focus:bg-destructive/10"
                            >
                                Sair
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
            </div>
        </header>
    );
};

export default ClienteNavbarSimples;
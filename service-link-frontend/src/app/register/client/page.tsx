'use client';

import { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

export default function ClientRegister() {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        phone: '',
        cpf: '',
        password: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log('Form data:', formData);
    };

    return (
        <div className="h-screen w-auto m-auto max-w-xl flex flex-col justify-center items-center gap-4">
            <h1 className="text-3xl font-bold mb-6 m-0">Cadastro de Cliente</h1>
            <form className="flex flex-col gap-4 w-full" onSubmit={handleSubmit}>
                <Input
                    name="name"
                    type="text"
                    placeholder="Digite seu nome completo"
                    value={formData.name}
                    onChange={handleChange}
                    required
                />
                
                <Input
                    name="email"
                    type="email"
                    placeholder="Digite seu e-mail"
                    value={formData.email}
                    onChange={handleChange}
                    required
                />

                <Input
                    name="phone"
                    type="tel"
                    placeholder="Digite seu telefone (00) 00000-0000"
                    value={formData.phone}
                    onChange={handleChange}
                    required
                />

                <Input
                    name="cpf"
                    type="text"
                    placeholder="Digite seu CPF 000.000.000-00"
                    value={formData.cpf}
                    onChange={handleChange}
                    required
                />

                <Input
                    name="password"
                    type="password"
                    placeholder="Digite sua senha"
                    value={formData.password}
                    onChange={handleChange}
                    required
                />

                <Button type="submit">
                    Cadastrar
                </Button>
            </form>
            <div className="flex flex-col items-center gap-2 mt-4">
                <p>Já possui uma conta?</p>
                <Button>
                    <a href="/login">Fazer Login</a>
                </Button>
            </div>
        </div>
    );
}
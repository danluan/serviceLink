"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

import { signIn } from "next-auth/react";

const LoginPage = () => {
  async function handleLogin(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const email = formData.get("email");
    const password = formData.get("password");
    
    const data = {
      email: email,
      password: password
    };

    signIn("credentials", {
      ...data,
      callbackUrl: "/client/home"
    })

  }

  return (
    <div className="h-screen w-auto m-auto max-w-xl flex flex-col justify-center items-center gap-4">
      <h1 className="text-3xl font-bold mb-6 m-0">Entrar</h1>
      <form onSubmit={handleLogin} className="flex flex-col gap-4 w-full">
        <Input name="email" type="text" placeholder="Digite seu e-mail" />
        <Input name="password" type="password" placeholder="Digite sua senha" />
        <Button>
          Entrar
        </Button>
      </form>
      <div className="flex flex-col items-center gap-2 mt-4">
        <p>Não possui uma conta?</p>
        <div className="flex gap-4">
          <Button>
            <a href="/register/client">Cadastre-se como Cliente</a>
          </Button>
          <Button>
            <a href="/register/professional">Cadastre-se como Profissional</a>
          </Button>
        </div>
      </div>
    </div>
  )
}

export default LoginPage;
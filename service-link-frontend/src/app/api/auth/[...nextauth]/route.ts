import NextAuth from "next-auth/next"
import CredentialsProvider from "next-auth/providers/credentials";
import { NextAuthOptions } from "next-auth"

const authOptions: NextAuthOptions = {
    providers: [
        CredentialsProvider({
            name: "Credentials",
            credentials: {
                email: { label: "Email", type: "text", placeholder: "email@example.com" },
                password: { label: "Password", type: "password" }
            },
            async authorize(credentials, req) {
                if (!credentials) {
                    return null;
                }

                const res = await fetch("http://localhost:8080/api/auth/login", {
                    method: "POST",
                    body: JSON.stringify({
                        email: credentials.email,
                        senha: credentials.password
                    }),
                    headers: { "Content-Type": "application/json" }
                });

                console.log(res);

                if (res.ok) {
                    const user = await res.json();
                    return user;
                }
                return null;
            }
        })
    ]
}
const handler = NextAuth(authOptions);

export { handler as GET, handler as POST }
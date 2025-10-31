'use client';
import { useState, useEffect } from "react";
import Navbar from "@/components/Professional/LogadoNavbar";
import ServiceSearch from "@/components/SearchServices/ServiceSearch";
import ServiceCard from "@/components/SearchServices/ServiceCard";
import WhatsAppButton from "@/components/WhatsAppButton";

interface Service {
    id: number;
    nome: string;
    descricao: string;
    precoBase: number;
    categoria: string;
}

const Page = () => {
    const [services, setServices] = useState<Service[]>([]);
    const [loading, setLoading] = useState(false);

    const fetchServices = async (filters?: {
        searchTerm?: string;
        minPrice?: string;
        maxPrice?: string;
        category?: string;
    }) => {
        try {
            setLoading(true);
            const token = localStorage.getItem('@servicelink:token');
            if (!token) {
                console.error('Token não encontrado.');
                return;
            }

            // Monta os parâmetros da URL de forma dinâmica
            const params = new URLSearchParams();
            if (filters?.searchTerm) params.append("nome", filters.searchTerm);
            if (filters?.category && filters.category !== "all") params.append("categoria", filters.category);
            if (filters?.minPrice) params.append("precoMin", filters.minPrice);
            if (filters?.maxPrice) params.append("precoMax", filters.maxPrice);

            const queryString = params.toString();
            const url = queryString
                ? `http://localhost:8080/api/servico?${queryString}`
                : `http://localhost:8080/api/servico`;

            const res = await fetch(url, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!res.ok) throw new Error(`Erro HTTP! Status: ${res.status}`);

            const data = await res.json();

            const mapped = data.map((s: any) => ({
                id: s.id,
                nome: s.nome,
                descricao: s.descricao,
                precoBase: s.precoBase,
                categoria: s.categoria,
            }));

            setServices(mapped);
        } catch (error) {
            console.error("Erro ao buscar serviços:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchServices();
    }, []);

    const handleFilter = (searchTerm: string, minPrice: string, maxPrice: string, category: string) => {
        fetchServices({ searchTerm, minPrice, maxPrice, category });
    };

    return (
        <div className="min-h-screen bg-background">
            <Navbar />
            <main className="container mx-auto px-4 py-8">
                <h1 className="text-4xl font-bold text-foreground mb-8 text-center">
                    Encontre o Serviço Ideal
                </h1>

                <ServiceSearch onFilter={handleFilter} />

                {loading ? (
                    <div className="text-center py-12 text-muted-foreground">
                        Carregando serviços...
                    </div>
                ) : (
                    <>
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
                            {services.map(service => (
                                <ServiceCard key={service.id} service={service} />
                            ))}
                        </div>

                        {services.length === 0 && (
                            <div className="text-center py-12">
                                <p className="text-muted-foreground text-lg">
                                    Nenhum serviço encontrado com os filtros aplicados.
                                </p>
                            </div>
                        )}
                    </>
                )}
            </main>
            <WhatsAppButton />
        </div>
    );
};

export default Page;

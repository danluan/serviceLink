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
    imagemUrl: string;
}

const Service = () => {

}

const Page = () => {
    const [services, setServices] = useState<Service[]>([]);
    const [filteredServices, setFilteredServices] = useState<Service[]>([]);

    useEffect(() => {
        async function fetchServices(){
            try{
                const token = localStorage.getItem('@servicelink:token');

                if (!token) {
                    console.error('Token não encontrado.');
                    return;
                }

                const res = await fetch('http://localhost:8080/api/servico', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                })
                if(!res.ok){
                    throw new Error(`Erro HTTP! Status: ${res.status}`)
                }
                const data = await res.json();
                const mapped = data.map((s: any) => ({
                    id: s.id,
                    nome: s.nome,
                    descricao: s.descricao,
                    precoBase: `R$ ${s.precoBase.toFixed(2)}`,
                    categoria: s.categoria,
                }));
                setServices(mapped);
                setFilteredServices(mapped);
            }catch (error){
                console.error("Erro ao buscar servicos:", error);
            }
        }
        fetchServices();
    }, []);

    const handleFilter = (searchTerm: string, minPrice: string, maxPrice: string, category: string) => {
        let filtered = [...services];

        if (searchTerm) {
            filtered = filtered.filter(service =>
                service.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
                service.descricao.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }

        if (category && category !== "all") {
            filtered = filtered.filter(service => service.categoria === category);
        }

        if (minPrice) {
            const min = parseFloat(minPrice);
            filtered = filtered.filter(service => {
                const price = service.precoBase;
                return !isNaN(price) && price >= min;
            });
        }

        if (maxPrice) {
            const max = parseFloat(maxPrice);
            filtered = filtered.filter(service => {
                const price = service.precoBase;
                return !isNaN(price) && price <= max;
            });
        }

        setFilteredServices(filtered);
    };

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="container mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold text-foreground mb-8 text-center">
          Encontre o Serviço Ideal
        </h1>
        
        <ServiceSearch onFilter={handleFilter} />
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
          {filteredServices.map(service => (
            <ServiceCard key={service.id} service={service} />
          ))}
        </div>

        {filteredServices.length === 0 && (
          <div className="text-center py-12">
            <p className="text-muted-foreground text-lg">
              Nenhum serviço encontrado com os filtros aplicados.
            </p>
          </div>
        )}
      </main>
        <WhatsAppButton/>
    </div>
  );
};

export default Page;

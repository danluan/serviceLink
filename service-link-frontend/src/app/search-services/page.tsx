'use client';
import { useState } from "react";
import Navbar from "@/components/Professional/LogadoNavbar";
import ServiceSearch from "@/components/SearchServices/ServiceSearch";
import ServiceCard from "@/components/SearchServices/ServiceCard";

interface Service {
  id: number;
  name: string;
  description: string;
  price: string;
  category: string;
}

const mockServices: Service[] = [
  {
    id: 1,
    name: "Limpeza Residencial Completa",
    description: "Limpeza profunda de todos os cômodos, incluindo cozinhas e banheiros. Deixa sua casa brilhando!",
    price: "R$ 180,00",
    category: "Limpeza"
  },
  {
    id: 2,
    name: "Conserto de Vazamentos",
    description: "Reparo de vazamentos em torneiras, canos e tubulações. Serviço rápido e garantido.",
    price: "A partir de R$ 80,00/hora",
    category: "Encanamento"
  },
  {
    id: 3,
    name: "Corte de Grama",
    description: "Corte e manutenção de gramados, deixando seu jardim impecável e bem cuidado.",
    price: "R$ 120,00",
    category: "Jardinagem"
  },
  {
    id: 4,
    name: "Instalação Elétrica",
    description: "Instalação e manutenção de sistemas elétricos residenciais com segurança e qualidade.",
    price: "A partir de R$ 100,00/hora",
    category: "Elétrica"
  },
  {
    id: 5,
    name: "Babá Especializada",
    description: "Cuidado profissional para crianças com experiência e referências verificadas.",
    price: "R$ 25,00/hora",
    category: "Babá"
  },
  {
    id: 6,
    name: "Limpeza de Estofados",
    description: "Limpeza profissional de sofás, cadeiras e colchões, removendo manchas e odores.",
    price: "R$ 150,00",
    category: "Limpeza"
  }
];

const Page = () => {
  const [filteredServices, setFilteredServices] = useState<Service[]>(mockServices);

  const handleFilter = (searchTerm: string, minPrice: string, maxPrice: string, category: string) => {
    let filtered = mockServices;

    if (searchTerm) {
      filtered = filtered.filter(service => 
        service.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        service.description.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (category && category !== "all") {
      filtered = filtered.filter(service => service.category === category);
    }

    if (minPrice) {
      const min = parseFloat(minPrice);
      filtered = filtered.filter(service => {
        const priceMatch = service.price.match(/R\$\s*(\d+(?:,\d+)?(?:\.\d+)?)/);
        if (priceMatch) {
          const price = parseFloat(priceMatch[1].replace(',', '.'));
          return price >= min;
        }
        return true;
      });
    }

    if (maxPrice) {
      const max = parseFloat(maxPrice);
      filtered = filtered.filter(service => {
        const priceMatch = service.price.match(/R\$\s*(\d+(?:,\d+)?(?:\.\d+)?)/);
        if (priceMatch) {
          const price = parseFloat(priceMatch[1].replace(',', '.'));
          return price <= max;
        }
        return true;
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
    </div>
  );
};

export default Page;

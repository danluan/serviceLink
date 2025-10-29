import { useState } from "react";
import { Search } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

interface ServiceSearchProps {
  onFilter: (searchTerm: string, minPrice: string, maxPrice: string, category: string) => void;
}

const ServiceSearch = ({ onFilter }: ServiceSearchProps) => {
  const [searchTerm, setSearchTerm] = useState("");
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");
  const [category, setCategory] = useState("all");

  const handleApplyFilters = () => {
    onFilter(searchTerm, minPrice, maxPrice, category);
  };

  return (
    <div className="bg-card border border-border rounded-lg p-6 shadow-card">
      <div className="space-y-4">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-5 h-5" />
          <Input
            type="text"
            placeholder="Buscar por serviço..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label className="text-sm font-medium text-foreground mb-2 block">
              Preço Mínimo
            </label>
            <Input
              type="number"
              placeholder="R$ 0"
              value={minPrice}
              onChange={(e) => setMinPrice(e.target.value)}
              min="0"
            />
          </div>

          <div>
            <label className="text-sm font-medium text-foreground mb-2 block">
              Preço Máximo
            </label>
            <Input
              type="number"
              placeholder="R$ 1000"
              value={maxPrice}
              onChange={(e) => setMaxPrice(e.target.value)}
              min="0"
            />
          </div>

          <div>
            <label className="text-sm font-medium text-foreground mb-2 block">
              Categoria
            </label>
            <Select value={category} onValueChange={setCategory}>
              <SelectTrigger>
                <SelectValue placeholder="Selecione..." />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Todas</SelectItem>
                <SelectItem value="LIMPEZA">Limpeza</SelectItem>
                <SelectItem value="HIDRAULICA">Hidráulica</SelectItem>
                <SelectItem value="ELETRICA">Elétrica</SelectItem>
                <SelectItem value="PINTURA">Pintura</SelectItem>
                <SelectItem value="JARDINAGEM">Jardinagem</SelectItem>
                  <SelectItem value="COZINHA">Cozinha</SelectItem>
                  <SelectItem value="OUTRAS">Outras</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="flex items-end">
            <Button onClick={handleApplyFilters} className="w-full">
              Aplicar Filtros
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ServiceSearch;

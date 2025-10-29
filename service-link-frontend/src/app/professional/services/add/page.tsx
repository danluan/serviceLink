'use client';

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/contexts/AuthContext";
import { useFormValidationServices } from "@/hooks/useFormValidation";
import { z } from "zod";
import { toast } from "sonner";
import LogadoNavbar from "@/components/Professional/LogadoNavbar";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Plus, Trash2, Send } from "lucide-react";

const servicoSchema = z.object({
  nome: z.string().min(3, "Nome deve ter no mínimo 3 caracteres").max(100, "Nome muito longo"),
  descricao: z.string().min(10, "Descrição deve ter no mínimo 10 caracteres").max(500, "Descrição muito longa"),
  precoBase: z.number().positive("Preço deve ser positivo").min(1, "Preço mínimo é R$ 1,00"),
  categoria: z.string().min(1, "Categoria é obrigatória"),
  imagemUrl: z.string().url("URL inválida").optional().or(z.literal("")),
});

type ServicoDTO = z.infer<typeof servicoSchema>;

const categorias = [
    "LIMPEZA",
    "HIDRAULICA",
    "ELETRICA",
    "PINTURA",
    "JARDINAGEM",
    "COZINHA",
    "OUTRAS"
];

const Page = () => {
  const { user } = useAuth();
  const router = useRouter();
  const { errors, validate, clearErrors } = useFormValidationServices(servicoSchema);
  const [servicos, setServicos] = useState<ServicoDTO[]>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [formData, setFormData] = useState<ServicoDTO>({
    nome: "",
    descricao: "",
    precoBase: 0,
    categoria: "",
    imagemUrl: "",
  });

  const handleInputChange = (field: keyof ServicoDTO, value: string | number) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const handleAddService = () => {
    if (validate(formData)) {
      setServicos((prev) => [...prev, formData]);
      setFormData({
        nome: "",
        descricao: "",
        precoBase: 0,
        categoria: "",
        imagemUrl: "",
      });
      clearErrors();
      setIsDialogOpen(false);
      toast.success("Serviço adicionado à lista!");
    } else {
      toast.error("Por favor, corrija os erros no formulário");
    }
  };

  const handleRemoveService = (index: number) => {
    setServicos((prev) => prev.filter((_, i) => i !== index));
    toast.info("Serviço removido da lista");
  };

  const handleSubmitAll = async () => {
      if (servicos.length === 0) {
          toast.error("Adicione pelo menos um serviço antes de enviar");
          return;
      }

      const authDataString = localStorage.getItem('@servicelink:auth');

      if (!authDataString) {
          toast.error("Dados de autenticação não encontrados. Faça login novamente.");
          return;
      }

      let authData;
      try {
          authData = JSON.parse(authDataString);
      } catch (e) {
          toast.error("Erro ao processar dados de autenticação.");
          console.error("Erro de parse JSON:", e);
          return;
      }

      const prestadorId = authData.profileId;

      const token = localStorage.getItem('@servicelink:token');

      if (!prestadorId) {
          toast.error("ID do prestador não encontrado. Tente relogar.");
          return;
      }

      if (!token) {
          toast.error("Token de autenticação não encontrado. Faça login novamente.");
          return;
      }

    setIsSubmitting(true);
    try {
      const response = await fetch(`http://localhost:8080/api/servico/prestador/${prestadorId}`, {
        method: "POST",
        headers: {
            'Authorization': `Bearer ${token}`,
          'Content-Type': "application/json",
        },
        body: JSON.stringify(servicos),
      });

      if (!response.ok) {
        throw new Error("Erro ao enviar serviços");
      }

        toast.success(`${servicos.length} serviço(s) cadastrado(s) com sucesso! Redirecionando...`);
        setServicos([]);

        router.push('/professional/services')
    } catch (error) {
      toast.error("Erro ao enviar serviços. Tente novamente.");
      console.error(error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen bg-background">
      <LogadoNavbar />
      <main className="container mx-auto px-4 py-8">
        <div className="max-w-4xl mx-auto">
          <div className="flex items-center justify-between mb-8">
            <div>
              <h1 className="text-4xl font-bold text-foreground mb-2">
                Cadastro de Serviços
              </h1>
              <p className="text-muted-foreground">
                Adicione seus serviços e envie todos de uma vez
              </p>
            </div>
            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
              <DialogTrigger asChild>
                <Button size="lg" className="gap-2">
                  <Plus className="h-5 w-5" />
                  Adicionar Novo Serviço
                </Button>
              </DialogTrigger>
              <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
                <DialogHeader>
                  <DialogTitle>Novo Serviço</DialogTitle>
                  <DialogDescription>
                    Preencha os dados do serviço. Você pode adicionar vários antes de enviar.
                  </DialogDescription>
                </DialogHeader>

                <div className="space-y-4 py-4">
                  <div className="space-y-2">
                    <Label htmlFor="nome">Nome do Serviço *</Label>
                    <Input
                      id="nome"
                      value={formData.nome}
                      onChange={(e) => handleInputChange("nome", e.target.value)}
                      placeholder="Ex: Limpeza Residencial Completa"
                    />
                    {errors.nome && (
                      <p className="text-sm text-destructive">{errors.nome}</p>
                    )}
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="descricao">Descrição *</Label>
                    <Textarea
                      id="descricao"
                      value={formData.descricao}
                      onChange={(e) => handleInputChange("descricao", e.target.value)}
                      placeholder="Descreva detalhadamente o serviço oferecido..."
                      rows={4}
                    />
                    {errors.descricao && (
                      <p className="text-sm text-destructive">{errors.descricao}</p>
                    )}
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="precoBase">Preço Base (R$) *</Label>
                    <Input
                      id="precoBase"
                      type="number"
                      step="0.01"
                      min="1"
                      value={formData.precoBase || ""}
                      onChange={(e) => handleInputChange("precoBase", parseFloat(e.target.value) || 0)}
                      placeholder="150.00"
                    />
                    {errors.precoBase && (
                      <p className="text-sm text-destructive">{errors.precoBase}</p>
                    )}
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="categoria">Categoria *</Label>
                    <Select
                      value={formData.categoria}
                      onValueChange={(value) => handleInputChange("categoria", value)}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder="Selecione uma categoria" />
                      </SelectTrigger>
                      <SelectContent>
                        {categorias.map((cat) => (
                          <SelectItem key={cat} value={cat}>
                            {cat}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                    {errors.categoria && (
                      <p className="text-sm text-destructive">{errors.categoria}</p>
                    )}
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="imagemUrl">URL da Imagem (opcional)</Label>
                    <Input
                      id="imagemUrl"
                      type="url"
                      value={formData.imagemUrl}
                      onChange={(e) => handleInputChange("imagemUrl", e.target.value)}
                      placeholder="https://exemplo.com/imagem.jpg"
                    />
                    {errors.imagemUrl && (
                      <p className="text-sm text-destructive">{errors.imagemUrl}</p>
                    )}
                  </div>
                </div>

                <DialogFooter>
                  <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                    Cancelar
                  </Button>
                  <Button onClick={handleAddService}>Adicionar à Lista</Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </div>

          {servicos.length === 0 ? (
            <Card>
              <CardContent className="flex flex-col items-center justify-center py-16">
                <p className="text-muted-foreground text-lg mb-4">
                  Nenhum serviço adicionado ainda
                </p>
                <p className="text-sm text-muted-foreground">
                  Clique em "Adicionar Novo Serviço" para começar
                </p>
              </CardContent>
            </Card>
          ) : (
            <>
              <div className="grid gap-4 mb-8">
                {servicos.map((servico, index) => (
                  <Card key={index}>
                    <CardHeader>
                      <div className="flex items-start justify-between">
                        <div className="flex-1">
                          <CardTitle>{servico.nome}</CardTitle>
                          <CardDescription>{servico.categoria}</CardDescription>
                        </div>
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => handleRemoveService(index)}
                          className="text-destructive hover:text-destructive"
                        >
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      </div>
                    </CardHeader>
                    <CardContent>
                      <p className="text-sm text-muted-foreground mb-2">
                        {servico.descricao}
                      </p>
                      <p className="text-lg font-semibold text-primary">
                        R$ {servico.precoBase.toFixed(2)}
                      </p>
                      {servico.imagemUrl && (
                        <p className="text-xs text-muted-foreground mt-2">
                          Imagem: {servico.imagemUrl}
                        </p>
                      )}
                    </CardContent>
                  </Card>
                ))}
              </div>

              <div className="flex justify-end">
                <Button
                  size="lg"
                  onClick={handleSubmitAll}
                  disabled={isSubmitting}
                  className="gap-2"
                >
                  <Send className="h-5 w-5" />
                  Finalizar Cadastro e Enviar Serviços ({servicos.length})
                </Button>
              </div>
            </>
          )}
        </div>
      </main>
    </div>
  );
};

export default Page;

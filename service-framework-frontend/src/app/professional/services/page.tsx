'use client';

import { useState, useEffect } from "react";
import { useRouter } from 'next/navigation';
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Sheet, SheetContent, SheetDescription, SheetHeader, SheetTitle } from "@/components/ui/sheet";
import { Loader2, Pencil, Trash2, Plus } from "lucide-react";
import { useFormValidationServices } from "@/hooks/useFormValidation";
import { z } from "zod";
import LogadoNavbar from "@/components/Professional/LogadoNavbar";
import WhatsAppButton from "@/components/WhatsAppButton";
import { toast } from "sonner";

const servicoSchema = z.object({
    nome: z.string().min(3, "Nome deve ter no mínimo 3 caracteres").max(100, "Nome muito longo"),
    descricao: z.string().min(10, "Descrição deve ter no mínimo 10 caracteres").max(500, "Descrição muito longa"),
    precoBase: z.coerce.number().min(1, "Preço deve ser maior ou igual a R$ 1,00").positive("Preço deve ser positivo"),
    categoria: z.string().min(1, "Selecione uma categoria"),
    imagemUrl: z.string().url("URL inválida").optional().or(z.literal("")), // Permite string vazia como opcional
});

interface Servico extends z.infer<typeof servicoSchema> {
    id: number;
}

// O tipo do formulário de edição deve ser String, pois o input é string
interface EditFormDTO {
    nome: string;
    descricao: string;
    precoBase: string; // Mantido como string para o input
    categoria: string;
    imagemUrl: string;
}

const categorias = ["Limpeza", "Jardinagem", "Elétrica", "Encanamento", "Babá", "Outros"];

const Page = () => {
    const [servicos, setServicos] = useState<Servico[]>([]);
    const [loading, setLoading] = useState(true);
    const [editingService, setEditingService] = useState<Servico | null>(null);
    const [deletingServiceId, setDeletingServiceId] = useState<number | null>(null);
    const [isEditSheetOpen, setIsEditSheetOpen] = useState(false);
    const [isSaving, setIsSaving] = useState(false);

    const router = useRouter();

    const { errors, validate, clearErrors } = useFormValidationServices(servicoSchema);

    const [editForm, setEditForm] = useState<EditFormDTO>({
        nome: "",
        descricao: "",
        precoBase: "",
        categoria: "",
        imagemUrl: "",
    });

    useEffect(() => {
        fetchServicos();
    }, []);

    const getAuthData = () => {
        const authDataString = localStorage.getItem('@servicelink:auth');
        const token = localStorage.getItem('@servicelink:token');

        if (!authDataString || !token) {
            return null;
        }

        try {
            const authData = JSON.parse(authDataString);
            return {
                prestadorId: authData.profileId,
                token: token // Usa o token de onde estiver armazenado
            };
        } catch (e) {
            console.error("Erro de parse JSON:", e);
            return null;
        }
    };

    const fetchServicos = async () => {
        const auth = getAuthData();
        if (!auth) {
            toast.error("Faça login para acessar seus serviços.");
            setLoading(false);
            return;
        }

        try {
            setLoading(true);

            const response = await fetch(`http://localhost:8080/api/servico/prestador/${auth.prestadorId}`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${auth.token}`,
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                throw new Error("Erro ao buscar serviços");
            }

            const data = await response.json();
            setServicos(data);
        } catch (error) {
            toast.error("Não foi possível carregar os serviços");
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (servico: Servico) => {
        setEditingService(servico);
        setEditForm({
            nome: servico.nome,
            descricao: servico.descricao,
            // CONVERSÃO: Converte o número para string para o input HTML
            precoBase: servico.precoBase.toFixed(2),
            categoria: servico.categoria,
            imagemUrl: servico.imagemUrl || "",
        });
        clearErrors();
        setIsEditSheetOpen(true);
    };

    const handleSaveEdit = async () => {
        // 3. Validação: Cria o objeto de dados com tipagem numérica para o Zod
        const formData = {
            nome: editForm.nome,
            descricao: editForm.descricao,
            // CONVERSÃO: Converte a string do input para número para o Zod validar
            precoBase: editForm.precoBase, // O Zod fará a coerção (z.coerce.number())
            categoria: editForm.categoria,
            // Garante que se for vazio, passa como string vazia (que é aceito pelo optional().or(z.literal("")))
            imagemUrl: editForm.imagemUrl,
        };

        // O validate do hook deve aceitar o formData (string-based) e retornar true/false
        if (!validate(formData)) {
            toast.error("Por favor, corrija os erros no formulário de edição.");
            return;
        }

        // Converte para o DTO final antes de enviar, já que o Zod validou
        const finalData = servicoSchema.parse(formData);

        const auth = getAuthData();
        if (!auth) {
            toast.error("Sessão expirada. Faça login novamente.");
            return;
        }

        try {
            setIsSaving(true);

            const response = await fetch(`http://localhost:8080/api/servico/${editingService?.id}`, {
                method: "PUT",
                headers: {
                    "Authorization": `Bearer ${auth.token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(finalData), // Envia os dados já validados e tipados
            });

            if (!response.ok) {
                // Tenta ler o erro do corpo da resposta
                const errorData = await response.json().catch(() => ({ message: 'Erro desconhecido' }));
                throw new Error(errorData.message || "Erro ao atualizar serviço");
            }

            const updatedServico = await response.json();

            setServicos((prev) =>
                prev.map((s) => (s.id === editingService?.id ? updatedServico : s))
            );

            toast.success("Serviço atualizado com sucesso!");

            setIsEditSheetOpen(false);
            setEditingService(null);
        } catch (error) {
            toast.error(error instanceof Error ? error.message : "Não foi possível atualizar o serviço");
        } finally {
            setIsSaving(false);
        }
    };

    const handleDelete = async () => {
        if (!deletingServiceId) return;

        const auth = getAuthData();
        if (!auth) {
            toast.error("Sessão expirada. Faça login novamente.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/servico/${deletingServiceId}`, {
                method: "DELETE",
                headers: {
                    "Authorization": `Bearer ${auth.token}`,
                },
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: 'Erro desconhecido' }));
                throw new Error(errorData.message || "Erro ao excluir serviço");
            }

            setServicos((prev) => prev.filter((s) => s.id !== deletingServiceId));

            toast.success("Serviço excluído com sucesso!");

            setDeletingServiceId(null);
        } catch (error) {
            toast.error(error instanceof Error ? error.message : "Não foi possível excluir o serviço");
        }
    };

  return (
    <div className="min-h-screen bg-background">
      <LogadoNavbar />
      
      <main className="container mx-auto px-4 py-8">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-4xl font-bold text-foreground">Meus Serviços</h1>
            <p className="text-muted-foreground mt-2">
              Gerencie seus serviços cadastrados
            </p>
          </div>
          <Button
            size="lg"
            onClick={() => router.push("/professional/services/add")}
            className="gap-2"
          >
            <Plus className="h-5 w-5" />
            Adicionar Novo Serviço
          </Button>
        </div>

        {loading ? (
          <div className="flex items-center justify-center py-20">
            <Loader2 className="h-8 w-8 animate-spin text-primary" />
          </div>
        ) : servicos.length === 0 ? (
          <div className="text-center py-20">
            <p className="text-muted-foreground text-lg mb-4">
              Você ainda não cadastrou nenhum serviço
            </p>
            <Button onClick={() => router.push("/professional/services/add")}>
              Cadastrar Primeiro Serviço
            </Button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {servicos.map((servico) => (
              <Card key={servico.id} className="flex flex-col h-full">
                <CardHeader>
                  <div className="flex items-start justify-between gap-2 mb-2">
                    <CardTitle className="text-xl">{servico.nome}</CardTitle>
                    <Badge variant="secondary" className="shrink-0">
                      {servico.categoria}
                    </Badge>
                  </div>
                  <CardDescription>{servico.descricao}</CardDescription>
                </CardHeader>
                <CardContent className="flex-grow">
                  <div className="text-2xl font-bold text-primary">
                    R$ {servico.precoBase.toFixed(2)}
                  </div>
                </CardContent>
                <CardFooter className="gap-2">
                  <Button
                    variant="outline"
                    className="flex-1 gap-2"
                    onClick={() => handleEdit(servico)}
                  >
                    <Pencil className="h-4 w-4" />
                    Editar
                  </Button>
                  <Button
                    variant="destructive"
                    className="flex-1 gap-2"
                    onClick={() => setDeletingServiceId(servico.id)}
                  >
                    <Trash2 className="h-4 w-4" />
                    Excluir
                  </Button>
                </CardFooter>
              </Card>
            ))}
          </div>
        )}
      </main>

      {/* Edit Sheet */}
      <Sheet open={isEditSheetOpen} onOpenChange={setIsEditSheetOpen}>
        <SheetContent className="overflow-y-auto">
          <SheetHeader>
            <SheetTitle>Editar Serviço</SheetTitle>
            <SheetDescription>
              Faça as alterações necessárias no serviço
            </SheetDescription>
          </SheetHeader>

          <div className="space-y-4 mt-6">
            <div>
              <Label htmlFor="edit-nome">Nome do Serviço</Label>
              <Input
                id="edit-nome"
                value={editForm.nome}
                onChange={(e) =>
                  setEditForm({ ...editForm, nome: e.target.value })
                }
                placeholder="Ex: Limpeza Residencial"
              />
              {errors.nome && (
                <p className="text-sm text-destructive mt-1">{errors.nome}</p>
              )}
            </div>

            <div>
              <Label htmlFor="edit-descricao">Descrição</Label>
              <Textarea
                id="edit-descricao"
                value={editForm.descricao}
                onChange={(e) =>
                  setEditForm({ ...editForm, descricao: e.target.value })
                }
                placeholder="Descreva o serviço..."
                rows={4}
              />
              {errors.descricao && (
                <p className="text-sm text-destructive mt-1">
                  {errors.descricao}
                </p>
              )}
            </div>

            <div>
              <Label htmlFor="edit-preco">Preço Base (R$)</Label>
              <Input
                id="edit-preco"
                type="number"
                step="0.01"
                value={editForm.precoBase}
                onChange={(e) =>
                  setEditForm({ ...editForm, precoBase: e.target.value })
                }
                placeholder="150.00"
              />
              {errors.precoBase && (
                <p className="text-sm text-destructive mt-1">
                  {errors.precoBase}
                </p>
              )}
            </div>

            <div>
              <Label htmlFor="edit-categoria">Categoria</Label>
              <Select
                value={editForm.categoria}
                onValueChange={(value) =>
                  setEditForm({ ...editForm, categoria: value })
                }
              >
                <SelectTrigger id="edit-categoria">
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
                <p className="text-sm text-destructive mt-1">
                  {errors.categoria}
                </p>
              )}
            </div>

            <div>
              <Label htmlFor="edit-imagem">URL da Imagem (Opcional)</Label>
              <Input
                id="edit-imagem"
                value={editForm.imagemUrl}
                onChange={(e) =>
                  setEditForm({ ...editForm, imagemUrl: e.target.value })
                }
                placeholder="https://..."
              />
            </div>

            <div className="flex gap-2 pt-4">
              <Button
                variant="outline"
                className="flex-1"
                onClick={() => setIsEditSheetOpen(false)}
                disabled={isSaving}
              >
                Cancelar
              </Button>
              <Button
                className="flex-1"
                onClick={handleSaveEdit}
                disabled={isSaving}
              >
                {isSaving ? (
                  <>
                    <Loader2 className="h-4 w-4 animate-spin mr-2" />
                    Salvando...
                  </>
                ) : (
                  "Salvar Alterações"
                )}
              </Button>
            </div>
          </div>
        </SheetContent>
      </Sheet>

      {/* Delete Confirmation Dialog */}
      <Dialog
        open={deletingServiceId !== null}
        onOpenChange={(open) => !open && setDeletingServiceId(null)}
      >
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Confirmar Exclusão</DialogTitle>
            <DialogDescription>
              Tem certeza que deseja excluir este serviço? Esta ação não pode
              ser desfeita.
            </DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => setDeletingServiceId(null)}
            >
              Cancelar
            </Button>
            <Button variant="destructive" onClick={handleDelete}>
              Excluir
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <WhatsAppButton />
    </div>
  );
};

export default Page;

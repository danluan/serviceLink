# 🛠️ Sistema de Prestadores de Serviços com LLM  

## 📖 Sobre o Projeto  
Este projeto tem como objetivo o desenvolvimento de uma plataforma digital que conecta clientes a prestadores de serviços. 
O diferencial está no uso de Modelos de Linguagem (LLM) para automatizar atendimentos, gerar orçamentos e responder às principais dúvidas dos clientes.

O projeto está sendo desenvolvido como parte da disciplina de **[Projeto Detalhado de Software]** na faculdade.  

---

## 🚀 Funcionalidades  
- Cadastro de prestadores de serviço (perfil do profissional).  
- Gestão de serviços (criação, edição, exclusão, listagem e busca).  
- Gestão de agendamentos (criação, edição, exclusão, consulta).  
- Envio automático de mensagens de confirmação de agendamento via integração de mensageria.  
- Respostas automáticas para dúvidas frequentes usando LLM.  
- Assistente de orçamento inteligente, interpretando descrições de problemas e sugerindo profissionais.  

---

## 📅 Metodologia e Sprints  
O desenvolvimento está sendo feito em **sprints** seguindo uma abordagem ágil.  

- **Sprint 1 (até 16/09/2025):** Configuração do projeto, banco de dados e CRUDs de Prestador, Serviço e Agendamento.  
- **Sprint 2 (até 30/09/2025):** Integração com API de mensageria e protótipos de atendimento automático por LLM.  
- **Sprint 3 (até 28/10/2025):** Refinamento das funcionalidades de LLM e entrega final.  

---

## 🏗️ Arquitetura  
O sistema segue uma **arquitetura em camadas**, com separação entre:  

- **Camada de Apresentação (Controller):** Endpoints REST.  
- **Camada de Negócio (Service):** Regras de negócio.  
- **Camada de Persistência (Repository):** Comunicação com o banco de dados.  
- **Camada de Modelo (Entities):** Representação das entidades.  

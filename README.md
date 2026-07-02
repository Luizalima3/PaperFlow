# Projeto PaperFlow - Sistema de Submissão e Avaliação de Artigos Científicos

> Para ver como colocar o projeto em funcionamento, acesse o arquivo: [como_rodar.md](como_rodar.md)

---

## Disciplina

**Padrões de Projeto de Software** | **Curso:** Sistemas para Internet

**Período:** 5º Período

**Professor:** Alex Sandro da Cunha Rêgo

---

## Descrição do Projeto

O **PaperFlow** é um sistema educacional para gerenciar o fluxo completo de submissão, revisão e avaliação de artigos científicos em eventos acadêmicos.

A aplicação permite que pesquisadores submetam artigos, revisores realizem avaliações de forma anônima (*blind-review*) e coordenadores gerenciem todo o ciclo de submissão e revisão.

O objetivo principal é automatizar e demonstrar de forma prática a aplicação de padrões de design (GoF, GRASP) e princípios SOLID em um cenário real de software.

---

## Objetivos

- Implementar o fluxo completo de submissão de artigos científicos
- Gerenciar eventos acadêmicos com múltiplas categorias
- Distribuir artigos automaticamente com afinidade temática
- Garantir avaliação *blind-review* com detecção de conflito de interesse
- Notificar autores e revisores por email
- Demonstrar 8 padrões GoF em funcionamento
- Aplicar todos os princípios GRASP e SOLID
- Manter arquitetura em camadas com separação de responsabilidades

---

## Funcionalidades e Padrões de Projeto

### RF01 - Inicialização de Evento

Permite criar um novo evento acadêmico contendo:

- Nome, cidade, data de início e término
- Categoria do evento (Full Paper, Short Paper, Demo)
- Prazo para submissão

> **Padrões de Projeto Adotados:**
> 
> - **Strategy:** Categorias do evento via `CategoriaEvento` e implementações concretas
> - **State:** Ciclo de vida do evento (Aberto/Fechado)

---

### RF02 - Cadastro de Usuários

O sistema permite cadastro de pesquisadores com:

- Email, senha, instituição
- Um mesmo usuário pode atuar como autor ou revisor
- Validações de campos obrigatórios

> **Padrão de Projeto Adotado:**
> 
> - **Factory/Builder:** Construção segura de objetos Usuario

---

### RF03 - Áreas Temáticas

O coordenador pode cadastrar áreas de conhecimento para:

- Classificação de artigos
- Especialidades dos revisores

Exemplos: Inteligência Artificial, Machine Learning, Engenharia de Software, Ciência de Dados, Visão Computacional

---

### RF04 - Comitê Técnico

O coordenador pode convidar pesquisadores para compor o comitê de revisão com suas áreas de especialidade.

---

### RF05 - Submissão de Artigos

Um artigo contém:

- Título, resumo, autores, coautores, áreas temáticas
- Categoria do evento e data de submissão
- Status: Submetido → Em Revisão → Aceito/Rejeitado

> **Padrões de Projeto Adotados:**
> 
> - **Builder:** Construção fluida de artigos via `Artigo.builder()`
> - **State:** Controle de ciclo de vida do artigo via `StatusArtigo`

---

### RF06 - Distribuição Automática

O sistema distribui automaticamente artigos para revisores considerando:

- **Afinidade temática:** Prioriza revisores com áreas compatíveis
- **Balanceamento:** Distribui carga igualmente entre revisores
- **Ausência de conflito:** Revisor não pode ser autor ou coautor
- **Blind-review:** Anonimato garantido

> **Padrões de Projeto Adotados:**
> 
> - **Strategy:** Interface `CategoriaEvento` com estratégias de distribuição
> - **Observer:** Notificações automáticas via `CanalNotificacoes` e `OuvinteEmail`

---

### RF07 - Avaliação de Artigos

Cada avaliação contém:

- **Contribuições:** Descrição dos pontos positivos
- **Críticas:** Descrição dos pontos negativos
- **Veredito:** Aceito, Fracamente Aceito, Fracamente Rejeitado, Rejeitado

Agregação: Média ≥ 3.0 = Aceito; < 3.0 = Rejeitado

> **Padrões de Projeto Adotados:**
> 
> - **State:** Atualiza estados internos do artigo conforme avaliações
> - **Observer:** Sistema de notificação de conclusões

---

### RF08 - Dashboard

Exibe indicadores em tempo real do evento:

- Quantidade de artigos submetidos
- Quantidade de revisores
- Artigos avaliados vs pendentes
- Revisores responsáveis e suas cargas

> **Padrão de Projeto Adotado:**
> 
> - **Singleton:** Instância centralizada do painel administrativo

---

### RF09 - Notificação dos Autores

Ao final do processo, autores recebem notificações formais contendo:

- Resultado da avaliação (Aceito/Rejeitado)
- Pareceres anônimos dos revisores
- Informações do evento

Modo de envio: teste (simulado) ou real (SMTP via Gmail)

> **Padrão de Projeto Adotado:**
> 
> - **Template Method:** Estrutura padrão de geração via `GeradorEmail` e subclasses
> - **Observer:** Disparo automático de notificações

---

### RF10 - Ações Administrativas

Permite ao coordenador executar ações especiais:

- Reabrir submissão
- Cancelar distribuição
- Reenviar notificações
- Aprovar publicação final

> **Padrão de Projeto Adotado:**
> 
> - **Command:** Interface `AcaoAdministrativa` com implementações concretas

---

## Arquitetura em Camadas

```
┌──────────────────────────────────────────────────┐
│ APRESENTAÇÃO                                     │
│ Main.java - Orquestração de cenário demo         │
└────────────────────────┬─────────────────────────┘
                         │
┌────────────────────────▼─────────────────────────┐
│ APLICAÇÃO                                        │
│ SistemaSubmissoes (Facade)                       │
│ AcaoAdministrativa (Command)                     │
└────────────────────────┬─────────────────────────┘
                         │
┌────────────────────────▼─────────────────────────┐
│ DOMÍNIO                                          │
│ Usuario, Evento, Artigo, Revisao, Parecer        │
│ StatusArtigo (State), CategoriaEvento (Strategy) │
└────────────────────────┬─────────────────────────┘
                         │
┌────────────────────────▼─────────────────────────┐
│ INFRAESTRUTURA                                   │
│ ServicoEmail (Strategy)                          │
│ CanalNotificacoes (Observer)                     │
│ GeradorEmail (Template Method)                   │
└──────────────────────────────────────────────────┘
```

---

## Padrões de Projeto Implementados

| Padrão | Classe Principal | Objetivo |
|--------|-----------------|----------|
| **Builder** | `Artigo.builder()` | Construção segura de artigos |
| **State** | `StatusArtigo` | Ciclo de vida de artigos |
| **Strategy** | `CategoriaEvento` | Variação de categorias e distribuição |
| **Observer** | `CanalNotificacoes` | Notificações automáticas |
| **Template Method** | `GeradorEmail` | Estrutura padrão de emails |
| **Command** | `AcaoAdministrativa` | Ações administrativas |
| **Facade** | `SistemaSubmissoes` | Orquestração central |
| **Singleton** | `Dashboard` | Instância única global |

---

## Princípios SOLID e GRASP

### SOLID
- ✅ **S**ingle Responsibility: Cada classe tem uma responsabilidade
- ✅ **O**pen/Closed: Extensível via Strategy e Template Method
- ✅ **L**iskov Substitution: Implementações intercambiáveis
- ✅ **I**nterface Segregation: Interfaces específicas
- ✅ **D**ependency Inversion: Inversão via interfaces

### GRASP
- ✅ Information Expert
- ✅ Creator
- ✅ Controller
- ✅ Low Coupling
- ✅ High Cohesion
- ✅ Polymorphism
- ✅ Pure Fabrication
- ✅ Indirection

---

## Tecnologia

- **Linguagem:** Java 21 (JavaSE-21 LTS)
- **Build Tool:** Maven 3.8+
- **Email:** Jakarta Mail 2.0.1 (SMTP/Gmail)
- **Documentação:** Mermaid (15 diagramas UML)

---

## Execução Rápida

```bash
# Compilar
mvn clean compile

# Executar simulação de teste
mvn exec:java -Dexec.mainClass=paperflow.Main

# Limpar e compilar (Maven Wrapper no Windows)
mvnw.cmd clean compile
```

**Nota:** Para instruções detalhadas de instalação e configuração, consulte [como_rodar.md](como_rodar.md)

---

## Diagramas UML

Diversos diagramas UML foram criados para documentar a arquitetura:

- Diagrama de Classes Completo
- Diagramas de Estado (Artigo, Evento)
- Diagramas de Padrões (Strategy, State, Observer, Command, etc)
- Diagramas de Arquitetura
- Fluxogramas de Processos

Todos os diagramas estão disponíveis em PDF na pasta [diagramas/](https://github.com/Luizalima3/PaperFlow/tree/main/diagramas/)

---

## Cenário Demonstrado

A simulação em `Main.java` executa um cenário completo em 10 etapas:

1. **Cadastro de Usuários:** Criação de autores, revisores, coordenador
2. **Registro de Áreas:** IA, Machine Learning, Engenharia de Software, etc
3. **Inicialização de Evento:** SBSI 2026 com prazo de submissão
4. **Comitê Técnico:** Registro de 3 revisores com especialidades
5. **Submissão de Artigos:** 2 artigos com múltiplos autores e coautores
6. **Distribuição Automática:** Alocação com afinidade e balanceamento
7. **Aceitar Revisões:** Revisores aceitam suas atribuições
8. **Conclusão de Revisões:** Parecer com contribuições, críticas e veredito
9. **Dashboard e Resultados:** Exibição de estatísticas e publicação final
10. **Notificações:** Envio de emails aos autores

---

## Status do Projeto

✅ Todas as funcionalidades implementadas e testadas
✅ 8 padrões GoF em funcionamento
✅ Todos os princípios SOLID e GRASP aplicados
✅ Arquitetura em camadas estabelecida
✅ Documentação completa (README, Diagramas UML)
✅ Simulação executável e validada
✅ Email real (SMTP) ou simulado conforme configuração

---

*Projeto educacional desenvolvido com foco em arquitetura limpa e aplicação prática de padrões de design.*

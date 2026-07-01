# PaperFlow - Sistema de Submissão e Avaliação de Artigos Científicos

Sistema educacional desenvolvido em Java para gerenciar completo fluxo de submissão, revisão e avaliação de artigos científicos em eventos acadêmicos. Implementa múltiplos padrões de design (GoF e GRASP) e princípios SOLID com arquitetura limpa.

## 📋 Informações do Projeto

- **Disciplina**: Padrões de Projeto de Software
- **Período**: 5º período - Sistemas para Internet
- **Professor**: Alex Sandro da Cunha Rêgo
- **Linguagem**: Java 21
- **Build Tool**: Maven
- **Status**: Production-ready

---

## 🎯 Funcionalidades Principais

### RF01 - Inicializar Evento
- Criar novo evento com nome, cidade, período
- Definir prazo de submissão
- Selecionar categoria (Full Paper, Short Paper, Demo)
- Reset automático de dados anteriores

### RF02 - Cadastro de Usuários
- Email, senha, instituição
- Usuários podem ser autores ou revisores
- Validações de email e campos obrigatórios

### RF03 - Áreas Temáticas
- Cadastro de especialidades (IA, Machine Learning, etc)
- Reutilização para artigos e revisores

### RF04 - Comitê Técnico
- Registro de revisores com áreas de expertise
- Afinidade matching automático

### RF05 - Submissão de Artigos
- Validação de prazo
- Coautores (pré-cadastrados)
- Múltiplas áreas temáticas
- Status: Submetido → Em Revisão → Aceito/Rejeitado

### RF06 - Distribuição Automática
- **Afinidade**: Prioriza revisores com áreas compatíveis
- **Balanceamento**: Distribui carga igualmente
- **Conflito de Interesse**: Impede que autor/coautor revise próprio artigo

### RF07 - Processo de Revisão
- Aceitar/rejeitar revisão
- Parecer com: contribuição, crítica, veredito (Aceito/Fracamente Aceito/Fracamente Rejeitado/Rejeitado)
- Agregação: Média ≥ 3.0 = Aceito; < 3.0 = Rejeitado

### RF08 - Dashboard
- Total de artigos submetidos
- Total de revisores
- Artigos avaliados e pendentes
- Lista de pendências detalhada

### RF09 - Notificações por Email
- **Convite a Revisores**: Artigos atribuídos, prazo de revisão
- **Resultado a Autores**: Aceito/Rejeitado + pareceres anônimos
- Modo teste (simulado) ou real (SMTP)

### RF10 - Ações Administrativas (Pattern: Command)
- Reabrir submissão
- Cancelar distribuição
- Reenviar notificações
- Aprovar publicação final

---

## 🏗️ Arquitetura

### Camadas

```
┌─────────────────────────────────────────────┐
│ APRESENTAÇÃO (Main.java)                    │
│ - Orquestração de cenário demo              │
│ - Modo teste/real para email                │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│ APLICAÇÃO (aplicacao/)                      │
│ - SistemaSubmissoes (Facade/Orchestrator)   │
│ - AcaoAdministrativa (Command pattern)      │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│ DOMÍNIO (dominio/)                          │
│ - Entidades: Usuario, Evento, Artigo        │
│ - ValueObjects: Parecer, DashboardResumo    │
│ - Estado: StatusArtigo (State pattern)      │
│ - Estratégia: CategoriaEvento               │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│ INFRAESTRUTURA (infra/)                     │
│ - ServicoEmail (Strategy: SMTP/Memória)     │
│ - CanalNotificacoes (Observer)              │
│ - GeradorEmail (Template Method)            │
└─────────────────────────────────────────────┘
```

### Estrutura de Pacotes

```
src/paperflow/
├── Main.java                                 # Entry point
├── aplicacao/
│   ├── SistemaSubmissoes.java               # Orquestrador central (Facade)
│   └── acoes/
│       ├── AcaoAdministrativa.java          # Interface Command
│       ├── AcaoReabrirSubmissao.java        # Comando concreto
│       ├── AcaoCancelarDistribuicao.java    # Comando concreto
│       ├── AcaoReenviarNotificacoes.java    # Comando concreto
│       └── AcaoAprovarPublicacaoFinal.java  # Comando concreto
├── dominio/
│   ├── Usuario.java                         # Domínio: usuário
│   ├── Evento.java                          # Domínio: evento
│   ├── Artigo.java                          # Domínio com Builder
│   ├── Revisao.java                         # Domínio: revisão
│   ├── Parecer.java                         # Value Object
│   ├── DashboardResumo.java                 # DTO/Value Object
│   ├── Veredito.java                        # Enum: escores
│   ├── StatusArtigo.java                    # State: interface
│   ├── StatusSubmetido.java                 # State concreto
│   ├── StatusEmRevisao.java                 # State concreto
│   ├── StatusAceito.java                    # State concreto
│   ├── StatusRejeitado.java                 # State concreto
│   └── categoria/
│       ├── CategoriaEvento.java             # Strategy: interface
│       ├── CategoriaFullPaper.java          # Strategy concreto
│       ├── CategoriaShortPaper.java         # Strategy concreto
│       └── CategoriaDemo.java               # Strategy concreto
└── infra/notificacao/
    ├── ServicoEmail.java                    # Abstração (interface)
    ├── ServicoEmailSmtp.java                # Implementação SMTP real
    ├── ServicoEmailMemoria.java             # Implementação para testes
    ├── Email.java                           # DTO com Builder
    ├── MensagemNotificacao.java             # DTO
    ├── TipoNotificacao.java                 # Enum
    ├── CanalNotificacao.java                # Observer: Subject interface
    ├── CanalNotificacoes.java               # Observer: Subject concreto
    ├── OuvinteNotificacao.java              # Observer: Listener interface
    ├── OuvinteEmail.java                    # Observer: Listener concreto
    ├── GeradorEmail.java                    # Template Method: abstrato
    ├── GeradorConviteRevisao.java           # Template Method: concreto
    ├── GeradorResultadoAutor.java           # Template Method: concreto
    ├── CorpoEmailBase.java                  # Template Method: abstrato
    ├── CorpoConviteRevisao.java             # Template Method: concreto
    └── CorpoResultadoAutor.java             # Template Method: concreto
```

---

## 🎨 Padrões de Design Implementados

### GoF (Gang of Four) - Padrões de Criação

| Pattern | Classe(s) | Propósito |
|---------|-----------|----------|
| **Builder** | `Artigo.Builder`, `Email.Builder` | Construção segura de objetos complexos com fluent API |
| **Factory Method** | Implícito em Builders | Criação de objetos com validação |

### GoF - Padrões Estruturais

| Pattern | Classe(s) | Propósito |
|---------|-----------|----------|
| **Facade** | `SistemaSubmissoes` | Interface unificada para subsistema complexo |
| **Decorator** | Observer pattern | Comportamento adicional sem modificação |

### GoF - Padrões Comportamentais

| Pattern | Classe(s) | Propósito |
|---------|-----------|----------|
| **State** | `StatusArtigo` + 4 implementações | Transições de estado do artigo (Submetido → EmRevisao → Aceito/Rejeitado) |
| **Strategy** | `CategoriaEvento` + 3 implementações | Flexibilidade para diferentes tipos de eventos |
| **Command** | `AcaoAdministrativa` + 4 implementações | Encapsular operações administrativas como objetos |
| **Observer** | `CanalNotificacoes`, `OuvinteEmail` | Notificações desacopladas para interessados |
| **Template Method** | `GeradorEmail`, `CorpoEmailBase` | Estrutura comum para geração de emails |

### GRASP (General Responsibility Assignment Software Patterns)

| Princípio | Aplicação |
|-----------|-----------|
| **Information Expert** | `Artigo` conhece seu próprio status; `Usuario` gerencia dados do usuário |
| **Creator** | `SistemaSubmissoes` cria `Usuario`, `Artigo`, `Revisao` |
| **Low Coupling** | Interfaces para `ServicoEmail`, `StatusArtigo`, `CategoriaEvento` |
| **High Cohesion** | Classes com responsabilidade única e bem definida |
| **Polymorphism** | Interface `StatusArtigo` com 4 implementações; `ServicoEmail` com 2 |
| **Pure Fabrication** | `SistemaSubmissoes` como orquestrador; `CanalNotificacoes` para infra |
| **Indirection** | Abstração de email previne acoplamento direto com SMTP |
| **Protected Variations** | Modo teste/real; novas categorias sem quebra de código |

### SOLID Principles

| Princípio | Status | Evidência |
|-----------|--------|----------|
| **S**ingle Responsibility | ✅ Excelente | Cada classe tem uma razão para mudar |
| **O**pen/Closed | ✅ Forte | Aberto para extensão (novas categorias) sem modificação |
| **L**iskov Substitution | ✅ Excelente | Todas implementações são perfeitamente substituíveis |
| **I**nterface Segregation | ✅ Bom | Interfaces focadas; minor: `AcaoAdministrativa` |
| **D**ependency Inversion | ✅ Forte | Injeção de dependência; código depende de abstrações |

---

## � Diagramas UML

Para visualizar os diagramas detalhados do projeto, consulte [UML_DIAGRAMS.md](UML_DIAGRAMS.md).

### Diagramas Inclusos:

1. **📊 Diagrama Geral de Classes** - Visão completa de todas as classes e relacionamentos
2. **🎨 Padrão Strategy (CategoriaEvento)** - Seleção de tipo de evento
3. **🔄 Padrão State (StatusArtigo)** - Ciclo de vida do artigo
4. **👁️ Padrão Observer (CanalNotificacoes)** - Notificações desacopladas
5. **⚙️ Padrão Command (AcaoAdministrativa)** - Operações administrativas
6. **🏗️ Padrão Template Method (GeradorEmail)** - Geração de emails
7. **🏭 Padrão Builder (Artigo)** - Construção de objetos
8. **🛑 Padrão Facade (SistemaSubmissoes)** - Interface unificada
9. **🔌 Padrão Strategy (ServicoEmail)** - Abstração de email
10. **📦 Arquitetura em Camadas** - Organização do código
11. **🔄 Fluxo de Distribuição** - Afinidade e balanceamento
12. **📊 Fluxo de Estados de Revisão** - Ciclo de revisão
13. **🎯 Matriz RACI** - Responsabilidades
14. **📈 Sequência de Fluxo Principal** - Chamadas de método
15. **📋 Resumo de Padrões** - Índice de técnicas

**Abra [UML_DIAGRAMS.md](UML_DIAGRAMS.md) para explorar os diagramas!**

---
## 🖼️ Diagramas em PNG/SVG

Além dos diagramas interativos Mermaid, todos os diagramas também estão disponíveis em formato PNG e SVG:

- **Pasta com Imagens**: [./diagrams/](diagrams/) (26 arquivos: 13 PNG + 13 SVG)
- **Índice Completo com Descrições**: [DIAGRAMS_INDEX.md](DIAGRAMS_INDEX.md)
- **Como Gerar Novamente**: Execute `python convert_diagrams.py`

**⭐ Navegue para [DIAGRAMS_INDEX.md](DIAGRAMS_INDEX.md) para visualizar e fazer download de cada diagrama!**

---
## �🚀 Executar o Projeto

### Pré-requisitos

- Java 21 (ou superior)
- Maven 3.8+ ou compilar manualmente

### Opção 1: Maven (Recomendado)

```bash
# Compilar
mvn clean compile

# Executar simulação (modo teste - padrão)
mvn exec:java -Dexec.mainClass=paperflow.Main
```

### Opção 2: Compilação Manual (Windows)

```batch
# Listar todos os arquivos .java
dir /s /b src\paperflow\*.java > sources.txt

# Compilar
rmdir /s /q out 2>nul
mkdir out
javac -d out @sources.txt

# Executar
java -cp out paperflow.Main
```

### Opção 3: Compilação Manual (Linux/Mac)

```bash
# Compilar
find src -name "*.java" | xargs javac -d out

# Executar
java -cp out paperflow.Main
```

---

## 🧪 Cenário Demonstrado

O `Main.java` executa simulação completa com:

1. ✅ Cadastro de 7 usuários (autores, revisores, coordenador)
2. ✅ Cadastro de 4 áreas temáticas (IA, Software, ML, Visão Computacional)
3. ✅ Criação do evento SBSI 2026 (Full Paper)
4. ✅ Registro de 3 revisores com especialidades
5. ✅ Submissão de 2 artigos
6. ✅ Distribuição automática com afinidade matching
7. ✅ Aceite de revisões e conclusão com pareceres
8. ✅ Geração de dashboard
9. ✅ Fechamento do ciclo com resultado final
10. ✅ Envio de notificações (teste ou real)

**Saída esperada**: ~160 linhas de log com detalhes de cada etapa

---

## 📊 Exemplos de Saída

### Modo Teste (Padrão)

```
=== SIMULACAO COMPLETA: SUBMISSAO, REVISAO E RESULTADO ===

[MODO TESTE] Emails serao apenas simulados (nao enviados).

Para enviar emails REAIS, configure as variaveis de ambiente e descomente a linha em Main.java.

[OK] Usuarios de teste cadastrados.
[OK] Areas tematicas do evento configuradas.
[OK] Evento 'Simpósio Brasileiro de Sistemas de Informação (SBSI) - 2026' iniciado.
     Categoria: Full Paper
[OK] Comite tecnico registrado com 3 revisores.
[OK] Artigos submetidos: 2
[SIMULADO] Email para: revisor.ana@ifpb.edu.br
[SIMULADO] Email para: revisor.clara@ifpb.edu.br
...
```

### Modo Real (com env vars configuradas)

```
[EMAIL ENVIADO] Para: revisor.ana@ifpb.edu.br
[EMAIL ENVIADO] Para: revisor.clara@ifpb.edu.br
[EMAIL ENVIADO] Para: revisor.bruno@ifpb.edu.br
...
```

---

## 🔒 Segurança

- ✅ **Teste por padrão**: Nenhuma credencial necessária para executar
- ✅ **Sem hardcoding**: Código não contém credenciais
- ⚠️ **Aviso**: Se implementar email real, nunca comite credenciais no Git

---

## 📝 Requisitos Não-Funcionais Atendidos

- ✅ **Aderência SOLID**: Princípios bem aplicados
- ✅ **Encapsulamento**: Construtores privados, coleções imutáveis
- ✅ **Tratamento de exceções**: Validações compreensivas
- ✅ **Reuso**: Componentes independentes e reutilizáveis
- ✅ **Baixo acoplamento**: Interfaces para abstrair implementações
- ✅ **Sem println direto**: Método `enviar()` sem println em classes de domínio
- ✅ **Fácil execução**: Um comando Maven

---

## 🧪 Extensibilidade

O projeto é facilmente extensível:

### Adicionar Nova Categoria de Evento
```java
// 1. Criar nova classe herdando CategoriaEvento
public class CategoriaWorkshop extends CategoriaEvento { ... }

// 2. Usar em Main.java
CategoriaEvento categoria = new CategoriaWorkshop();
sistema.iniciarNovoEvento(..., categoria);
```

### Adicionar Novo Estado de Artigo
```java
// 1. Criar novo Status
public class StatusArquivado implements StatusArtigo { ... }

// 2. Usar em SistemaSubmissoes
artigo.mudarStatus(new StatusArquivado());
```

### Trocar Implementação de Email
```java
// Trocar de SMTP para outro serviço
ServicoEmail emailService = new ServicoEmailSendGrid(...);
SistemaSubmissoes sistema = new SistemaSubmissoes(emailService, ...);
```

---

## 📚 Referências

- [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Maven Documentation](https://maven.apache.org/)
- [Jakarta Mail API](https://jakarta.ee/specifications/mail/)
- [Design Patterns: Elements of Reusable Object-Oriented Software](https://en.wikipedia.org/wiki/Design_Patterns)
- [GRASP (Object Design Roles)](https://en.wikipedia.org/wiki/GRASP_(object-oriented_design))
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)

---

## 📄 Licença

Projeto educacional desenvolvido para disciplina de Padrões de Projeto de Software.

---

**Última atualização**: 2026-07-01  
**Versão**: 1.0 (Production-Ready)

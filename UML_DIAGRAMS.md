# Diagramas UML - PaperFlow

Documentação detalhada dos diagramas UML do sistema PaperFlow, incluindo arquitetura geral e padrões de design específicos.

---

## 📐 Diagrama Geral de Classes (Principal)

```mermaid
classDiagram
    %% ===== APLICAÇÃO =====
    class SistemaSubmissoes {
        -eventos: Map
        -usuarios: Map
        -artigos: Map
        -revisores: Set
        -cargaPorRevisor: Map
        -canalNotificacoes: CanalNotificacao
        -servicoEmail: ServicoEmail
        -emailsEnviados: List
        +iniciarNovoEvento(...)
        +cadastrarUsuario(...)
        +cadastrarAreaTematica(...)
        +registrarRevisorNoComite(...)
        +submeterArtigo(...)
        +distribuirArtigosAutomaticamente()
        +aceitarRevisao(...)
        +concluirRevisao(...)
        +gerarDashboard()
        +fecharCicloRevisaoEPublicarResultado()
        +executarAcao(AcaoAdministrativa)
        +enviarEmail(...)
    }

    class AcaoAdministrativa {
        <<interface>>
        +executar()
        +descricao()
    }

    class AcaoReabrirSubmissao {
        +executar()
        +descricao()
    }

    class AcaoCancelarDistribuicao {
        +executar()
        +descricao()
    }

    class AcaoReenviarNotificacoes {
        -artigoId: String
        +executar()
        +descricao()
    }

    class AcaoAprovarPublicacaoFinal {
        -artigoId: String
        +executar()
        +descricao()
    }

    %% ===== DOMÍNIO =====
    class Usuario {
        -email: String
        -senha: String
        -instituicao: String
        -especialidades: Set
        +getEmail()
        +getSenha()
        +getInstituicao()
        +getEspecialidades()
    }

    class Evento {
        -nome: String
        -cidade: String
        -periodo: String
        -prazoSubmissao: LocalDateTime
        -areasTematicas: Set
        -aberto: boolean
        -categoria: CategoriaEvento
        +estaAberto(): boolean
        +getNome()
        +getCategoria()
    }

    class Artigo {
        -id: String
        -titulo: String
        -resumo: String
        -autor: Usuario
        -coautores: List
        -areasTematicas: Set
        -status: StatusArtigo
        -revisoes: List
        +getId()
        +getTitulo()
        +getAutor()
        +mudarStatus(StatusArtigo)
        +addRevisao(Revisao)
    }

    class Builder {
        -id: String
        -titulo: String
        +build(): Artigo
    }

    class Revisao {
        -artigo: Artigo
        -revisor: Usuario
        -estado: EstadoRevisao
        -parecer: Parecer
        +aceitarRevisao()
        +concluirRevisao(Parecer)
        +getEstado()
        +getParecer()
    }

    class Parecer {
        -contribuicao: String
        -critica: String
        -veredito: Veredito
        +getVeredito()
        +getVeredito().getScore(): int
    }

    class Veredito {
        <<enumeration>>
        ACEITO(4)
        FRACAMENTE_ACEITO(3)
        FRACAMENTE_REJEITADO(2)
        REJEITADO(1)
        -score: int
        +getScore(): int
    }

    class StatusArtigo {
        <<interface>>
        +getNome(): String
    }

    class StatusSubmetido {
        +getNome(): String
    }

    class StatusEmRevisao {
        +getNome(): String
    }

    class StatusAceito {
        +getNome(): String
    }

    class StatusRejeitado {
        +getNome(): String
    }

    class DashboardResumo {
        -totalArtigos: int
        -totalRevisores: int
        -totalArtigosAvaliados: int
        -totalPendentes: int
        -pendencias: List
        +getTotalArtigos()
        +getTotalRevisores()
        +getPendencias()
    }

    class EstadoRevisao {
        <<enumeration>>
        PENDENTE
        ACEITA
        CONCLUIDA
    }

    %% ===== CATEGORIA (STRATEGY) =====
    class CategoriaEvento {
        <<interface>>
        +getNomeCategoria(): String
    }

    class CategoriaFullPaper {
        +getNomeCategoria(): String
    }

    class CategoriaShortPaper {
        +getNomeCategoria(): String
    }

    class CategoriaDemo {
        +getNomeCategoria(): String
    }

    %% ===== NOTIFICAÇÕES (INFRASTRUCTURE) =====
    class ServicoEmail {
        <<interface>>
        +enviar(String, String, String)
    }

    class ServicoEmailSmtp {
        -host: String
        -porta: int
        -usuario: String
        -senha: String
        +enviar(String, String, String)
        +deMeioAmbiente(): ServicoEmailSmtp
    }

    class ServicoEmailMemoria {
        -emails: List
        +enviar(String, String, String)
    }

    class Email {
        -destinatario: String
        -assunto: String
        -corpo: String
        +getDestinatario()
        +getAssunto()
        +getCorpo()
    }

    class EmailBuilder {
        +paraDestinatario(String)
        +comAssunto(String)
        +comCorpo(String)
        +build(): Email
    }

    class CanalNotificacao {
        <<interface>>
        +notificar(MensagemNotificacao)
        +adicionarOuvinte(OuvinteNotificacao)
        +removerOuvinte(OuvinteNotificacao)
    }

    class CanalNotificacoes {
        -ouvintes: List
        +notificar(MensagemNotificacao)
        +adicionarOuvinte(OuvinteNotificacao)
        +removerOuvinte(OuvinteNotificacao)
    }

    class OuvinteNotificacao {
        <<interface>>
        +aoReceberNotificacao(MensagemNotificacao)
    }

    class OuvinteEmail {
        -servicoEmail: ServicoEmail
        +aoReceberNotificacao(MensagemNotificacao)
    }

    class MensagemNotificacao {
        -tipo: TipoNotificacao
        -destinatario: String
        -assunto: String
        -corpo: String
    }

    class TipoNotificacao {
        <<enumeration>>
        CONVITE_REVISAO
        RESULTADO_AUTOR
    }

    class GeradorEmail {
        <<abstract>>
        #servicoEmail: ServicoEmail
        +gerar(MensagemNotificacao)
        #montarCorpoEmail(): CorpoEmail
    }

    class GeradorConviteRevisao {
        +gerar(MensagemNotificacao)
        #montarCorpoEmail(): CorpoEmail
    }

    class GeradorResultadoAutor {
        +gerar(MensagemNotificacao)
        #montarCorpoEmail(): CorpoEmail
    }

    class CorpoEmailBase {
        <<abstract>>
        +gerarCorpo(): String
        #gerarCabecalho(): String
        #gerarRodape(): String
        #gerarConteudo(): String
    }

    class CorpoConviteRevisao {
        +gerarCorpo(): String
        #gerarConteudo(): String
    }

    class CorpoResultadoAutor {
        +gerarCorpo(): String
        #gerarConteudo(): String
    }

    %% ===== RELACIONAMENTOS =====
    
    %% SistemaSubmissoes relationships
    SistemaSubmissoes --> Evento : "utiliza"
    SistemaSubmissoes --> Usuario : "gerencia"
    SistemaSubmissoes --> Artigo : "gerencia"
    SistemaSubmissoes --> Revisao : "cria"
    SistemaSubmissoes --> CanalNotificacao : "usa"
    SistemaSubmissoes --> ServicoEmail : "injeta"
    SistemaSubmissoes --> AcaoAdministrativa : "executa"
    SistemaSubmissoes --> Email : "armazena"
    
    %% AcaoAdministrativa implementations
    AcaoAdministrativa <|.. AcaoReabrirSubmissao
    AcaoAdministrativa <|.. AcaoCancelarDistribuicao
    AcaoAdministrativa <|.. AcaoReenviarNotificacoes
    AcaoAdministrativa <|.. AcaoAprovarPublicacaoFinal

    %% Artigo relationships
    Artigo --> Usuario : "tem autor"
    Artigo --> StatusArtigo : "tem"
    Artigo --> Revisao : "recebe"
    Artigo --> "Artigo.Builder" : "criado por"
    
    %% Revisao relationships
    Revisao --> Usuario : "tem revisor"
    Revisao --> Parecer : "tem"
    Revisao --> EstadoRevisao : "tem estado"
    
    %% Parecer relationships
    Parecer --> Veredito : "tem"

    %% Evento relationships
    Evento --> CategoriaEvento : "usa"

    %% Strategy pattern
    CategoriaEvento <|.. CategoriaFullPaper
    CategoriaEvento <|.. CategoriaShortPaper
    CategoriaEvento <|.. CategoriaDemo

    %% State pattern
    StatusArtigo <|.. StatusSubmetido
    StatusArtigo <|.. StatusEmRevisao
    StatusArtigo <|.. StatusAceito
    StatusArtigo <|.. StatusRejeitado

    %% Email
    Email --> "Email.Builder" : "criado por"

    %% Observer pattern
    CanalNotificacao <|.. CanalNotificacoes
    OuvinteNotificacao <|.. OuvinteEmail
    CanalNotificacoes --> OuvinteNotificacao : "notifica"
    OuvinteEmail --> ServicoEmail : "usa"
    
    %% Template Method pattern
    GeradorEmail <|-- GeradorConviteRevisao
    GeradorEmail <|-- GeradorResultadoAutor
    GeradorEmail --> CorpoEmailBase : "cria"
    CorpoEmailBase <|-- CorpoConviteRevisao
    CorpoEmailBase <|-- CorpoResultadoAutor

    %% Service implementations
    ServicoEmail <|.. ServicoEmailSmtp
    ServicoEmail <|.. ServicoEmailMemoria
```

---

## 🎨 Padrão: STRATEGY - CategoriaEvento

Permite selecionar o tipo de evento em tempo de execução sem modificar o código cliente.

```mermaid
classDiagram
    class SistemaSubmissoes {
        -eventoAtual: Evento
        +iniciarNovoEvento(..., categoria: CategoriaEvento)
    }

    class Evento {
        -categoria: CategoriaEvento
        -nome: String
        +getCategoria(): CategoriaEvento
    }

    class CategoriaEvento {
        <<interface>>
        +getNomeCategoria(): String
    }

    class CategoriaFullPaper {
        +getNomeCategoria(): String
    }

    class CategoriaShortPaper {
        +getNomeCategoria(): String
    }

    class CategoriaDemo {
        +getNomeCategoria(): String
    }

    SistemaSubmissoes --> Evento
    Evento --> CategoriaEvento : "composição"
    CategoriaEvento <|.. CategoriaFullPaper
    CategoriaEvento <|.. CategoriaShortPaper
    CategoriaEvento <|.. CategoriaDemo

    note "Padrão Strategy\nDefinir família de algoritmos\nencapsulados e intercambiáveis"
```

---

## 🔄 Padrão: STATE - StatusArtigo

Define transições de estado do artigo durante seu ciclo de vida.

```mermaid
stateDiagram-v2
    [*] --> Submetido: submeterArtigo()
    
    Submetido --> EmRevisao: distribuirArtigos()
    
    EmRevisao --> Aceito: fecharCiclo()\n(média >= 3.0)
    EmRevisao --> Rejeitado: fecharCiclo()\n(média < 3.0)
    
    Aceito --> [*]
    Rejeitado --> [*]
    
    note right of Submetido
        Artigo cadastrado no sistema
        Aguardando distribuição
    end note
    
    note right of EmRevisao
        Distribuído aos revisores
        Aguardando conclusão de revisões
    end note
    
    note right of Aceito
        Aprovado para publicação
        Média de vereditos >= 3.0
    end note
    
    note right of Rejeitado
        Não aprovado
        Média de vereditos < 3.0
    end note
```

**Implementação em Código:**
```mermaid
classDiagram
    class Artigo {
        -status: StatusArtigo
        +mudarStatus(StatusArtigo)
        +getNomeStatus(): String
    }

    class StatusArtigo {
        <<interface>>
        +getNome(): String
    }

    class StatusSubmetido {
        +getNome(): String
    }

    class StatusEmRevisao {
        +getNome(): String
    }

    class StatusAceito {
        +getNome(): String
    }

    class StatusRejeitado {
        +getNome(): String
    }

    Artigo --> StatusArtigo
    StatusArtigo <|.. StatusSubmetido
    StatusArtigo <|.. StatusEmRevisao
    StatusArtigo <|.. StatusAceito
    StatusArtigo <|.. StatusRejeitado

    note "Padrão State\nEncapsular transições de estado\nPermitir mudança em runtime"
```

---

## 👁️ Padrão: OBSERVER - CanalNotificacoes

Implementa notificação desacoplada: quando um evento ocorre, todos os interessados são notificados.

```mermaid
classDiagram
    class CanalNotificacao {
        <<interface>>
        +notificar(MensagemNotificacao)
        +adicionarOuvinte(OuvinteNotificacao)
        +removerOuvinte(OuvinteNotificacao)
    }

    class CanalNotificacoes {
        -ouvintes: List~OuvinteNotificacao~
        +notificar(MensagemNotificacao)
        +adicionarOuvinte(OuvinteNotificacao)
        +removerOuvinte(OuvinteNotificacao)
    }

    class OuvinteNotificacao {
        <<interface>>
        +aoReceberNotificacao(MensagemNotificacao)
    }

    class OuvinteEmail {
        -servicoEmail: ServicoEmail
        +aoReceberNotificacao(MensagemNotificacao)
    }

    class MensagemNotificacao {
        -tipo: TipoNotificacao
        -destinatario: String
        -assunto: String
        -corpo: String
    }

    class SistemaSubmissoes {
        -canalNotificacoes: CanalNotificacao
        +distribuirArtigos()
        +fecharCiclo()
    }

    CanalNotificacao <|.. CanalNotificacoes
    OuvinteNotificacao <|.. OuvinteEmail
    CanalNotificacoes --> OuvinteNotificacao : "notifica"
    OuvinteNotificacao --> MensagemNotificacao : "recebe"
    SistemaSubmissoes --> CanalNotificacao : "usa"

    note "Padrão Observer\nEstabelecer comunicação 1:N\nEntre Subject (Subject) e Observers (Ouvintes)"
```

---

## ⚙️ Padrão: COMMAND - AcaoAdministrativa

Encapsula operações administrativas como objetos, permitindo fila, log e desfazer.

```mermaid
classDiagram
    class AcaoAdministrativa {
        <<interface>>
        +executar()
        +descricao(): String
    }

    class AcaoReabrirSubmissao {
        +executar()
        +descricao(): String
    }

    class AcaoCancelarDistribuicao {
        +executar()
        +descricao(): String
    }

    class AcaoReenviarNotificacoes {
        -artigoId: String
        +executar()
        +descricao(): String
    }

    class AcaoAprovarPublicacaoFinal {
        -artigoId: String
        +executar()
        +descricao(): String
    }

    class SistemaSubmissoes {
        +executarAcao(AcaoAdministrativa)
    }

    AcaoAdministrativa <|.. AcaoReabrirSubmissao
    AcaoAdministrativa <|.. AcaoCancelarDistribuicao
    AcaoAdministrativa <|.. AcaoReenviarNotificacoes
    AcaoAdministrativa <|.. AcaoAprovarPublicacaoFinal
    SistemaSubmissoes --> AcaoAdministrativa : "executa"

    note "Padrão Command\nEncapsular request como objeto\nPermitir parametrização e fila"
```

---

## 🏗️ Padrão: TEMPLATE METHOD - GeradorEmail

Define estrutura comum para geração de emails, deixando detalhes para subclasses.

```mermaid
classDiagram
    class GeradorEmail {
        <<abstract>>
        #servicoEmail: ServicoEmail
        +gerar(MensagemNotificacao)*
        #montarCorpoEmail()*: CorpoEmail
        #enviarEmail(Email)
    }

    class GeradorConviteRevisao {
        +gerar(MensagemNotificacao)
        #montarCorpoEmail(): CorpoEmail
    }

    class GeradorResultadoAutor {
        +gerar(MensagemNotificacao)
        #montarCorpoEmail(): CorpoEmail
    }

    class CorpoEmailBase {
        <<abstract>>
        +gerarCorpo(): String*
        #gerarCabecalho(): String
        #gerarRodape(): String
        #gerarConteudo(): String*
    }

    class CorpoConviteRevisao {
        +gerarCorpo(): String
        #gerarConteudo(): String
    }

    class CorpoResultadoAutor {
        +gerarCorpo(): String
        #gerarConteudo(): String
    }

    GeradorEmail <|-- GeradorConviteRevisao
    GeradorEmail <|-- GeradorResultadoAutor
    GeradorEmail --> CorpoEmailBase : "cria"
    CorpoEmailBase <|-- CorpoConviteRevisao
    CorpoEmailBase <|-- CorpoResultadoAutor

    note "Padrão Template Method\nDefinir estrutura em classe base\nPermitir que subclasses\nimplementem passos específicos"
```

---

## 🏭 Padrão: BUILDER - Artigo

Constrói objetos complexos passo a passo, garantindo validação.

```mermaid
classDiagram
    class Artigo {
        -id: String
        -titulo: String
        -resumo: String
        -autor: Usuario
        -coautores: List
        -areasTematicas: Set
        -status: StatusArtigo
        -reviewersReviewsMap: Map
        -Artigo(Builder)
        +getId()
        +getTitulo()
    }

    class Builder {
        -id: String
        -titulo: String
        -resumo: String
        -autor: Usuario
        -coautores: List
        -areasTematicas: Set
        +comId(String): Builder
        +comTitulo(String): Builder
        +comResumo(String): Builder
        +comAutor(Usuario): Builder
        +adicionarCoautor(String): Builder
        +adicionarAreaTematica(String): Builder
        +build(): Artigo
    }

    Artigo --> Builder : "criado por"

    note "Padrão Builder\nSeparar construção de representação\nValidar estado antes de criar"
```

---

## 🛑 Padrão: FACADE - SistemaSubmissoes

Oferece interface unificada para subsistema complexo.

```mermaid
classDiagram
    class SistemaSubmissoes {
        <<facade>>
        -eventos: Map
        -usuarios: Map
        -artigos: Map
        -revisores: Set
        -canalNotificacoes: CanalNotificacao
        -servicoEmail: ServicoEmail
        +iniciarNovoEvento(...)
        +cadastrarUsuario(...)
        +submeterArtigo(...)
        +distribuirArtigosAutomaticamente()
        +concluirRevisao(...)
        +gerarDashboard()
    }

    class Usuario
    class Evento
    class Artigo
    class Revisao
    class Parecer
    class CanalNotificacao
    class ServicoEmail

    SistemaSubmissoes --> Usuario : "gerencia"
    SistemaSubmissoes --> Evento : "controla"
    SistemaSubmissoes --> Artigo : "controla"
    SistemaSubmissoes --> Revisao : "coordena"
    SistemaSubmissoes --> Parecer : "processa"
    SistemaSubmissoes --> CanalNotificacao : "orquestra"
    SistemaSubmissoes --> ServicoEmail : "utiliza"

    note "Padrão Facade\nFornecer interface simplificada\nPara subsistema complexo"
```

---

## 🔌 Padrão: STRATEGY - ServicoEmail

Permite trocar implementação de email sem alterar código cliente.

```mermaid
classDiagram
    class ServicoEmail {
        <<interface>>
        +enviar(String destinatario, String assunto, String corpo)*
    }

    class ServicoEmailSmtp {
        -host: String
        -porta: int
        -usuario: String
        -senha: String
        +enviar(String, String, String)
        +deMeioAmbiente(): ServicoEmailSmtp
    }

    class ServicoEmailMemoria {
        -emails: List
        +enviar(String, String, String)
    }

    class SistemaSubmissoes {
        -servicoEmail: ServicoEmail
    }

    class Main {
        +main(String[])
    }

    ServicoEmail <|.. ServicoEmailSmtp
    ServicoEmail <|.. ServicoEmailMemoria
    SistemaSubmissoes --> ServicoEmail : "injeta"
    Main --> SistemaSubmissoes : "cria"

    note "Padrão Strategy + Injeção de Dependência\nTodo teste: ServicoEmailMemoria\nProdução: ServicoEmailSmtp"
```

---

## 📦 Arquitetura em Camadas

```mermaid
graph TB
    subgraph Apresentacao["🎨 Camada de Apresentação"]
        Main["Main.java<br/>Orquestração da simulação"]
    end

    subgraph Aplicacao["⚙️ Camada de Aplicação"]
        Sistema["SistemaSubmissoes<br/>(Facade/Orchestrator)"]
        Acoes["AcaoAdministrativa<br/>(Command Pattern)"]
    end

    subgraph Dominio["🧠 Camada de Domínio"]
        Entidades["Usuario, Evento<br/>Artigo, Revisao<br/>Parecer, DashboardResumo"]
        Estados["StatusArtigo<br/>(State Pattern)"]
        Categorias["CategoriaEvento<br/>(Strategy Pattern)"]
        Valores["Veredito, EstadoRevisao<br/>TipoNotificacao"]
    end

    subgraph Infraestrutura["🔧 Camada de Infraestrutura"]
        Email["ServicoEmail<br/>(Strategy Pattern)"]
        Notificacoes["CanalNotificacoes<br/>(Observer Pattern)"]
        Geradores["GeradorEmail<br/>(Template Method Pattern)"]
    end

    Main --> Sistema
    Sistema --> Acoes
    Sistema --> Entidades
    Sistema --> Estados
    Sistema --> Categorias
    Sistema --> Notificacoes
    Notificacoes --> Email
    Notificacoes --> Geradores
    Geradores --> Email

    style Apresentacao fill:#FFE5E5
    style Aplicacao fill:#E5F5FF
    style Dominio fill:#E5FFE5
    style Infraestrutura fill:#FFF5E5
```

---

## 🔄 Fluxo de Distribuição Automática

Demonstra a lógica inteligente de afinidade e balanceamento de carga.

```mermaid
graph LR
    A["📄 Artigo com áreas<br/>IA, Machine Learning"] 
    B["👨 Revisor 1<br/>IA, Deep Learning<br/>Carga: 2"]
    C["👨 Revisor 2<br/>Software, ML<br/>Carga: 1"]
    D["👨 Revisor 3<br/>Software, Visão<br/>Carga: 3"]
    
    A --> Score1["⭐⭐ Afinidade<br/>Score: 2/3 match"]
    A --> Score2["⭐⭐⭐ Afinidade<br/>Score: 2/2 match"]
    A --> Score3["⭐ Afinidade<br/>Score: 0/2 match"]
    
    Score1 --> R1["✓ Revisor 1 selecionado<br/>(Afinidade alta<br/>+ Carga baixa)"]
    
    style A fill:#FFE5E5
    style B fill:#E5F5FF
    style C fill:#E5FFE5
    style D fill:#FFF5E5
    style Score1 fill:#FFE5CC
    style Score2 fill:#CCE5FF
    style Score3 fill:#FFCCCC
    style R1 fill:#E5FFE5
```

---

## 📊 Fluxo de Estados de Revisão

Estados internos da revisão durante o processo.

```mermaid
stateDiagram-v2
    [*] --> Pendente: criar Revisao

    Pendente --> Aceita: aceitarRevisao()
    Pendente --> Rejeitada: revisor recusa
    
    Aceita --> Concluida: concluirRevisao(Parecer)
    
    Concluida --> [*]
    Rejeitada --> [*]

    note right of Pendente
        Aguardando resposta do revisor
    end note
    
    note right of Aceita
        Revisor aceitou a revisão
        Aguardando parecer
    end note
    
    note right of Concluida
        Parecer entregue
        Revisão finalizada
    end note
```

---

## 🎯 Matriz de Responsabilidades (RACI)

| Responsabilidade | SistemaSubmissoes | Usuario | Artigo | Revisao | CanalNotificacoes | ServicoEmail |
|---|:---:|:---:|:---:|:---:|:---:|:---:|
| Criar evento | **R** | - | - | - | - | - |
| Cadastrar usuário | **R** | **A** | - | - | - | - |
| Receber submissão | **R** | **A** | **C** | - | - | - |
| Validar prazo | **R** | - | **C** | - | - | - |
| Distribuir artigos | **R** | - | **C** | **A** | **I** | - |
| Aceitar revisão | **R** | **A** | - | **C** | - | - |
| Concluir revisão | **R** | **A** | **C** | **A** | **I** | - |
| Gerar parecer | - | **A** | **C** | **R** | - | - |
| Notificar | **R** | - | **C** | - | **A** | **C** |
| Enviar email | **R** | - | - | - | **C** | **A** |

**Legenda**: R=Responsável, A=Accountable, C=Consultado, I=Informado

---

## 📈 Sequência de Fluxo Principal

Mostra a sequência de chamadas no cenário completo.

```mermaid
sequenceDiagram
    actor User
    participant Main
    participant SistemaSubmissoes as Sistema
    participant Evento
    participant Artigo as Artigo
    participant Revisao
    participant Canal as CanalNotif
    participant Email as ServicoEmail

    User->>Main: main()
    Main->>Sistema: cadastrarUsuario()
    Main->>Sistema: cadastrarAreaTematica()
    Main->>Sistema: iniciarNovoEvento()
    
    Main->>Sistema: registrarRevisorNoComite()
    
    Main->>Sistema: submeterArtigo()
    Sistema->>Evento: estaAberto()
    Sistema->>Artigo: new Artigo()
    
    Main->>Sistema: distribuirArtigosAutomaticamente()
    Sistema->>Revisao: new Revisao()
    Sistema->>Canal: notificar()
    Canal->>Email: enviar()
    
    Main->>Sistema: aceitarRevisao()
    Main->>Sistema: concluirRevisao()
    Sistema->>Canal: notificar()
    Canal->>Email: enviar()
    
    Main->>Sistema: fecharCicloRevisaoEPublicarResultado()
    Sistema->>Canal: notificar()
    Canal->>Email: enviar()
    
    Main->>Sistema: gerarDashboard()
    Main->>User: Simulação concluída
```

---

## 📋 Resumo de Padrões por Camada

| Camada | Padrão | Classe(s) | Benefício |
|--------|--------|-----------|-----------|
| **Aplicação** | Facade | SistemaSubmissoes | Interface unificada, encapsulamento |
| **Aplicação** | Command | AcaoAdministrativa | Operações encapsuladas, extensível |
| **Domínio** | State | StatusArtigo | Transições seguras, comportamento específico |
| **Domínio** | Strategy | CategoriaEvento | Flexibilidade, sem modificação |
| **Infraestrutura** | Strategy | ServicoEmail | Teste/Produção, extensível |
| **Infraestrutura** | Observer | CanalNotificacoes | Desacoplamento, 1:N |
| **Infraestrutura** | Template Method | GeradorEmail | Estrutura comum, customização |
| **Criação** | Builder | Artigo, Email | Construção validada, fluent API |

---

## 🧪 Cenários de Teste UML

### Teste 1: Distribuição com Afinidade
```
Artigo {IA, ML} --> Revisor {IA, ML, DL} ✅ (score 2/2)
                --> Revisor {SW, ML}    ✅ (score 1/2)
                --> Revisor {SW, VC}    ❌ (score 0/2, fallback)
```

### Teste 2: Balanceamento de Carga
```
Revisor A carga: 2 --> Recebe artigo (afinidade 2)
Revisor B carga: 3 --> Não recebe (carga alta)
Revisor C carga: 1 --> Recebe artigo (carga baixa)
```

### Teste 3: Conflito de Interesse
```
Artigo {Autor: Juan, Coautores: [Maria]}
  --> Revisor Juan  ❌ (é autor)
  --> Revisor Maria ❌ (é coautor)
  --> Revisor Pedro ✅ (sem conflito)
```

---

## 📚 Notação Usada

- **Interface** (<<interface>>) - contrato sem implementação
- **Abstract** (<<abstract>>) - classe base para herança
- **Enumeration** (<<enumeration>>) - tipos enumerados
- **→** Associação simples
- **→|** Herança (is-a)
- **--→** Dependência
- **◆--→** Composição (parte obrigatória)
- **○--→** Agregação (parte opcional)
- **\*--→** Multiplicidade (muitos)

---

**Última atualização**: 2026-07-01  
**Versão**: 1.0  
**Gerado com**: Mermaid Diagram Engine

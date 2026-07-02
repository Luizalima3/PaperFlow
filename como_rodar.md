# Como Colocar o Projeto em Funcionamento

---

## Pré-requisitos

Antes de começar, certifique-se de possuir o seguinte em seu ambiente de desenvolvimento:

- **Java Development Kit (JDK):** Versão 11 ou superior (recomendado JDK 17 ou posterior, compatível com Java 21)
- **IDE de sua preferência:** IntelliJ IDEA, VS Code ou Eclipse
- **Git:** Para clonagem e controle de versão do repositório
- **Maven:** Versão 3.8+ (opcional - pode usar Maven Wrapper)

---

## Passos para Configuração e Execução

### 1. Clonar o Repositório

Abra o seu terminal (cmd, PowerShell ou bash) e execute o comando abaixo para clonar o projeto para a sua máquina local:

```bash
git clone https://github.com/seu-usuario/paperflow.git
cd paperflow
```

Ou, se preferir usar SSH:

```bash
git clone git@github.com:seu-usuario/paperflow.git
cd paperflow
```

---

### 2. Estrutura de Diretórios

Após clonar o repositório, a estrutura do projeto será:

```
paperflow/
├── diagramas/
├── src/
│   ├── java/
│   │   └── br/edu/ifpb/paperflow/
│   │       ├── Main.java
│   │       ├── aplicacao/
│   │       │   ├── SistemaSubmissoes.java
│   │       │   └── acoes/
│   │       ├── dominio/
│   │       │   ├── Usuario.java
│   │       │   ├── Evento.java
│   │       │   ├── Artigo.java
│   │       │   ├── Revisao.java
│   │       │   ├── Parecer.java
│   │       │   ├── DashboardResumo.java
│   │       │   ├── Veredito.java
│   │       │   ├── StatusArtigo.java
│   │       │   └── categoria/
│   │       └── infra/
│   │           └── notificacao/
│   └── resources/
├── pom.xml
├── README.md
└── como_rodar.md
```

---

### 3. Compilação e Execução

O projeto utiliza **Maven** para gerenciamento de dependências e compilação. Existem várias formas de compilar e executar:

#### Opção 1 – Utilizando o Maven Wrapper (Recomendado para Windows)

> **Recomendado para Windows**, pois **não é necessário ter o Maven instalado**. Basta possuir o JDK configurado.

Abra o Prompt de Comando (CMD) ou PowerShell na pasta raiz do projeto e execute:

```bash
mvnw.cmd clean compile
```

Após a compilação bem-sucedida, execute a classe principal:

```bash
mvnw.cmd exec:java -Dexec.mainClass=br.edu.ifpb.paperflow.Main
```

---

#### Opção 2 – Utilizando o Maven Instalado

Caso o Maven já esteja instalado e configurado na variável de ambiente `PATH`, execute:

```bash
mvn clean compile
```

Após a compilação:

```bash
mvn exec:java -Dexec.mainClass=br.edu.ifpb.paperflow.Main
```

---

#### Opção 3 – Execução pela IDE (IntelliJ IDEA)

1. **Abra a pasta raiz do projeto:**
   - File → Open
   - Selecione a pasta `paperflow`

2. **Aguarde o Maven baixar dependências:**
   - A IDE automaticamente detectará `pom.xml` e baixará todas as dependências

3. **Localize a classe Main.java:**
   - Navegue até: `src/java/br/edu/ifpb/paperflow/Main.java`

4. **Execute a classe:**
   - Clique com o botão direito em `Main.java`
   - Selecione `Run 'Main.main()'`
   - Ou use o atalho: `Ctrl+Shift+F10` (Windows/Linux) ou `Ctrl+Shift+R` (Mac)

---

#### Opção 4 – Execução pela IDE (VS Code)

1. **Abra a pasta do projeto:**
   - File → Open Folder
   - Selecione a pasta `paperflow`

2. **Instale as extensões necessárias:**
   - Extension Pack for Java (Microsoft)
   - Maven for Java (Microsoft)

3. **Aguarde o Maven carregar:**
   - VS Code detectará `pom.xml` automaticamente

4. **Execute a classe Main:**
   - Abra `src/java/br/edu/ifpb/paperflow/Main.java`
   - Clique em `Run` acima do método `main`
   - Ou use o terminal integrado:

```bash
mvn exec:java -Dexec.mainClass=br.edu.ifpb.paperflow.Main
```

---

### 4. Compilação Apenas (Sem Execução)

Se desejar apenas compilar sem executar:

```bash
mvn clean compile
```

Após compilação bem-sucedida, os arquivos `.class` serão gerados em:

```
target/classes/br/edu/ifpb/paperflow/
```

---

### 5. Limpeza de Artefatos Anteriores

Para remover arquivos compilados anteriores e limpar o projeto:

```bash
mvn clean
```

Isso remove completamente o diretório `target/`.

---

## Esperado Durante a Execução

Quando a simulação for executada com sucesso, você verá na console:

```
=================================================
   Sistema de Submissao e Revisao de Artigos
   IFPB - Padrões de Projeto de Software
=================================================

[MODO TESTE] Emails serao apenas simulados (nao enviados).

[1] Cadastro de usuarios
[2] Evento e areas tematicas
[3] Comitê tecnico e revisores
[4] Submissao de artigos
[5] Distribuicao automatica
[6] Avaliacao e agregacao de resultado
[7] Encerramento e notificacoes
[8] Dashboard e resultado final
[9] Teste de envio e historico de emails
[SIMULADO] Email para: revisor.anaa@ifpb.edu.br
[SIMULADO] Email para: revisor.claraa@ifpb.edu.br
[SIMULADO] Email para: revisor.brunoo@ifpb.edu.br
...
=== RESULTADO FINAL ===
[SIMULADO] Email para: maria.lima.5@academico.ifpb.edu.br
```

Todos os emails estão sendo **simulados** (não são enviados de verdade, apenas exibidos na console).

---

## Modo Email Real (Opcional)

Para habilitar o envio de emails de verdade via SMTP/Gmail:

1. **Abra `src/java/br/edu/ifpb/paperflow/Main.java`**

2. **Localize as linhas referentes ao ServicoEmail (aproximadamente linha 20):**

```java
// Comentado: Email real via SMTP
// ServicoEmail servicoEmail = new ServicoEmailSmtp("smtp.gmail.com", 587, "seu-email", "sua-senha");

// Ativo: Email simulado
ServicoEmail servicoEmail = new ServicoEmailTeste();
```

3. **Para ativar emails reais:**
   - Comente a linha `ServicoEmailTeste`
   - Descomente a linha `ServicoEmailSmtp`
   - Substitua `seu-email` e `sua-senha` pelas suas credenciais

4. **Recompile e execute:**

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=br.edu.ifpb.paperflow.Main
```

> ⚠️ **Importante:** Se usar Gmail, gere uma [Senha de Aplicativo](https://myaccount.google.com/apppasswords) em vez de usar a senha convencional da conta.

---

## Solução de Problemas

### Erro: "O Java não é reconhecido como comando interno"

- **Causa:** JDK não está instalado ou não está no PATH
- **Solução:** 
  1. Instale o JDK 11+
  2. Configure a variável de ambiente `JAVA_HOME` apontando para a pasta de instalação do JDK
  3. Adicione `%JAVA_HOME%\bin` ao PATH

### Erro: "Maven não é reconhecido como comando interno"

- **Causa:** Maven não está no PATH ou Maven Wrapper não encontrado
- **Solução:** 
  1. Use `mvnw.cmd` em vez de `mvn` (Maven Wrapper incluído no projeto)
  2. Ou instale Maven e configure o PATH corretamente

### Erro: "Failed to resolve dependencies"

- **Causa:** Problemas com conexão à internet ou repositório Maven
- **Solução:**
  1. Verifique sua conexão com a internet
  2. Limpe o cache local do Maven:

```bash
mvn clean -U
```

### Erro: "Revisao nao encontrada para este revisor e artigo"

- **Causa:** Inconsistência nas contas de email entre registro e uso
- **Solução:** Verifique se os emails utilizados em cada etapa (cadastro, distribuição, revisão) estão exatamente iguais

---

## Comandos Úteis

| Comando | Descrição |
|---------|-----------|
| `mvn clean` | Remove arquivos compilados |
| `mvn compile` | Compila o projeto |
| `mvn clean compile` | Limpa e compila |
| `mvn exec:java -Dexec.mainClass=br.edu.ifpb.paperflow.Main` | Executa a classe Main |
| `mvn test` | Executa testes (se houver) |
| `mvn package` | Cria JAR executável |

---

## Verificação de Instalação

Para verificar se tudo está configurado corretamente:

```bash
# Verificar versão do Java
java -version

# Verificar versão do Maven
mvn -version
```

Ambos os comandos devem retornar versões instaladas sem erros.

---

## Próximos Passos

Após executar a simulação com sucesso:

1. **Explore o código-fonte** em `src/java/br/edu/ifpb/paperflow/`
3. **Visualize os diagramas** na pasta `diagramas/` (arquivos PDF)
4. **Estude os padrões** implementados em cada classe

---

*Para mais informações sobre o projeto, consulte o [README.md](README.md)*

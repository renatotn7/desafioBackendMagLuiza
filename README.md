
## Índice

-   [Guia de Configuração e Execução do Desafio Backend Magaluiza](#user-content-guia-de-configuração-e-execução-do-desafio-backend-magaluiza)
    
    -   [1 Pré-requisitos: Instalação de Ferramentas Essenciais](#user-content-1-pré-requisitos-instalação-de-ferramentas-essenciais)
        
        -   [1.1. Java Development Kit (JDK) 21](#user-content-11-java-development-kit-jdk-21)
            
            -   [Windows:](#user-content-windows)
                
            -   [Linux:](#user-content-linux)
                
        -   [1.2. Apache Maven 3.9.3](#user-content-12-apache-maven-393)
            
            -   [Windows:](#user-content-windows-1)
                
            -   [Linux:](#user-content-linux-1)
                
        -   [1.3. Docker e Docker Compose](#user-content-13-docker-e-docker-compose)
            
            -   [Windows:](#user-content-windows-2)
                
            -   [Linux:](#user-content-linux-2)
                
    -   [2 Clonar e Compilar a Aplicação](#user-content-2-clonar-e-compilar-a-aplicação)
        
    -   [3 Executar a Aplicação com Docker Compose](#user-content-3-executar-a-aplicação-com-docker-compose)
        
    -   [4 Acessar o PostgreSQL no Contêiner](#user-content-4-acessar-o-postgresql-no-contêiner)
        
-   [Guia da API](#user-content-guia-da-api)
    
    -   [1 Acesso à Documentação Interativa (Swagger UI)](#user-content-1-acesso-à-documentação-interativa-swagger-ui)
        
    -   [2 Autenticação e Registro de Usuários](#user-content-2-autenticação-e-registro-de-usuários)
        
        -   [2.1. Registro de Usuários](#user-content-21-registro-de-usuários)
            
        -   [2.2. Login de Usuários](#user-content-22-login-de-usuários)
            
        -   [2.3. Uso do Token JWT](#user-content-23-uso-do-token-jwt)
            
    -   [3 Gerenciamento de Produtos](#user-content-3-gerenciamento-de-produtos)
        
    -   [4 Gerenciamento de Clientes](#user-content-4-gerenciamento-de-clientes)
        
    -   [5 Gerenciamento de Produtos Favoritos](#user-content-5-gerenciamento-de-produtos-favoritos)

-   [Escolhas Tecnológicas no Projeto Desafio Backend Magaluiza](#user-content-escolhas-tecnológicas-no-projeto-desafio-backend-magaluiza)
    
    -   [1\. Base do Projeto: Spring Boot e Java 21](#user-content-1-base-do-projeto-spring-boot-e-java-21)
        
    -   [2\. Dependências Essenciais (Starters do Spring Boot)](#user-content-2-dependências-essenciais-starters-do-spring-boot)
        
    -   [3\. Banco de Dados: PostgreSQL e Flyway](#user-content-3-banco-de-dados-postgresql-e-flyway)
        
    -   [4\. Ferramentas de Desenvolvimento e Teste](#user-content-4-ferramentas-de-desenvolvimento-e-teste)
        
    -   [5\. Segurança e Documentação da API](#user-content-5-segurança-e-documentação-da-api)
        
    -   [6\. Estratégia de Build com Maven e Docker](#user-content-6-estratégia-de-build-com-maven-e-docker)
        

# Guia de Configuração e Execução do Desafio Backend Magaluiza

Este documento irá guiá-lo através da configuração do ambiente de desenvolvimento, compilação do projeto com Maven, execução da aplicação e seu banco de dados PostgreSQL usando Docker Compose, e como acessar o banco de dados dentro do container.

## 1\. Pré-requisitos: Instalação de Ferramentas Essenciais

Vamos começar instalando as ferramentas necessárias.

---

### 1.1. Java Development Kit (JDK) 21

O Java 21 é a versão LTS (Long-Term Support) recomendada para o Spring Boot 3.x.

#### Windows:

1.  **Baixar o Instalador:** Acesse o site oficial do [Eclipse Temurin](https://adoptium.net/temurin/releases/) (recomendado por ser uma distribuição OpenJDK estável). Escolha o **JDK 21** para **Windows**, arquitetura `x64`, e baixe o instalador `.msi`.
    
2.  **Executar o Instalador:** Execute o arquivo `.msi` baixado. Siga as instruções do instalador. É geralmente recomendado manter o caminho de instalação padrão (ex: `C:\Program Files\Java\jdk-21`).
    
3.  **Configurar Variáveis de Ambiente:**
    
    -   Pressione `Win + R`, digite `sysdm.cpl` e Enter.
        
    -   Clique em **"Variáveis de Ambiente..."**.
        
    -   Na seção **"Variáveis do sistema"** (inferior):
        
        -   Clique em **"Novo..."**.
            
        -   Em "Nome da variável", digite `JAVA_HOME`.
            
        -   Em "Valor da variável", digite o caminho da sua instalação do JDK (ex: `C:\Program Files\Java\jdk-21`). Clique "OK".
            
        -   Encontre a variável `Path` (ou `PATH`), selecione-a e clique em **"Editar..."**.
            
        -   Clique em **"Novo"** e adicione `%JAVA_HOME%\bin`.
            
        -   Mova essa entrada para cima na lista, se necessário, para garantir que seja prioridade. Clique "OK" em todas as janelas.
            
4.  **Verificar a Instalação:** Abra um **novo** Prompt de Comando (CMD) ou PowerShell e digite:
    
    Bash
    
    ```
    java -version
    javac -version
    ```
    
    Você deve ver a versão do Java 21.
    

#### Linux:

1.  **Instalação Via Gerenciador de Pacotes (Recomendado - Ubuntu/Debian Exemplo):**
    
    Bash
    
    ```
    sudo apt update
    sudo apt install openjdk-21-jdk -y
    ```
    
2.  **Configurar Variáveis de Ambiente (se não for configurado automaticamente):**
    
    -   Abra seu terminal e edite o arquivo de perfil do seu shell (ex: `~/.bashrc` ou `~/.zshrc`):
        
        Bash
        
        ```
        nano ~/.bashrc # Ou ~/.zshrc se usa Zsh
        ```
        
    -   Adicione as seguintes linhas no final do arquivo (ajuste o caminho se necessário):
        
        Bash
        
        ```
        export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 # Verifique o caminho exato da sua instalação
        export PATH=$JAVA_HOME/bin:$PATH
        ```
        
    -   Salve e feche o arquivo.
        
    -   Aplique as mudanças:
        
        Bash
        
        ```
        source ~/.bashrc # Ou ~/.zshrc
        ```
        
3.  **Verificar a Instalação:**
    
    Bash
    
    ```
    java -version
    javac -version
    ```
    
    Você deve ver a versão do Java 21.
    

---

### 1.2. Apache Maven 3.9.3

#### Windows:

1.  **Baixar o Binário:** Acesse o site do [Apache Maven](https://maven.apache.org/download.cgi) e baixe o arquivo `apache-maven-3.9.11-bin.zip`.
    
2.  **Extrair:** Descompacte o arquivo ZIP em um diretório de sua escolha (ex: `C:\Program Files\Apache\apache-maven-3.9.11`).
    
3.  **Configurar Variáveis de Ambiente:**
    
    -   Siga os passos 3 do JDK para abrir as "Variáveis de Ambiente".
        
    -   Na seção **"Variáveis do sistema"**:
        
        -   Clique em **"Novo..."**.
            
        -   Em "Nome da variável", digite `M2_HOME`.
            
        -   Em "Valor da variável", digite o caminho da sua instalação do Maven (ex: `C:\Program Files\Apache\apache-maven-3.9.11`). Clique "OK".
            
        -   Encontre a variável `Path` (ou `PATH`), selecione-a e clique em **"Editar..."**.
            
        -   Clique em **"Novo"** e adicione `%M2_HOME%\bin`. Clique "OK" em todas as janelas.
            
4.  **Verificar a Instalação:** Abra um **novo** Prompt de Comando (CMD) ou PowerShell e digite:
    
    Bash
    
    ```
    mvn -v
    ```
    
    Você deve ver a versão do Maven 3.9.3.
    

#### Linux:

1.  **Baixar e Extrair:**
    
    Bash
    
    ```
    wget https://archive.apache.org/dist/maven/maven-3/3.9.3/binaries/apache-maven-3.9.11-bin.tar.gz
    sudo tar -xvzf apache-maven-3.9.11-bin.tar.gz -C /opt/
    sudo mv /opt/apache-maven-3.9.11 /opt/maven
    ```
    
2.  **Configurar Variáveis de Ambiente:**
    
    -   Edite o arquivo de perfil do seu shell (ex: `~/.bashrc` ou `~/.zshrc`):
        
        Bash
        
        ```
        nano ~/.bashrc # Ou ~/.zshrc
        ```
        
    -   Adicione as seguintes linhas no final do arquivo:
        
        Bash
        
        ```
        export M2_HOME=/opt/maven
        export PATH=$M2_HOME/bin:$PATH
        ```
        
    -   Salve, feche e aplique as mudanças:
        
        Bash
        
        ```
        source ~/.bashrc # Ou ~/.zshrc
        ```
        
3.  **Verificar a Instalação:**
    
    Bash
    
    ```
    mvn -v
    ```
    
    Você deve ver a versão do Maven 3.9.11.
    

---

### 1.3. Docker e Docker Compose

#### Windows:

1.  **Baixar e Instalar Docker Desktop:** Acesse o site oficial do [Docker Desktop](https://www.docker.com/products/docker-desktop/) e baixe o instalador para Windows.
    
2.  **Executar o Instalador:** Siga as instruções. O Docker Desktop instala o Docker Engine, Docker CLI e Docker Compose. Certifique-se de habilitar o WSL 2 (Windows Subsystem for Linux 2) se for solicitado, pois ele é a forma mais performática de rodar Docker no Windows.
    
3.  **Verificar a Instalação:** Abra um novo Prompt de Comando (CMD) ou PowerShell e digite:
    
    Bash
    
    ```
    docker --version
    docker compose version
    ```
    
    Você deve ver as versões do Docker e Docker Compose.
    

#### Linux:

1.  **Instalar Docker Engine:** Siga as instruções do site oficial do Docker para sua distribuição Linux. O método recomendado geralmente envolve adicionar o repositório Docker e usar seu gerenciador de pacotes.
    
    -   **Para Ubuntu/Debian (exemplo):**
        
        Bash
        
        ```
        sudo apt update
        sudo apt install ca-certificates curl gnupg
        sudo install -m 0755 -d /etc/apt/keyrings
        curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
        sudo chmod a+r /etc/apt/keyrings/docker.gpg
        echo \
          "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
          "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
          sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
        sudo apt update
        sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
        ```
        
2.  **Adicionar Seu Usuário ao Grupo Docker (Recomendado):** Para executar comandos Docker sem `sudo`:
    
    Bash
    
    ```
    sudo usermod -aG docker $USER
    newgrp docker # Aplique a mudança imediatamente ou faça logout/login
    ```
    
3.  **Verificar a Instalação:**
    
    Bash
    
    ```
    docker --version
    docker compose version
    ```
    
    Você deve ver as versões do Docker e Docker Compose.
    

---

## 2\. Clonar e Compilar a Aplicação

Agora que suas ferramentas estão instaladas, vamos preparar o projeto.

1.  **Clonar o Repositório:** Abra seu terminal ou Prompt de Comando e clone o projeto:
    
    Bash
    
    ```
    git clone https://github.com/renatotn7/desafioBackendMagLuiza.git
    cd desafioBackendMagLuiza
    ```
    
2.  **Compilar o JAR Executável:**
    
    -   Certifique-se de que você está no diretório `desafioBackendMagLuiza` (onde está o `pom.xml`).
        
    -   Use o Maven para limpar e empacotar a aplicação. Este comando irá compilar o código e criar um arquivo `.jar` executável na pasta `target/`.
        
    
    Bash
    
    ```
    mvn clean package
    ```
    -   Após a execução bem-sucedida, você deve encontrar o arquivo `favoritos-0.0.1-SNAPSHOT.jar` dentro da pasta `target/`.
        

---

## 3\. Executar a Aplicação com Docker Compose

O Docker Compose é ideal para subir a aplicação Spring Boot e o banco de dados PostgreSQL juntos.

1.  **Certifique-se de que o Docker Desktop (Windows) ou o serviço Docker (Linux) está rodando.**
    
2.  **Navegue até o diretório raiz do projeto** (`desafioBackendMagLuiza`), onde o `docker-compose.yml` e a pasta `target/` estão.
    
3.  **Inicie os serviços com Docker Compose:**
    
    -   Use o comando `docker compose up -d --build`.
        
    -   `-d`: Executa os contêineres em segundo plano (detached mode).
        
    -   `--build`: Garante que a imagem da sua aplicação (`app`) seja reconstruída usando o `Dockerfile`, incorporando o JAR mais recente. Isso é crucial após a compilação com Maven.
        
    
    Bash
    
    ```
    docker compose up -d --build
    ```
    
    Você verá o Docker criando as imagens (se ainda não existirem) e iniciando os contêineres `app` e `db`.
    
4.  **Verificar o Status dos Contêineres:**
    
    Bash
    
    ```
    docker ps
    ```
    
    Você deve ver `app` e `db` rodando com o status `Up`.
    
5.  **Acessar a Aplicação:** Sua aplicação Spring Boot estará disponível em `http://localhost:8080`.
    

---

## 4\. Acessar o PostgreSQL no Contêiner

Para acessar o banco de dados PostgreSQL que está rodando dentro do seu contêiner, você usará a porta que você foi mapeada no `docker-compose.yml`.

Seu `docker-compose.yml` tem:

YAML

```
  db:
    # ...
    ports:
      - "5433:5432"
```

Isso significa que a porta `5432` **dentro do contêiner `db`** está mapeada para a porta `5433` **na sua máquina host**.

Para se conectar ao PostgreSQL a partir de um cliente SQL (como pgAdmin, DBeaver, psql):

-   **Host:** `localhost` (ou `127.0.0.1`)
    
-   **Porta:** `5433`
    
-   **Nome do Banco de Dados:** `favoritos_aiqfome_db` (conforme configurado em `POSTGRES_DB`)
    
-   **Usuário:** `favoritos_aiqfome_user` (conforme configurado em `POSTGRES_USER`)
    
-   **Senha:** `1234567890` (conforme configurado em `POSTGRES_PASSWORD`)
    

Você pode, por exemplo, usar o cliente de linha de comando `psql` se ele estiver instalado na sua máquina:

Bash

```
psql -h localhost -p 5433 -U favoritos_aiqfome_user -d favoritos_aiqfome_db
```

Ele pedirá a senha (`1234567890`).

---

Com este guia, você deve conseguir configurar seu ambiente, rodar a aplicação e interagir com o banco de dados sem problemas! 

## Guia da API

Este documento detalha as funcionalidades da API "Desafio Backend Magazine Luiza", cobrindo autenticação, gerenciamento de usuários, clientes e produtos favoritos.

---

### 1\. Acesso à Documentação Interativa (Swagger UI)
Você pode acessar a documentação swagger da API para explorar todos os endpoints através desse documento swagger.json.
Para realizar testes basta importar no Postman por exemplo ou uma outra ferramenta de consulta de apis que possa rodar local
O json se encontra aqui:  [swagger.json](swagger.json).
---

### 2\. Autenticação e Registro de Usuários

A API utiliza JWT (JSON Web Token) para autenticação.

#### 2.1. Registro de Usuários

Crie novos usuários com diferentes níveis de acesso. obrigatório pois na aplicacao não há usuários quando inicia. Crie dois, um ADM e um USER

-   **Endpoint:** `POST /auth/v1/register`
    
-   **Corpo da Requisição (JSON):**
    
    JSON
    
    ```
    {
      "login": "nomeDeUsuario",
      "password": "suaSenha",
      "role": "ADMIN"  // ou "USER"
    }
    ```
    
-   **Roles Disponíveis:**
    
    -   `ADMIN`: Possui acesso total a todos os endpoints.
        
    -   `USER`: Possui acesso limitado, principalmente aos endpoints de produtos.
        

#### 2.2. Login de Usuários

Autentique-se para obter um token JWT.

-   **Endpoint:** `POST /auth/v1/login`
    
-   **Corpo da Requisição (JSON):**
    
    JSON
    
    ```
    {
      "login": "nomeDeUsuario",
      "password": "suaSenha"
    }
    ```
    
-   **Resposta:** Um **token JWT assinado digitalmente**. Este token é essencial para acessar os endpoints protegidos.
    

#### 2.3. Uso do Token JWT

Para acessar os demais serviços da API, inclua o token JWT obtido no login no cabeçalho `Authorization` de suas requisições, prefixado com `Bearer`.

-   **Exemplo de Cabeçalho:**
    
    ```
    Authorization: Bearer SEU_TOKEN_JWT_AQUI
    ```
    

---

### 3\. Gerenciamento de Produtos

Estes endpoints permitem a consulta de produtos, que são obtidos de uma API externa (FakeStoreAPI).

-   **Acesso:** Disponível para usuários com as roles **`ADMIN`** ou **`USER`**.
    
-   **Listar Todos os Produtos:**
    
    -   `GET /api/v1/products`
        
        -   Obtém uma lista de todos os produtos disponíveis na API externa.
            
-   **Obter Um Único Produto:**
    
    -   `GET /api/v1/products/{id}`
        
        -   Obtém os detalhes de um produto específico, utilizando seu ID.
            

---

### 4\. Gerenciamento de Clientes

Estes endpoints permitem operações CRUD (Criação, Leitura, Atualização e Exclusão) sobre os clientes.

-   **Acesso:** Restrito apenas a usuários com a role **`USER`**.
    
-   **Criar um Cliente:**
    
    -   `POST /api/v1/clients`
        
        -   Cria um novo registro de cliente.
            
-   **Obter Todos os Clientes:**
    
    -   `GET /api/v1/clients`
        
        -   Lista todos os clientes cadastrados.
            
-   **Obter Um Único Cliente:**
    
    -   `GET /api/v1/clients/{id}`
        
        -   Obtém os detalhes de um cliente específico pelo seu ID.
            
-   **Atualizar um Cliente:**
    
    -   `PUT /api/v1/clients/{id}`
        
        -   Atualiza as informações de um cliente existente.
            
-   **Deletar um Cliente:**
    
    -   `DELETE /api/v1/clients/{id}`
        
        -   Remove um cliente do sistema pelo seu ID.
            

---

### 5\. Gerenciamento de Produtos Favoritos

Estes endpoints permitem que os usuários gerenciem sua lista de produtos favoritos.

-   **Acesso:** Restrito apenas a usuários com a role **`USER`**.
    
-   **Adicionar um Produto Favorito:**
    
    -   `POST /api/v1/favorites`
        
        -   Adiciona um produto à lista de favoritos de um cliente.
            
-   **Listar Favoritos por Cliente:**
    
    -   `GET /api/v1/favorites/client/{clientId}`
        
        -   Lista todos os produtos favoritos de um cliente específico.
            
-   **Deletar um Produto Favorito:**
    
    -   `DELETE /api/v1/favorites/{favoriteId}`
        
        -   Remove um produto da lista de favoritos, utilizando o ID do registro de favorito (não o ID do produto).
          

---
## Escolhas Tecnológicas no Projeto Desafio Backend Magaluiza

Este documento detalha as principais escolhas tecnológicas e dependências configuradas no arquivo `pom.xml` do projeto "Desafio Backend Magazine Luiza", explicando o racional por trás de cada uma, com foco no uso do Java 21 e na compatibilidade com Docker.

---

### 1\. Base do Projeto: Spring Boot e Java 21

O coração do projeto é o **Spring Boot**, utilizando a versão `3.2.5` do `spring-boot-starter-parent`. Essa escolha é fundamental por diversos motivos:

-   **Produtividade:** Spring Boot oferece uma experiência de desenvolvimento rápida e simplificada com auto-configuração, starters e um servidor embarcado (Tomcat por padrão), eliminando a necessidade de configurações complexas.
    
-   **Conveniência:** Facilita a criação de aplicações _stand-alone_ (autônomas) que podem ser executadas como JARs.
    
-   **Ecossistema Amplo:** Permite integrar facilmente outras bibliotecas e frameworks do ecossistema Spring.
    

A escolha do **Java 21** como a versão do JDK é estratégica:

-   **Versão LTS (Long-Term Support):** Java 21 é uma versão de suporte de longo prazo, garantindo atualizações e suporte por um período estendido, o que é ideal para projetos em produção.
    
-   **Compatibilidade com Spring Boot 3.x:** As versões 3.x do Spring Boot exigem Java 17 ou superior. O Java 21 oferece os recursos mais recentes e otimizações de performance disponíveis na plataforma Java, permitindo que o projeto aproveite ao máximo as inovações da linguagem.
    
-   **Containerização Eficiente:** O Java 21, especialmente quando usado com imagens base como `eclipse-temurin:21-jre-alpine` no Docker, é otimizado para ambientes de contêineres, resultando em imagens mais leves e inicialização mais rápida da aplicação.
    

---

### 2\. Dependências Essenciais (Starters do Spring Boot)

O `pom.xml` faz uso extensivo dos "Starters" do Spring Boot, que são conjuntos de dependências pré-configuradas para funcionalidades comuns.

-   **`spring-boot-starter-data-jpa`**: Habilita a persistência de dados utilizando JPA (Java Persistence API) com Hibernate como implementação padrão. Isso permite interagir com bancos de dados relacionais de forma orientada a objetos, minimizando o código boilerplate.
    
-   **`spring-boot-starter-validation`**: Adiciona suporte à validação de dados usando a Bean Validation API (JSR 380). Isso é crucial para garantir que os dados recebidos via API REST estejam em um formato e conteúdo válidos, melhorando a robustez da aplicação.
    
-   **`spring-boot-starter-security`**: Integra o Spring Security, fornecendo um robusto framework para autenticação e autorização. Essencial para proteger os endpoints da API, gerenciando acesso baseado em roles (ADMIN/USER) e implementando a lógica de login/registro.
    
-   **`spring-boot-starter-web`**: Inclui todas as dependências necessárias para construir aplicações web e APIs RESTful, como Spring MVC e o servidor Tomcat embarcado. É a base para expor os endpoints da sua aplicação.
    

---

### 3\. Banco de Dados: PostgreSQL e Flyway

-   **`org.postgresql:postgresql`**: O driver JDBC para PostgreSQL. Esta dependência é marcada com `<scope>runtime</scope> no pom da aplicacão`, o que significa que ela só é necessária em tempo de execução, não durante a compilação. Isso é ideal para ambientes onde o banco de dados é externo à aplicação principal, como em um contêiner Docker separado.
    
-   **`org.flywaydb:flyway-database-postgresql`**: Integra o Flyway, uma ferramenta de _database migration_. O Flyway é essencial para gerenciar o esquema do banco de dados de forma versionada e controlada. Ele garante que as alterações no schema (criação de tabelas, modificação de colunas) sejam aplicadas de forma consistente em diferentes ambientes (desenvolvimento, teste, produção), o que é particularmente útil em um cenário de contêineres onde o banco de dados pode ser recriado ou atualizado.
    

---

### 4\. Ferramentas de Desenvolvimento e Teste

    
-   **`spring-boot-devtools`**: Uma dependência de desenvolvimento que oferece recursos como reinício rápido da aplicação e LiveReload. Embora seja útil em desenvolvimento local, ela é marcada como `<optional>true</optional>` e `<scope>runtime</scope> no pom da aplicacão`, o que significa que não será empacotada no JAR final que vai para o Docker, mantendo a imagem do contêiner mais leve e segura.
        
-   **`org.projectlombok:lombok`**: Uma biblioteca popular que reduz o código boilerplate gerando automaticamente getters, setters, construtores, métodos `equals`, `hashCode` e `toString`, além de outras funcionalidades como `@Slf4j` para logs. Melhora a legibilidade e concisão do código. A exclusão de Lombok na configuração do `spring-boot-maven-plugin` é uma boa prática para evitar conflitos potenciais ou dependências desnecessárias no JAR final, pois o próprio Maven Plugin pode reprocessar classes anotadas com Lombok.
    

---

### 5\. Segurança e Documentação da API

-   **`com.auth0:java-jwt`**: Biblioteca para a geração e validação de JWTs. É fundamental para a segurança da API, permitindo a autenticação de usuários e a proteção de rotas. A escolha do JWT para autenticação é alinhada com as melhores práticas para APIs RESTful sem estado, o que se adapta muito bem a ambientes conteinerizados.
    
-   **`org.springdoc:springdoc-openapi-starter-webmvc-ui`**: Habilita a geração automática de documentação da API no formato OpenAPI (Swagger). Isso permite que você acesse uma UI interativa (`/swagger-ui/index.html`) para testar e entender os endpoints da sua API, facilitando a integração com outros desenvolvedores e sistemas.
    

---

### 6\. Estratégia de Build com Maven e Docker


-   **`spring-boot-maven-plugin`**: Este plugin é responsável por empacotar a aplicação Spring Boot em um JAR executável. Ele cria um JAR "fat" ou "uber-JAR", que inclui todas as dependências da aplicação.
    
-   **Exclusão de Lombok na Configuração do Plugin:** A configuração `<excludes> no pom` para `lombok` dentro do plugin é uma prática recomendada. Embora Lombok seja útil em tempo de compilação para gerar código, ele não precisa estar no JAR final em tempo de execução, pois o código já foi gerado. Isso ajuda a manter o tamanho do JAR otimizado e evita possíveis problemas de classpath em ambientes de produção.
    

A arquitetura do projeto, com um JAR autônomo e o uso de `Dockerfile` para conteinerização, reflete uma abordagem moderna de desenvolvimento:

-   **Portabilidade com Docker:** O JAR final pode ser facilmente empacotado em uma imagem Docker (como visto em `Dockerfile`), que inclui um JRE mínimo (Java 21 JRE Alpine) e o JAR da aplicação. Isso garante que a aplicação execute de forma consistente em qualquer ambiente que suporte Docker, eliminando problemas de "funciona na minha máquina".
    
-   **Isolamento:** Docker oferece isolamento para a aplicação e suas dependências, incluindo o banco de dados PostgreSQL (via Docker Compose), o que simplifica o setup e o deploy.
    
-   **Desenvolvimento e Produção Similares:** O uso de Docker Compose para orquestrar a aplicação e o banco de dados em desenvolvimento espelha de perto como a aplicação será implantada em produção, reduzindo surpresas.

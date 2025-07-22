
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

1.  **Baixar o Binário:** Acesse o site do [Apache Maven](https://maven.apache.org/download.cgi) e baixe o arquivo `apache-maven-3.9.3-bin.zip`.
    
2.  **Extrair:** Descompacte o arquivo ZIP em um diretório de sua escolha (ex: `C:\Program Files\Apache\apache-maven-3.9.3`).
    
3.  **Configurar Variáveis de Ambiente:**
    
    -   Siga os passos 3 do JDK para abrir as "Variáveis de Ambiente".
        
    -   Na seção **"Variáveis do sistema"**:
        
        -   Clique em **"Novo..."**.
            
        -   Em "Nome da variável", digite `M2_HOME`.
            
        -   Em "Valor da variável", digite o caminho da sua instalação do Maven (ex: `C:\Program Files\Apache\apache-maven-3.9.3`). Clique "OK".
            
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
    wget https://archive.apache.org/dist/maven/maven-3/3.9.3/binaries/apache-maven-3.9.3-bin.tar.gz
    sudo tar -xvzf apache-maven-3.9.3-bin.tar.gz -C /opt/
    sudo mv /opt/apache-maven-3.9.3 /opt/maven
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
    
    Você deve ver a versão do Maven 3.9.3.
    

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

Para acessar o banco de dados PostgreSQL que está rodando dentro do seu contêiner, você usará a porta que você mapeou no `docker-compose.yml`.

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

Com a aplicação em execução, você pode acessar a documentação interativa da API (Swagger UI) para explorar todos os endpoints e testá-los diretamente:

-   **URL:** `http://localhost:8080/swagger-ui/index.html`
    

---

### 2\. Autenticação e Registro de Usuários

A API utiliza JWT (JSON Web Token) para autenticação.

#### 2.1. Registro de Usuários

Crie novos usuários com diferentes níveis de acesso.

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

-   **Acesso:** Restrito apenas a usuários com a role **`ADMIN`**.
    
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

-   **Acesso:** Restrito apenas a usuários com a role **`ADMIN`**.
    
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

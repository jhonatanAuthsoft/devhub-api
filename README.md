
problema de merge na branch apagando arquivos

✅ Passo a passo definitivo
1. Faça o merge normalmente

   git checkout sua_branch
   git merge main
   
Trazer tudo da main (✅)

Mas também apagar seus arquivos/pastas que não existem mais na main (❌)

2. você pode restaurar em lote com este comando:

git diff --name-only --diff-filter=D 'HEAD@{1}' HEAD | ForEach-Object { git restore --source='HEAD@{1}' $_ }
Esse comando identifica todos os arquivos deletados e os restaura de como estavam antes do merge.

4. Adicione e commit

   git add .
   git commit -m "Merge main preservando arquivos próprios"


# 1.  **Visão Geral**

>O SeuLarMS é uma aplicação backend robusta, desenvolvida em Spring Boot, que serve como a espinha dorsal
de um sistema de gerenciamento de imóveis e assinaturas. A API fornece endpoints para cadastro de usuários,
gerenciamento de planos, processamento de pagamentos, listagem de imóveis e muito mais.

# 2.  **Pré-requisitos**
>Antes de iniciar, certifique-se de que você tem as seguintes ferramentas instaladas e configuradas no seu ambiente de desenvolvimento:
>
> certifique-se de que você tem as seguintes ferramentas instaladas e configuradas no seu ambiente de desenvolvimento:
>
> >• Java Development Kit (JDK): Versão 17 ou superior.
>>
>>• Apache Maven: Para gerenciamento de dependências e build do projeto.
>>
>> • PostgreSQL: Banco de dados utilizado pela aplicação.
>>
>> • IDE de sua preferência: IntelliJ IDEA, Eclipse ou VS Code com as extensões para Java/Spring.


# 3.  **Como Rodar o Sistema**
>Siga os passos abaixo para configurar e executar a aplicação localmente.
### 3.1  **Clonar o Repositório**
    git clone <url_do_seu_repositorio_git>
    cd modelo

### 3.2  **Configurar o Banco de Dados**

>A aplicação utiliza PostgreSQL e Flyway para gerenciar as migrações do banco de dados.
>> 1.Acesse seu servidor PostgreSQL.
>>
>> 2.Crie um banco de dados para a aplicação:

    CREATE DATABASE modelo_db;

> 3. Crie um usuário e senha para a aplicação e conceda as permissões necessárias:

    CREATE USER modelo_user WITH PASSWORD 'sua_senha_segura';
    GRANT ALL PRIVILEGES ON DATABASE modelo_db TO modelo_user;

### 3.3  **Configurar Variáveis de Ambiente**
>A aplicação depende de várias chaves de API e configurações externas. É uma prática de
segurança essencial NÃO colocar essas informações diretamente no arquivo __application.properties__.
>
> Em vez disso, configure-as como variáveis de ambiente.Crie um arquivo de configuração na sua IDE ou exporte as seguintes variáveis no seu terminal:

``` 
# Configuração do Banco de Dados
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/modelo_db
SPRING_DATASOURCE_USERNAME=modelo_user
SPRING_DATASOURCE_PASSWORD=sua_senha_segura

# Configuração do Flyway
SPRING_FLYWAY_URL=${SPRING_DATASOURCE_URL}
SPRING_FLYWAY_USER=${SPRING_DATASOURCE_USERNAME}
SPRING_FLYWAY_PASSWORD=${SPRING_DATASOURCE_PASSWORD}

# Segredo para geração de Token JWT (use um valor longo e aleatório)
JWT_SECRET=seu_segredo_super_secreto_para_jwt


```

### 3.3  **Executar a Aplicação**
>Você pode rodar a aplicação de duas maneiras:
>> 1. __Via Maven (linha de comando)__: Navegue até a raiz do projeto e execute:

    mvn spring-boot:run

> 2. Via IDE: Abra a classe principal __ModeloApplication.java__ e execute-a como uma Aplicação Java.
>
> Após a inicialização, a aplicação estará disponível em http://localhost:8080.

>  3. Contêineres
> 
>  Abra um terminal na raiz do projeto e execute o comando:
> 
>     docker-compose up --build -d,
>  Parar os Contêineres
> 
>     docker-compose down
> Se quiser remover também o volume de dados do banco (para começar do zero na próxima vez), use: __docker-compose down -v__.
 
 

# 4.  **Gerenciamento de Banco de Dados com Flyway**
>Este projeto utiliza Flyway para gerenciar a evolução do esquema do banco de dados de forma automatizada e versionada. 
> 
>Isso garante que a estrutura do banco de dados esteja sempre em sincronia com o código da aplicação.
> 
> Ao iniciar a aplicação, o Spring Boot automaticamente aciona o Flyway, que:
> 1. Conecta-se ao banco de dados.
> 2. Cria uma tabela de controle chamada flyway_schema_history para rastrear quais migrações já foram aplicadas.
> 3. Escaneia a pasta de migrações em busca de novos scripts SQL.
> 4. Executa os novos scripts em ordem de versão.
> 
> **Importante: Se uma migração falhar, a aplicação não iniciará. Isso é uma medida de segurança para evitar inconsistências entre o código e o banco de dados.**

> ### Como Criar uma Nova Migração
> Para fazer uma alteração no banco de dados (criar uma tabela, adicionar uma coluna, etc.), siga estes passos:
> 1. Crie um arquivo .sql na pasta: src/main/resources/db/migration
> 2. Nomeie o arquivo seguindo a convenção obrigatória:
>
> `__VERSÃO>__<DESCRIÇÃO>.sql`
> * `V: A letra "V" maiúscula.`
> * `<VERSÃO>: Um número de versão (ex: 1, 2, 1.1). `
> 
> Use números sequenciais.•__: Dois underscores (underline duplo).•<DESCRIÇÃO>: Um texto descritivo sobre a alteração, usando underscores em vez de espaços.
> 
> **Exemplos de nomes de arquivos válidos**
>
>     V1__Create_table_usuarios.sql
>     V2__Create_table_planos_e_assinaturas.sql
>     V3__Add_coluna_status_na_tabela_usuarios.sql
> Basta criar o arquivo com o conteúdo SQL desejado. Na próxima vez que a aplicação iniciar, o Flyway aplicará a nova migração automaticamente.

# 5.  **Arquitetura do Sistema**
>A aplicação segue uma arquitetura em camadas, desacoplada e orientada a serviços, com integrações a serviços externos essenciais.
```
   Cliente (Browser / Mobile)    |
+---------------------------------+
                v
+---------------------------------+
     Modelo API (Spring Boot)    |
---------------------------------|
  1. Camada de Apresentação      |
     - Controllers (REST API)    |
     - Security (JWT, OAuth2)    |
     - DTOs e Mappers            |
---------------------------------|
  2. Camada de Negócio           |
     - Services                  |
     - Lógica de Negócio         |
---------------------------------|
  3. Camada de Dados             |
     - Repositories (JPA)        |
     - Entidades (Model)         |
     - Flyway (Migrations)       |
+---------------------------------+
          |
      v         v          v
+-----------+   +----------------------+   +--------------------------+
|PostgreSQL |   |  Serviços Externos   |   |   Infraestrutura Cloud   |
+-----------+   |----------------------|   |--------------------------|
                | - Servidor de Email  |   | - AWS S3 (Armazenamento) |
                | - Outros             |   +--------------------------+
                +----------------------+
```
# 6.  **Endpoints de Gerenciamento e Documentação**,

### 6.1  **Executar a Aplicação**
>Acessando a Documentação da API (Swagger)A API é autodocumentada usando springdoc-openapi. Isso gera uma interface
interativa onde você pode visualizar e testar todos os endpoints.

    URL Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

>Para testar endpoints protegidos:
>>1. Faça login na aplicação para obter um token JWT.
>>2. Na página do Swagger,clique no botão "Authorize" no canto superior direito.
>>3. Na janela que abrir, digite __Bearer <seu_token_jwt>__ e clique em "Authorize".
>>4. Agora você pode testar os endpoints que exigem autenticação.

### 6.2  **Acessando o Spring Boot Actuator**

>O Actuator expõe endpoints para monitoramento e gerenciamento da aplicação.
>
>URL Actuator: [http://localhost:8080/actuator](http://localhost:8080/actuator)

>Endpoints úteis:
>
>* /actuator/health: Verifica a saúde da aplicação (conexão com banco, disco, etc.).
>*  /actuator/info: Exibe informações gerais da aplicação.
>* /actuator/metrics: Fornece métricas detalhadas (uso de memória, requisições, etc.).
>*  /actuator/mappings: Lista todos os endpoints @RequestMapping da aplicação.

# 7.  **Principais Tecnologias e Dependências**,

>Framework Principal: Spring Boot 3.4.2
> * Linguagem: Java 17
> * Banco de Dados: PostgreSQL com Spring Data JPA (Hibernate).
> * Migrações de Banco: Flyway
> * Segurança: Spring Security (autenticação via JWT e OAuth2 com Google).
> * Documentação da API: springdoc-openapi (Swagger UI)
> * Armazenamento de Arquivos: AWS S3 SDK
> * Geração de PDF: iText 7
> * Utilitários: Lombok

# 8.  **Estrutura de Pacotes**,

O projeto segue uma estrutura de pacotes convencional para facilitar a organização do código:
```
authsoft.projeto
├── config/            # Classes de configuração (Security, OpenAPI, etc.)
├── controller/        # Controladores REST que expõem a API
│   └── dto/           # Data Transfer Objects (DTOs) para requisição e retorno
├── exception/         # Classes de exceções customizadas e handlers
├── mapper/            # Mapeadores de DTO para Entidade e vice-versa
├── model/             # Entidades JPA que representam as tabelas do banco
│   └── enums/         # Enumerações usadas no modelo
├── repository/        # Interfaces do Spring Data JPA para acesso a dados
├── service/           # Lógica de negócio da aplicação
│   └── imp/           # Implementações das interfaces de serviço
└── ProjetoApplication.java # Classe principal da aplicação
```
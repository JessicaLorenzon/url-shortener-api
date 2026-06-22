# URL Shortener API

Projeto baseado no roadmap de projetos do [roadmap.sh](https://roadmap.sh/projects/url-shortening-service), com o objetivo de praticar desenvolvimento de APIs RESTful com Java, persistência de dados e testes automatizados.

## Descrição

O **URL Shortener API** é uma aplicação backend que permite encurtar URLs longas em links curtos e rastrear o número de acessos.

A API permite:

- Criar URLs encurtadas a partir de links longos
- Redirecionar para a URL original usando o código curto
- Consultar estatísticas de acesso
- Gerenciar URLs criadas

Cada URL encurtada possui um identificador único e contador de acessos.

## Tecnologias utilizadas

- **Java** – Linguagem principal
- **Spring Boot** – Framework para construção da API
- **Spring Data JPA / Hibernate** – Mapeamento objeto-relacional
- **H2** – Banco de dados em memória
- **REST API** – Comunicação via HTTP
- **Maven** – Gerenciador de dependências
- **JUnit / RestAssured** – Testes unitários e de integração

## Como rodar o projeto

### 1. Clone o repositório

```bash
git clone https://github.com/JessicaLorenzon/url-shortener-api.git
```

### 2. Instale as dependências com o Maven

### 3. Inicie a aplicação com o Maven

A API estará acessível em http://localhost:8080

## Endpoints disponíveis

### 1. Criar URL curta

```http
POST /shorten
```

#### Payload (JSON):

```json
{
  "originalUrl": "https://www.example.com/some/long/url"
}
```

### 2. Recuperar URL original

```http
GET /shorten/{shotCode}
```

### 3. Atualizar URL curta

```http
PUT /shorten/{shotCode}
```

#### Payload (JSON):

```json
{
  "originalUrl": "https://www.example.com/some/updated/url"
}
```

### 4. Excluir URL curta

```http
DELETE /shorten/{shotCode}
```

### 5. Obter estatísticas da URL

```http
GET /shorten/{shotCode}/stats
```

### 6. Recuperar todas as URLs

```http
GET /shorten
```

### 7. Redirecionar para URL original

```http
GET /{shotCode}
```

## Testes

O projeto possui:

- **Testes unitários** utilizando **JUnit** para validação da lógica de negócio
- **Testes de integração** utilizando **RestAssured** para validação dos endpoints da API 

Os testes podem ser executados com:

```bash
mvn test
```
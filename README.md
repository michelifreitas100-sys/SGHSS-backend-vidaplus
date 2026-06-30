# SGHSS — Back-end

Projeto Multidisciplinar UNINTER · Estudo de caso VidaPlus

API REST em **Java + Spring Boot** com **PostgreSQL**, autenticação **JWT** e documentação via **Swagger**.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Banco | PostgreSQL |
| Autenticação | Spring Security + JWT |
| Documentação | Swagger UI (springdoc-openapi) |
| Build | Maven |

## Requisitos atendidos

- CRUD REST: `/pacientes`, `/profissionais`
- Autenticação JWT: `/auth/register`, `/auth/login`
- Persistência: JPA + PostgreSQL
- LGPD: CPF criptografado (AES), senhas com BCrypt, tabela `audit_logs`
- RBAC: papéis `ADMIN`, `PROFISSIONAL`, `PACIENTE`
- Tratamento de erros via `GlobalExceptionHandler`

## Como executar

**Pré-requisitos:** JDK 17+, Maven 3.8+, PostgreSQL

```sql
CREATE DATABASE sghss_db;
```

```bash
export DB_USER=postgres
export DB_PASSWORD=sua_senha
export JWT_SECRET=chave-aleatoria-minimo-32-caracteres
export APP_ENC_KEY=ChaveAES16Bytes!
```

```bash
mvn spring-boot:run
```

API disponível em `http://localhost:8080`  
Swagger em `http://localhost:8080/swagger-ui.html`

**Fluxo no Swagger:**
1. `POST /auth/register` → cria usuário
2. `POST /auth/login` → obtém token JWT
3. **Authorize** → cola `Bearer <token>`
4. Testa `/pacientes` e `/profissionais`

## Exemplos

```http
POST /auth/register
{ "nome": "Admin", "email": "admin@vidaplus.com", "senha": "senha123", "role": "ADMIN" }

POST /auth/login
{ "email": "admin@vidaplus.com", "senha": "senha123" }
→ { "token": "eyJ...", "tipo": "Bearer", "role": "ADMIN" }

POST /pacientes
Authorization: Bearer 
{ "nome": "João Silva", "cpf": "12345678900", "dataNascimento": "1990-05-10" }
```

## Estrutura

```
src/main/java/com/vidaplus/sghss/
├── config/       # Security e Swagger
├── controller/   # Endpoints REST
├── dto/          # Request/Response
├── model/        # Entidades JPA
├── repository/   # Spring Data JPA
├── security/     # JWT, AES, UserDetails
├── service/      # Regras de negócio
├── exception/    # Erros globais
└── audit/        # Logs LGPD
```




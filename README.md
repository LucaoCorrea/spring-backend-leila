# 💇‍♀️ Spring Backend - Cabeleleila Leila

Este projeto foi desenvolvido como parte de um **desafio técnico da DSIN**. A proposta envolve a construção de um sistema de agendamento para um salão de beleza, com funcionalidades voltadas para clientes e administradores, seguindo critérios práticos e organizacionais específicos.

## Repositórios 🗃️

- Backend: [Cabeleleila leila backend](https://github.com/LucaoCorrea/spring-backend-leila)
- Frontend: [Cabeleleila leila frontend](https://github.com/LucaoCorrea/react-frontend-leila)

## 📋 Descrição do Projeto

O sistema permite que clientes agendem serviços em horários disponíveis e que administradores gerenciem esses agendamentos e os serviços prestados. Entre os recursos estão:

- Autenticação com JWT
- Controle de usuários com `ROLE_USER` e `ROLE_ADMIN`
- Agendamento com múltiplos serviços
- Regras de edição e cancelamento com 48h de antecedência
- Cálculo de valor total do agendamento
- Painel gerencial de faturamento
- API REST com Spring Boot

---

## ✅ Requisitos mínimos para rodar o projeto

- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [PostgreSQL 12+](https://www.postgresql.org/)

---

## ⚙️ Configuração e Execução

1. **Clone o repositório:**

	```bash
	git clone https://github.com/LucaoCorrea/spring-backend-leila.git
	cd spring-backend-leila
2.  **Configure o `application.properties`:**
    

Altere o arquivo em `src/main/resources/application.properties` com suas credenciais de banco:
.properties

`spring.datasource.url=jdbc:postgresql://localhost:5432/leila
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha` 

3.  **Crie o banco de dados no PostgreSQL:**

`CREATE DATABASE leila;` 

4.  **Execute o projeto:**

`./mvnw spring-boot:run` 

O backend será iniciado em: `http://localhost:8080`


## 🔐 Acesso à API

O projeto utiliza autenticação JWT. Para acessar os endpoints protegidos:

1.  Faça login via `/auth/login`
    
2.  Guarde o token JWT retornado
    
3.  Envie o token no header `Authorization` como:


`Authorization: Bearer <seu_token>` 

----------

## 📁 Estrutura das Roles

-   `ROLE_USER`: Pode criar e visualizar seus próprios agendamentos
    
-   `ROLE_ADMIN`: Pode visualizar todos os agendamentos, cadastrar serviços, gerar relatórios e editar dados
    

----------

## 🧪 Endpoints principais

-   `POST /auth/register` – Registro de usuários
    
-   `POST /auth/login` – Login e geração de token
    
-   `GET /bookings` – Lista todos os agendamentos (ADMIN)
    
-   `GET /bookings/client/{id}` – Lista os agendamentos do cliente
    
-   `POST /bookings` – Criação de novo agendamento
    
-   `PUT /bookings/{id}` – Edição (apenas se > 48h)
    
-   `DELETE /bookings/{id}` – Cancelamento (ADMIN)
    
-   `GET /services` – Lista de serviços
    
-   `POST /services` – Cadastro de novo serviço (ADMIN)
    

----------

## 🎯 Sobre o desafio

Este projeto foi desenvolvido com base na **Avaliação Técnica DSIN - Teste Prático para Desenvolvedores**, com o objetivo de demonstrar domínio prático sobre:

-   Desenvolvimento backend com Spring Boot
    
-   Regras de negócio e manipulação de entidades relacionais
    
-   Segurança com autenticação/autorização via JWT
    
-   Integração com banco de dados relacional (PostgreSQL)
    
-   Princípios RESTful


## ✨ Autor

**Lucas Corrêa**  
[GitHub](https://github.com/LucaoCorrea) · Backend Developer Java & C# 
# Digital Wallet System
Full-stack digital wallet application built with Spring Boot, React, PostgreSQL and Docker.
<img width="1722" height="915" alt="Screenshot 2026-06-23 at 01 18 42" src="https://github.com/user-attachments/assets/2956f1ee-86f4-498b-bcf3-7457d4214d12" />

## About

This project is a simple digital wallet system for managing users, balances and wallet transactions.

The backend is built with Spring Boot and exposes a REST API. The frontend is built with React and consumes the API using authenticated requests.

I added user registration, login with JWT, protected wallet routes, deposits, transfers, transaction history, request validation and service tests for the main wallet rules.

## Stack

Java 21, Spring Boot, Spring Security, JWT, PostgreSQL, React, Vite, Docker, JUnit and Mockito.

![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-green)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-brightgreen)
![React](https://img.shields.io/badge/React-Vite-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![JUnit](https://img.shields.io/badge/JUnit-Tests-green)

## Main Features

User registration, login, JWT authentication, protected wallet routes, personal wallet balance, deposits, transfers between wallets, transaction history, request validation, centralized error handling and Swagger documentation.

## Project Structure

backend: Spring Boot REST API  
frontend: React app  
docker-compose.yml: Local services  
README.md: Project documentation  

## API Endpoints

Auth:

```text
POST /api/auth/register
POST /api/auth/login
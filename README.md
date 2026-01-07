# Book-Social-Network-project

## Overview

Book Social Network lets users create accounts, manage book collections, and interact with a community of book lovers. Key functionalities include adding books, sharing books, borrowing and returning, and user interactions. The backend is powered by Spring Boot (Java), while the frontend is built with Angular.

## Features

- User registration & authentication
- Book management (add / update / delete)
- Sharing books with others
- Borrowing & returning books
- File uploads 
- Frontend UI with Angular
- Backend REST API with Spring Boot

## Tech Stack

### Backend
- Java
- Spring Boot  
  (Spring Web, Spring Data JPA, Spring Security, Hibernate)
- Postgres 

### Frontend
- Angular/TypeScript
- HTML / CSS ,Bootstrap

### DevOps
- docker compose for mailserver  and DB
---

## 🚀 Project Setup

### Prerequisites

- Git
- Docker
- Docker Compose

---
1️⃣ Clone the Repository
```bash
git clone https://github.com/fasih6/Book-Social-Network-project.git
```
2️⃣ Build Backend Docker Image
Navigate to the backend directory:
```bash
cd book-network
```
Build the backend image:
```bash
docker build -t bsn/bsn:1.0.0 -f ../docker/backend/Dockerfile .
```
3️⃣ Build Frontend Docker Image
Navigate to the frontend directory:
```bash
cd ../book-network-ui
```
Build the frontend image:
```bash
docker build -t bsn/bsn-ui:1.0.0 -f ../docker/frontend/Dockerfile .
```
4️⃣ Run the Application
Go back to main folder and start all services using Docker Compose:
```bash
docker-compose up -d
```
🌐 Access the Application

Open your browser and go to:
```
http://localhost:8080/login
```

# 📘 Fórum Hub API

A **Fórum Hub API** é uma aplicação backend desenvolvida em Java com Spring Boot, que simula a estrutura de um fórum real. Permite o gerenciamento completo de usuários, tópicos e respostas, com autenticação segura via JWT, controle de acesso com Spring Security, documentação via Swagger e versionamento do banco com Flyway.

Este projeto foi desenvolvido como parte do programa **Oracle Next Education (ONE)** em parceria com a **Alura**, na **Turma G8**.

---

## 🚀 Tecnologias e Dependências

- Java 17
- Spring Boot 3
- Spring Web
- Spring Boot DevTools
- Spring Data JPA
- Spring Security
- Lombok
- Flyway Migration
- Validation API
- MySQL Driver
- JWT (JSON Web Token)
- SpringDoc OpenAPI + Swagger

---

## 🛠️ Funcionalidades

- 🔐 **Autenticação segura** com Spring Security + JWT
- 👤 CRUD completo de **usuários**
  - Atualização de nome com confirmação de senha
  - Atualização de senha
  - Exclusão do próprio perfil com confirmação de senha
- 💬 CRUD de **tópicos** vinculados ao autor autenticado
  - Status automático: `NAO_RESPONDIDO` ou `RESPONDIDO`
- 💡 CRUD de **respostas**
  - Apenas o autor pode atualizar ou deletar
  - Respostas de um tópico numeradas localmente
- 📈 Relatório: contagem de tópicos e respostas por usuário
- ✅ Testes de endpoints com **Insomnia**
- 🧪 Documentação interativa com **Swagger UI**
- 🗃️ Versionamento de banco com **Flyway**

---

🧪 Testes com Insomnia
Todos os endpoints foram testados utilizando o Insomnia. Você pode importar a collection manualmente ou testar via Swagger.

🧑‍💻 Autor
Davi Moraes

💼 Desenvolvedor Java | Backend

🎓 Aluno do programa Oracle Next Education (ONE) – Turma G8

📫 Meu perfil no LinkedIn: https://www.linkedin.com/in/davi-moraes-dev/

🧠 Aprendizados
Este projeto me proporcionou uma experiência prática profunda com:

Estruturação profissional em MVC

Segurança com JWT

Manipulação de entidades relacionais no JPA

Versionamento de banco com Flyway

Boas práticas REST

Criação de endpoints protegidos e públicos

🗂️ Organização do Projeto

src/
 └── main/
     ├── java/
     │   └── forum.hub.api/
     │       ├── controller
     │       ├── domain (usuarios, topicos, respostas)
     │       ├── infra (segurança, token, exceções)
     │       └── ...
     └── resources/
         ├── application.properties
         └── db/migration

| [<img loading="lazy" src="https://avatars.githubusercontent.com/u/195828744?v=4" width=115><br><sub>Davi Moraes</sub>](https://github.com/WiiinchesteR)
| :---: |



# Projeto de gerenciamento de pedidos

Este projeto utiliza Docker Compose para gerenciar o serviço de banco de dados (Postgres 17) e um cache Redis 7.
O objetivo é facilitar o setup do ambiente de desenvolvimento, garantindo que todos os serviços estejam configurados de forma consistente.

---

## Estrutura do projeto

```plainttext
order_control/
|-- backend/            #Código backend (Java 17 Spring Boot)
|-- postgres/data       #Dados persistidos do Postgres
```

---

## Pré-requisitos

Certifique-se de que você tenha instalado:
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Java 17](https://adoptium.net/?variant=openjdk17)

Verifique se o Docker está funcionando corretamente:

```bash
docker --version
docker-compose --version
```

Verifique se o Java está instalado corretamente:

```bash
java -version
```
---

## Configuração Inicial

1. **Clone este repositório**:

   ```bash
   git clone https://github.com/vitorcastilho/order-control
   cd order-control
   ```

2. **Certifique-se de que o diretório de dados do Postgres existe**:

   ```bash
   mkdir -p ~/development/src/postgres/data
   ```

---

## Subindo os Containers

1. **Execute o Docker Compose**:

   No diretório onde está localizado o arquivo `docker-compose.yml`, execute:

   ```bash
   docker-compose up -d
   ```

   - O parâmetro `-d` executa os containers em modo "detached" (em segundo plano).

2. **Verifique se os containers estão rodando**:

   ```bash
   docker ps
   ```

   Você verá uma lista de containers ativos.

---

## Acessando os Serviços

- **Backend**: Acesse [http://localhost:8080](http://localhost:8080)
- **Postgres**:
  - Host: `localhost`
  - Porta: `5432`
  - Usuário: `postgres`
  - Senha: `postgres`
  - Banco de dados: `order-control`
- **Redis**
  - Host: `localhost`
  - Porta: `6379`

---

## Subindo o backend(Java 17 Spring Boot)

1. **Navegue até o diretório** `order-control/`
   ```bash
   cd order-control
   ```

2. **Compile e execute o projeto Spring Boot**:
   Se você estiver utilizando o Maven:
   ```bash
   cmvn spring-boot:run
   ```
   O backend será iniciado e você poderá acessá-lo em http://localhost:8080.

---

Com essas configurações, o ambiente de desenvolvimento estará pronto para uso com Docker, Postgres, Redis e o backend em Java 17 com Spring Boot.

---

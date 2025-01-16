# Projeto de gerenciamento de pedidos

Este projeto utiliza Docker Compose para gerenciar o serviço de banco de dados (Postgres 17).
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

Verifique se o Docker está funcionando corretamente:

```bash
docker --version
docker-compose --version
```

---

## Configuração Inicial

1. **Clone este repositório**:

   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd src
   ```

2. **Certifique-se de que o diretório de dados do Postgres existe**:

   ```bash
   mkdir -p ~/src/postgres/data
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

---
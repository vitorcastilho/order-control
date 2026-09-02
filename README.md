# Order Control

[![CI](https://github.com/vitorcastilho/order-control/actions/workflows/ci.yml/badge.svg)](https://github.com/vitorcastilho/order-control/actions/workflows/ci.yml)
![Cobertura](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)

API REST de gerenciamento de pedidos em Java 17 e Spring Boot, com PostgreSQL, cache em Redis e ambiente completo em Docker Compose.

O domínio é simples de propósito — clientes, produtos e pedidos. O que o projeto exercita é o que costuma dar errado em volta disso: separação de camadas, tratamento consistente de erro, controle de estoque sob concorrência de leitura e uma suíte de testes que sustenta refatoração.

---

## Decisões de projeto

### Camadas e dependências apontando para dentro

```
web/api/v1/controller   → recebe HTTP, não conhece regra de negócio
application/service     → orquestra a regra, depende de interfaces
application/mapper      → traduz DTO ↔ entidade
domain/model            → entidades, sem anotação de framework web
infrastructure/         → repositórios, validadores, configuração, exceções
```

Os services dependem de `ICustomerService`, `IProductService` e `ICustomerOrderService`, não das implementações. Isso existe menos por dogma e mais porque foi o que permitiu testar `CustomerOrderService` sem subir contexto Spring: nos testes, `IProductService` é um mock e a verificação recai sobre o contrato, não sobre a implementação.

### Duas mensagens em cada erro

`ResourceNotFoundException` e `ValidationException` carregam `developerMessage` e `clientMessage`. O `GlobalExceptionHandler` devolve as duas no corpo:

```json
{
  "status": 400,
  "developerMessage": "The reported status does not exist: FINALIZADO",
  "clientMessage": "O status informado não existe."
}
```

A separação é deliberada: quem depura precisa do id que falhou e do valor recebido; quem usa o sistema não deveria ver isso. Sem os dois campos, a tendência é vazar detalhe interno para a tela ou empobrecer o log.

### Cache no pedido, não na listagem inteira

`getCustomerOrderById` usa `@Cacheable`, `updateOrderStatus` usa `@CacheEvict` e a listagem popula o cache item a item com `@CachePut`. O TTL é de 10 minutos, definido em `CacheConfig`.

A escolha é por granularidade: cachear a página inteira invalidaria tudo a cada alteração de um único pedido. Cacheando por id, a alteração de um pedido derruba apenas a entrada dele.

O serializador é `GenericJackson2JsonRedisSerializer` em vez da serialização Java padrão — o conteúdo do Redis fica legível e não quebra quando a classe muda de pacote.

### Estoque com baixa validada

`ProductService.updateQuantityOfProduct` recebe um delta com sinal: positivo entra no estoque, negativo sai. A baixa só ocorre se houver saldo, e a recusa é uma `ValidationException`, não um estado silenciosamente inconsistente.

Zerar o estoque é permitido; ficar negativo, não. Os testes cobrem essa fronteira explicitamente.

### Versionamento por prefixo de rota

`ApiVersionConfig` prefixa com `api/v1` apenas as classes do pacote `web.api.v1.controller`, em vez de repetir o prefixo em cada `@RequestMapping`. Uma futura `v2` convive com a `v1` mudando só o pacote.

### Configuração externalizada, com default

Banco e Redis são configurados por variável de ambiente com valor padrão:

```yaml
url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:order_control}
username: ${DB_USER:postgres}
password: ${DB_PASSWORD:postgres}
```

O `.env.example` documenta as variáveis disponíveis; o `.env` real fica fora do versionamento. Com os defaults, a aplicação sobe sem nenhuma configuração — e o mesmo conjunto de variáveis alimenta o `docker-compose`.

**Onde essa escolha para de servir:** `.env` documenta quais variáveis existem, mas não resolve gestão de segredo. Em produção o certo é um gerenciador dedicado — Vault, AWS Secrets Manager, Docker secrets ou injeção pela própria plataforma. Aqui a decisão é deliberada pelo escopo: ambiente local, credencial de desenvolvimento.

### Migrations versionadas

O schema é gerenciado por Flyway (`db/migration`), com `ddl-auto: none`. O Hibernate não altera schema — o banco só muda por migration revisada. O teste de contexto sobe contra um Postgres real no CI, então uma migration quebrada falha o build.

---

## Testes

**131 testes.** Cobertura de instruções acima de 99%, de branches acima de 97%.

A suíte é organizada por custo de execução:

| Tipo | Ferramenta | O que cobre |
|---|---|---|
| Unitário | JUnit + Mockito | services, mapper, validadores, utilitários |
| Camada web | `@WebMvcTest` | status HTTP e corpo da resposta, com service mockado |
| Contexto | `@SpringBootTest` | a aplicação sobe contra Postgres e Redis reais e roda as migrations |

Os testes de service cobrem os caminhos de erro, não só o caminho feliz: recurso inexistente, estoque insuficiente, status inválido, status vazio. A baixa de estoque é verificada com `ArgumentCaptor`, para conferir o valor efetivamente persistido, e com `never()`, para garantir que nada é gravado quando a validação recusa — um teste que só verificasse a exceção passaria mesmo se a gravação acontecesse antes dela.

**A suíte encontrou três defeitos ao ser escrita**, todos corrigidos no PR #19: uma guarda de validação que era código morto e fazia a API responder 500 no lugar de 400; uma inconsistência em que o método normalizava a caixa ao gravar mas recusava minúsculas na validação; e um handler genérico que capturava as exceções de roteamento do Spring, fazendo rota inexistente responder 500 em vez de 404.

O pipeline também revelou, na primeira execução, um teste de contexto que nunca havia rodado — estava no pacote `com.order_control` enquanto a aplicação está em `com.ordercontrol`.

---

## Como rodar

**Pré-requisitos:** Docker, Docker Compose e Java 17.

```bash
git clone https://github.com/vitorcastilho/order-control
cd order-control

# opcional: ajuste as variáveis (há default para todas)
cp .env.example .env

# sobe Postgres 17 e Redis 7
docker compose up -d

# sobe a aplicação
cd order-control
mvn spring-boot:run
```

A API fica em `http://localhost:8080/api/v1`.

**Documentação interativa:** `http://localhost:8080/swagger-ui.html`
**Especificação OpenAPI:** `http://localhost:8080/v3/api-docs`

**Rodar os testes:**

```bash
cd order-control
mvn verify
```

O relatório de cobertura é gerado em `target/site/jacoco/index.html`. O teste de contexto exige Postgres e Redis no ar; para rodar só os unitários:

```bash
mvn verify -Dtest='!OrderControlApplicationTests'
```

---

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/v1/customers` | lista clientes, paginado |
| `GET` | `/api/v1/customers/{id}` | busca cliente por id |
| `POST` | `/api/v1/customers` | cria cliente |
| `GET` | `/api/v1/products` | lista produtos, paginado |
| `GET` | `/api/v1/products/{id}` | busca produto por id |
| `POST` | `/api/v1/products` | cria produto |
| `GET` | `/api/v1/customer-orders` | lista pedidos, paginado |
| `GET` | `/api/v1/customer-orders/{id}` | busca pedido por id |
| `GET` | `/api/v1/customer-orders/status/{status}` | filtra por status |
| `POST` | `/api/v1/customer-orders` | cria pedido e dá baixa no estoque |
| `PATCH` | `/api/v1/customer-orders/{id}/status` | altera o status do pedido |

Todos os endpoints estão documentados no Swagger UI, com os códigos de resposta de cada operação. A paginação usa `offset`, `limit` e `sortBy` como parâmetros de consulta. Os status possíveis são `PENDING`, `PROCESSING`, `COMPLETED` e `CANCELED`, aceitos em qualquer caixa.

---

## Stack

Java 17 · Spring Boot 3.4 · Spring Data JPA · Flyway · PostgreSQL 17 · Redis 7 · springdoc-openapi · JUnit 5 · Mockito · JaCoCo · Docker Compose · GitHub Actions

---

## Licença

[MIT](LICENSE)

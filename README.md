# API de Pedidos

API REST desenvolvida em Spring Boot para gerenciamento de pedidos de entrega de pequenas empresas. Projeto construído como portfólio, com foco em boas práticas de arquitetura, modelagem de domínio e tratamento de erros.

## Sobre o projeto

O sistema permite cadastrar clientes, produtos e pedidos, onde cada pedido pode conter múltiplos itens (produtos + quantidade). O valor de cada item e o total do pedido são calculados automaticamente pelo backend a partir do preço vigente do produto no momento da compra — o cliente da API nunca informa valores monetários diretamente, evitando manipulação de preços.

## Tecnologias

- Java 17
- Spring Boot 3
- Spring Data JPA / Hibernate
- MySQL
- Bean Validation
- Maven
- Lombok
- SpringDoc OpenAPI (Swagger)

## Arquitetura

O projeto segue a organização em camadas (**package by layer**), separando responsabilidades em:

```
com.devgabriel.pedidos
├── controller     → camada de entrada HTTP, validação de request
├── service        → regras de negócio
├── mapper         → conversão entre Entity e DTO
├── repository     → acesso a dados (Spring Data JPA)
├── domain         → entidades JPA
├── dto            → contratos de entrada e saída da API
├── enums          → enumerações de domínio (ex: StatusPedido)
└── exception      → exceções customizadas e tratamento global de erros
```

### Decisões de modelagem

- **`Pedido` e `Produto` nunca se relacionam diretamente.** A relação entre eles é resolvida por `ItemPedido`, uma entidade associativa que carrega dados próprios da relação (quantidade e preço praticado no momento da compra), impedindo que o valor de um pedido já fechado mude se o preço do produto mudar depois.
- **`ItemPedido` não possui Controller nem Service próprios.** Ele é tratado como parte do agregado `Pedido` — nunca é criado, listado ou removido isoladamente pela API, apenas através das operações do recurso `Pedido`.
- **DTOs de entrada nunca aceitam valores monetários ou datas de criação.** Preço unitário vem sempre do produto buscado no banco; `dataPedido` é gerada automaticamente via `@CreationTimestamp`; `status` inicial é sempre definido pelo backend.
- **Relacionamentos `@ManyToOne` configurados como `LAZY`**, evitando carregamento desnecessário e problemas de N+1 query.
- **Cascade e orphan removal** configurados entre `Pedido` e `ItemPedido`, refletindo que os itens não têm ciclo de vida próprio fora de um pedido.
- **Soft delete em Pedido.** O endpoint de exclusão não remove o registro do banco — apenas altera o status para `CANCELADO`, preservando histórico para fins de auditoria e relatórios.

## Tratamento de erros

Erros são centralizados em um `@RestControllerAdvice`, retornando um corpo padronizado:

```json
{
  "status": 404,
  "mensagem": "Cliente não encontrado com id: 99",
  "timestamp": "2026-07-02T15:41:04.154"
}
```

| Situação | Status HTTP |
|---|---|
| Dado de entrada inválido (Bean Validation) | 400 |
| Recurso não encontrado | 404 |
| Conflito de regra de negócio (ex: e-mail duplicado, cancelar pedido já entregue) | 409 |

## Principais endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/v1/clientes` | Cadastra cliente |
| GET | `/v1/clientes` | Lista clientes (paginado) |
| GET | `/v1/clientes/{id}` | Busca cliente por id |
| PUT | `/v1/clientes/{id}` | Atualiza cliente |
| DELETE | `/v1/clientes/{id}` | Remove cliente |
| POST | `/v1/produtos` | Cadastra produto |
| GET | `/v1/produtos` | Lista produtos (paginado) |
| GET | `/v1/produtos/{id}` | Busca produto por id |
| PUT | `/v1/produtos/{id}` | Atualiza produto |
| DELETE | `/v1/produtos/{id}` | Remove produto |
| POST | `/v1/pedidos` | Cadastra pedido com itens |
| GET | `/v1/pedidos` | Lista pedidos (paginado) |
| GET | `/v1/pedidos/{id}` | Busca pedido por id |
| PATCH | `/v1/pedidos/{id}/status` | Atualiza status do pedido |
| DELETE | `/v1/pedidos/{id}` | Cancela pedido (soft delete) |

Documentação interativa completa disponível via Swagger em `/swagger-ui.html` com o projeto em execução.

### Exemplo — criar pedido

```http
POST /v1/pedidos
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    { "produtoId": 1, "quantidade": 2 },
    { "produtoId": 2, "quantidade": 1 }
  ]
}
```

```json
{
  "id": 1,
  "cliente": { "id": 1, "nome": "João Silva" },
  "itens": [
    { "id": 1, "produtoId": 1, "nomeProduto": "Pizza Grande", "quantidade": 2, "valorUnitario": 45.00, "subtotal": 90.00 },
    { "id": 2, "produtoId": 2, "nomeProduto": "Refrigerante", "quantidade": 1, "valorUnitario": 8.00, "subtotal": 8.00 }
  ],
  "dataPedido": "2026-07-02",
  "status": "PENDENTE",
  "valorTotal": 98.00
}
```

## Como rodar o projeto

### Pré-requisitos

- Java 17+
- Maven
- MySQL

### Passos

1. Clone o repositório:
```bash
git clone https://github.com/rosniaksync/api-pedidos.git
cd api-pedidos
```

2. Crie um banco de dados MySQL para o projeto.

3. Configure as variáveis de ambiente com base no arquivo `.env.example`:
```
DB_URL=jdbc:mysql://localhost:3306/nome_do_banco
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

Essas variáveis podem ser definidas como variáveis de ambiente do sistema ou configuradas diretamente nas Run Configurations da sua IDE.

4. Rode a aplicação:
```bash
./mvnw spring-boot:run
```

5. A API estará disponível em `http://localhost:8080`, e a documentação Swagger em `http://localhost:8080/swagger-ui.html`.

## Autor

Desenvolvido por [rosniaksync](https://github.com/rosniaksync) como projeto de portfólio para estudo prático de Spring Boot, JPA/Hibernate e boas práticas de API REST.

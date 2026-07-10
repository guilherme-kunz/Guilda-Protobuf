# Guilda Protobuf — Backend

Mock server criado para o workshop **Protobuf vs JSON/REST**. Demonstra como o mesmo contrato `.proto` pode ser servido tanto em formato binário (Protobuf) quanto em JSON a partir de um único endpoint.

---

## Tecnologias

| Dependência | Versão | Papel |
|---|---|---|
| Node.js | ≥ 18 | Runtime |
| Express | ^4.19 | HTTP server |
| protobufjs | ^7.4 | Codificação/decodificação Protobuf em runtime |
| nodemon | ^3.1 _(dev)_ | Hot-reload durante desenvolvimento |

---

## Pré-requisitos

- **Node.js 18+** — [nodejs.org](https://nodejs.org)
- **npm** (incluído com o Node.js)

---

## Instalação e execução

```bash
# Instalar dependências
npm install

# Modo produção
npm start

# Modo desenvolvimento (hot-reload)
npm run dev
```

O servidor sobe em **http://localhost:3000**.

> **Emulador Android:** use `http://10.0.2.2:3000` para acessar o host da máquina a partir do emulador.

---

## Contrato Protobuf

O arquivo `.proto` compartilhado fica em `proto/user.proto` (raiz do repositório).  
O servidor carrega esse contrato em runtime via `protobufjs`.

```
proto/
└── user.proto   ← contrato único, compartilhado com o app Android
```

Mensagens definidas:

| Mensagem | Campos |
|---|---|
| `User` | `id`, `name`, `email`, `roles[]` |
| `UserList` | `users[]` |
| `CreateUserRequest` | `name`, `email`, `roles[]` |
| `Role` _(enum)_ | `UNSPECIFIED`, `ADMIN`, `EDITOR`, `VIEWER` |

---

## Endpoints

O servidor é **dual-mode**: responde em Protobuf ou JSON de acordo com o header `Accept` da requisição.

| Header `Accept` | Formato da resposta |
|---|---|
| `application/x-protobuf` | Binário Protobuf |
| `application/json` ou ausente | JSON legível |

---

### `GET /api/v1/users`

Retorna a lista de todos os usuários.

**Resposta Protobuf** (`Accept: application/x-protobuf`)
```
Content-Type: application/x-protobuf
Body: UserList (binário)
```

**Resposta JSON**
```json
{
  "users": [
    { "id": "1", "name": "Alice Silva", "email": "alice@guilda.com", "roles": [1, 3] }
  ]
}
```

**cURL de exemplo:**
```bash
# JSON
curl http://localhost:3000/api/v1/users

# Protobuf (inspecionar bytes)
curl -H "Accept: application/x-protobuf" http://localhost:3000/api/v1/users | xxd | head
```

---

### `POST /api/v1/users`

Cria um novo usuário. O body deve ser um `CreateUserRequest` codificado em Protobuf (`Content-Type: application/x-protobuf`).

**Request**
```
Content-Type: application/x-protobuf
Body: CreateUserRequest (binário)
```

**Resposta de sucesso** — `201 Created`
```
Content-Type: application/x-protobuf
Body: User (binário com o usuário criado)
```

---

### `DELETE /api/v1/users/:id`

Remove o usuário pelo `id`. Retorna o objeto deletado.

**Resposta de sucesso** — `200 OK`
```
Content-Type: application/x-protobuf
Body: User (binário do usuário removido)
```

**Resposta de erro** — `404 Not Found`
```json
{ "error": "User not found" }
```

**cURL de exemplo:**
```bash
curl -X DELETE -H "Accept: application/x-protobuf" http://localhost:3000/api/v1/users/1
```

---

## Estrutura do projeto

```
backend/
├── proto/
│   └── user.proto       # Contrato Protobuf compartilhado
├── server.js            # Entry point — Express + handlers
├── package.json
└── package-lock.json
```

> `node_modules/` não é versionado. Execute `npm install` após clonar.

---

## Dados mock

O servidor inicia com 4 usuários em memória. As alterações feitas via POST/DELETE persistem apenas enquanto o processo estiver rodando — ao reiniciar, os dados voltam ao estado inicial.

```
ID 1 — Alice Silva    | alice@guilda.com  | ADMIN, VIEWER
ID 2 — Bob Santos     | bob@guilda.com    | EDITOR, VIEWER
ID 3 — Carol Martins  | carol@guilda.com  | ADMIN, EDITOR, VIEWER
ID 4 — Diego Ramos    | diego@guilda.com  | VIEWER
```

---

## Logs do servidor

Cada requisição é logada no terminal com o formato e o tamanho do payload:

```
[PROTOBUF] GET  /api/v1/users → 87 bytes
[JSON]     GET  /api/v1/users → 312 bytes
[CREATE]   POST /api/v1/users → id=5 name="Novo Usuário"
[DELETE]   DELETE /api/v1/users/3 → name="Carol Martins"
```

Isso permite comparar em tempo real o ganho de compressão do Protobuf frente ao JSON.

# Guilda Protobuf

Projeto de workshop interno da Guilda Android demonstrando o uso de **Protocol Buffers** como alternativa ao JSON/REST.

## Repositórios

| Pasta | Descrição |
|---|---|
| [`backend/`](./backend) | Mock server Node.js + Express — serve a API em Protobuf e JSON |
| [`android/`](./android) | App Android — Kotlin, Jetpack Compose, Hilt, Retrofit + Protobuf |

O contrato compartilhado entre os dois projetos fica em [`proto/user.proto`](./proto/user.proto).

## Como rodar

```bash
# 1. Subir o backend
cd backend
npm install && npm start

# 2. Abrir android/ no Android Studio e executar no emulador
```

> Documentação detalhada de cada projeto no `README.md` de cada pasta.

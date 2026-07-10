# Guilda Protobuf — Android App

Aplicativo Android criado para o workshop **Protobuf vs JSON/REST**. Demonstra o uso de Protocol Buffers como formato de serialização em substituição ao JSON, integrando com um mock server Node.js.

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin 2.1 |
| UI | Jetpack Compose + Material 3 |
| Navegação | Navigation Compose 2.8 |
| DI | Hilt 2.53 |
| HTTP | Retrofit 2.11 + OkHttp 4.12 |
| Serialização | Protocol Buffers (protobuf-javalite 4.28) |
| Async | Coroutines + StateFlow |
| Build | Gradle 9.3 + AGP 8.7 + KSP 2.1 |

---

## Pré-requisitos

- **Android Studio Ladybug** (ou superior)
- **JDK 17**
- **Android SDK** com API 26+ instalada
- **Backend rodando** em `http://localhost:3000` (ver repositório `guilda-protobuf-backend`)

---

## Como executar

1. Clone o repositório e abra a pasta `android/` no Android Studio
2. Aguarde o Gradle sync terminar (o plugin Protobuf gera as classes automaticamente)
3. Inicie o backend Node.js na máquina host
4. Execute o app em um **emulador** (o endereço `10.0.2.2:3000` é o alias do host no emulador Android)

> Para rodar em **dispositivo físico**, altere `BASE_URL` em `di/NetworkModule.kt` para o IP local da máquina na rede Wi-Fi.

---

## Contrato Protobuf

O arquivo de contrato fica em:

```
app/src/main/proto/user.proto
```

O plugin `com.google.protobuf` (versão 0.9.4) processa esse arquivo durante o build e gera automaticamente:

- **Classes Java Lite** — `User`, `UserList`, `CreateUserRequest`, `Role`
- **Extensões Kotlin** — DSL builders como `createUserRequest { }`, acesso a `this.roles += ...`

Nenhuma geração manual de código é necessária.

---

## Arquitetura

O app segue o padrão **MVVM** com separação em camadas:

```
ui/
├── AppNavGraph.kt          # NavHost com as rotas da aplicação
├── UserListScreen.kt       # Lista de usuários + botão de criar/deletar
├── UserViewModel.kt        # Estado da lista (Loading / Success / Error)
├── CreateUserScreen.kt     # Formulário de criação
├── CreateUserViewModel.kt  # Estado do formulário
└── theme/
    ├── Color.kt            # Paleta Material 3 (dark + light)
    └── Theme.kt            # GuildaProtobufTheme (auto dark mode)

data/
├── remote/
│   └── UserApi.kt          # Interface Retrofit
├── repository/
│   ├── UserRepository.kt
│   └── UserRepositoryImpl.kt

di/
└── NetworkModule.kt        # Hilt — provê OkHttpClient, Retrofit, UserApi
```

### Fluxo de dados

```
Screen → ViewModel → Repository → UserApi (Retrofit + ProtoConverterFactory) → Backend
                                                                                    ↓
Screen ← StateFlow  ← Result<T>  ←──────────── Protobuf desserializado ←───────────
```

---

## Funcionalidades

| Feature | Descrição |
|---|---|
| Listar usuários | Busca via `GET /api/v1/users`, exibe nome, e-mail e roles |
| Criar usuário | Formulário com nome, e-mail e seleção de roles via `FilterChip` |
| Deletar usuário | Ícone de lixeira em cada card, confirmação imediata |
| Auto-refresh | A lista é recarregada automaticamente ao voltar da tela de criação |
| Tema escuro | Alternância automática baseada na configuração do sistema (Material 3) |

---

## Dependências principais

```toml
# gradle/libs.versions.toml

[versions]
agp              = "8.7.3"
kotlin           = "2.1.0"
ksp              = "2.1.0-1.0.29"
compose-bom      = "2024.12.01"
hilt             = "2.53.1"
retrofit         = "2.11.0"
protobuf         = "4.28.3"
protobuf-plugin  = "0.9.4"
navigation       = "2.8.5"
```

> `converter-protobuf` (Retrofit) tem `protobuf-java` como dependência transitiva — ela é excluída explicitamente no `build.gradle.kts` para evitar conflito com `protobuf-javalite`.

---

## Estrutura de arquivos relevantes

```
android/
├── app/
│   ├── build.gradle.kts                  # Config do módulo + bloco protobuf { }
│   └── src/main/
│       ├── proto/
│       │   └── user.proto                # Contrato Protobuf
│       ├── java/com/guilda/protobuf/
│       │   ├── GuildaApp.kt              # @HiltAndroidApp
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   ├── di/
│       │   └── ui/
│       └── res/
│           ├── values/themes.xml
│           └── values-night/themes.xml   # Tema escuro para barra de status
├── gradle/
│   └── libs.versions.toml               # Version catalog
└── build.gradle.kts                     # Config raiz
```

---

## Observações técnicas

**Por que `protobuf-javalite` e não `protobuf-java`?**  
A variante `javalite` gera classes menores, sem reflection e sem dependência de `protobuf-java`, sendo a recomendada para Android.

**Por que a versão do `protoc` é hardcoded no `build.gradle.kts`?**  
O plugin Protobuf registra um objeto `libs` próprio na `ExtensionContainer` do Gradle, eclipsando o accessor do version catalog dentro do bloco `protobuf { }`. A solução é usar a string literal `"4.28.3"` diretamente.

**Como o `ProtoConverterFactory` funciona?**  
`converter-protobuf` inspeciona o tipo de retorno da função Retrofit. Se for `MessageLite` (implementado por todas as classes geradas pelo plugin), serializa o body como `application/x-protobuf` e desserializa a resposta binária de volta para o objeto Kotlin.

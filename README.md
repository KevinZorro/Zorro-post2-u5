## Capturas de pantalla



# biblioteca-api — Post-Contenido 2

API REST completa construida con **Spring Boot 3.2**, **Spring Data JPA**,
**DTOs**, **manejo global de errores** con `@RestControllerAdvice` y
**documentación interactiva** con SpringDoc/Swagger UI.

Extiende el Post-Contenido 1 con las siguientes capas adicionales:
`dto/`, `mapper/`, `exception/` y un nuevo controlador versionado `v2`.

---

## Arquitectura
HTTP Request
│
▼
LibroControllerV2 ← recibe LibroRequestDTO, retorna LibroResponseDTO
│ nunca expone entidades JPA directamente
▼
LibroMapper ← convierte entre Libro (Entity) ↔ DTOs
│
▼
LibroService ← lógica de negocio (validación ISBN, búsquedas)
│
▼
LibroRepository ← Spring Data JPA genera la implementación en runtime
│
▼
H2 Database ← tabla "libros" creada automáticamente por Hibernate

⬆
GlobalExceptionHandler ← intercepta TODAS las excepciones del sistema
(@RestControllerAdvice) convierte NoSuchElementException → 404
convierte IllegalArgumentException → 400
convierte MethodArgumentNotValidException → 400
---

## Estructura de Paquetes
src/main/java/com/universidad/patrones/
├── BibliotecaApiApplication.java
├── SecurityConfig.java
├── model/
│ └── Libro.java
├── repository/
│ └── LibroRepository.java
├── service/
│ └── LibroService.java
├── dto/
│ ├── LibroRequestDTO.java ← datos de ENTRADA (cliente → API)
│ └── LibroResponseDTO.java ← datos de SALIDA (API → cliente)
├── mapper/
│ └── LibroMapper.java ← conversión Entity ↔ DTO
├── exception/
│ └── GlobalExceptionHandler.java
└── controller/
├── LibroController.java ← v1: expone /api/libros (Post-1)
└── LibroControllerV2.java ← v2: expone /api/v2/libros con DTOs
---

## Requisitos

- Java 17+
- Maven 3.8+

---

## Ejecución

```bash
git clone https://github.com/Kevinzorro/Zorro-post2-u5.git
cd Zorro-post2-u5
mvn clean package
mvn spring-boot:run
```

La aplicación levanta en **http://localhost:8080**

---

## URLs Disponibles

| Recurso | URL |
|---|---|
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html |
| API v2 (con DTOs) | http://localhost:8080/api/v2/libros |
| API v1 (Post-1) | http://localhost:8080/api/libros |
| Consola H2 | http://localhost:8080/h2-console |
| JSON OpenAPI | http://localhost:8080/v3/api-docs |

> H2 Console: JDBC URL `jdbc:h2:mem:biblioteca_db`, usuario `sa`, sin contraseña.

---

## Endpoints REST v2 — `/api/v2/libros`

| Método | URL | Descripción | Código OK | Código Error |
|---|---|---|---|---|
| GET | `/api/v2/libros` | Lista todos los libros | 200 | — |
| GET | `/api/v2/libros/{id}` | Obtiene un libro por ID | 200 | 404 |
| POST | `/api/v2/libros` | Crea un nuevo libro | 201 | 400 |
| DELETE | `/api/v2/libros/{id}` | Elimina un libro | 204 | 404 |

---

## Ejemplos de Uso

### Crear un libro válido → `201 Created`

```bash
curl -X POST http://localhost:8080/api/v2/libros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Refactoring",
    "autor": "Martin Fowler",
    "isbn": "978-0201485677",
    "anioPublicacion": 1999,
    "categoria": "Ingenieria de Software"
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "titulo": "Refactoring",
  "autor": "Martin Fowler",
  "isbn": "978-0201485677",
  "anioPublicacion": 1999,
  "categoria": "Ingenieria de Software"
}
```

---

### ISBN duplicado → `400 Bad Request`

```bash
curl -X POST http://localhost:8080/api/v2/libros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Otro Libro",
    "autor": "Otro Autor",
    "isbn": "978-0201485677",
    "anioPublicacion": 2020
  }'
```

**Respuesta:**
```json
{
  "error": "Ya existe un libro con ISBN: 978-0201485677"
}
```

---

### ID inexistente → `404 Not Found`

```bash
curl http://localhost:8080/api/v2/libros/999
```

**Respuesta:**
```json
{
  "error": "Libro no encontrado: 999"
}
```

---

### Título vacío → `400 Bad Request` (validación `@Valid`)

```bash
curl -X POST http://localhost:8080/api/v2/libros \
  -H "Content-Type: application/json" \
  -d '{"titulo": "", "autor": "X", "isbn": "123"}'
```

**Respuesta:**
```json
{
  "errores": [
    "titulo: El título es obligatorio"
  ]
}
```

---

## Patrones Aplicados

### DTO Pattern
`LibroRequestDTO` define los datos que acepta la API (entrada).
`LibroResponseDTO` define los datos que expone la API (salida).
La entidad `Libro` nunca sale directamente al cliente, lo que permite
cambiar el modelo de dominio sin romper el contrato de la API.

### Mapper
`LibroMapper` centraliza la conversión entre entidades y DTOs.
Si la lógica de mapeo cambia, solo se modifica esta clase.

### Global Exception Handler
`GlobalExceptionHandler` con `@RestControllerAdvice` intercepta
todas las excepciones del sistema y las convierte en respuestas HTTP
semánticamente correctas:

| Excepción | Código HTTP | Uso |
|---|---|---|
| `NoSuchElementException` | 404 | Recurso no encontrado |
| `IllegalArgumentException` | 400 | Regla de negocio violada (ISBN duplicado) |
| `MethodArgumentNotValidException` | 400 | Validación `@Valid` fallida |

---

## Pruebas

```bash
mvn test
```

---

## Capturas de Pantalla

> Ver carpeta `/docs` del repositorio para evidencia visual de:
> - Swagger UI listando los endpoints de `/api/v2/libros`
> - POST exitoso retornando `201`
> - POST con ISBN duplicado retornando `400`
> - GET con ID inexistente retornando `404`
> - POST con título vacío retornando `400` con lista de errores

---

## Dependencias Clave

| Dependencia | Versión | Uso |
|---|---|---|
| `spring-boot-starter-web` | 3.2.5 | Controladores REST |
| `spring-boot-starter-data-jpa` | 3.2.5 | Patrón Repository |
| `spring-boot-starter-validation` | 3.2.5 | `@Valid`, `@NotBlank` |
| `spring-boot-starter-security` | 3.2.5 | Autenticación |
| `springdoc-openapi-starter-webmvc-ui` | 2.5.0 | Swagger UI |
| `h2` | runtime | Base de datos embebida |
| `lombok` | optional | Reducción de boilerplate |
## Capturas



# biblioteca-api

API REST de gestión de biblioteca construida con **Spring Boot 3.2**, 
**Spring Data JPA** (patrón Repository) y **H2** como base de datos embebida.
Implementa la arquitectura en capas: **Entity → Repository → Service → Controller**.

## Arquitectura
HTTP Request
│
▼
LibroController ← solo maneja HTTP (códigos de estado, ResponseEntity)
│
▼
LibroService ← toda la lógica de negocio (validaciones, unicidad)
│
▼
LibroRepository ← Spring Data JPA genera la implementación en runtime
│
▼
H2 Database ← tabla "libros" creada automáticamente por Hibernate

## Requisitos

- Java 17+
- Maven 3.8+ (o usar `./mvnw` incluido)

## Ejecución

```bash
git clone https://github.com/<KevinZorro>/Zorro-post1-u5.git
cd Zorro-post1-u5
mvn clean package
mvn spring-boot:run
```

La aplicación levanta en **http://localhost:8080**  
Consola H2: **http://localhost:8080/h2-console** (JDBC URL: `jdbc:h2:mem:biblioteca_db`, usuario: `sa`)

## Endpoints REST

| Método | URL                          | Descripción                        | Código |
|--------|------------------------------|------------------------------------|--------|
| GET    | `/api/libros`                | Lista todos los libros             | 200    |
| GET    | `/api/libros/{id}`           | Obtiene un libro por ID            | 200/404|
| POST   | `/api/libros`                | Crea un nuevo libro                | 201    |
| PUT    | `/api/libros/{id}`           | Actualiza un libro existente       | 200/404|
| DELETE | `/api/libros/{id}`           | Elimina un libro                   | 204/404|
| GET    | `/api/libros/buscar?q=texto` | Busca por palabra en el título     | 200    |
| GET    | `/api/libros/autor?nombre=X` | Busca libros por autor             | 200    |

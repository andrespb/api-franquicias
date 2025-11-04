# API de Gestión de Franquicias

API REST desarrollada en Spring Boot para la gestión de franquicias, sucursales y productos.

## 📋 Características

- **Gestión de Franquicias**: Crear y consultar franquicias
- **Gestión de Sucursales**: Agregar sucursales a franquicias
- **Gestión de Productos**: Agregar, eliminar y modificar productos en sucursales
- **Consultas Avanzadas**: Obtener productos con máximo stock por sucursal
- **Caché con Redis**: Optimización de consultas frecuentes
- **Persistencia**: Soporte para H2 (desarrollo) y MySQL (producción)

## 🏗️ Arquitectura

```
src/
├── main/
│   ├── java/com/api/franquicias/
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositorios Spring Data
│   │   ├── service/        # Lógica de negocio
│   │   ├── controller/     # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── config/         # Configuraciones
│   │   └── exception/      # Manejo de excepciones
│   └── resources/
│       ├── application.properties
│       └── application-prod.properties
```

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Data Redis**
- **MySQL** / **H2 Database**
- **Lombok**
- **Maven**

## 📦 Requisitos Previos

- JDK 17 o superior
- Maven 3.6+
- Redis (opcional, para caché)
- MySQL 8.0+ (para producción)

## ⚙️ Configuración

### Base de Datos H2 (Desarrollo)

La aplicación viene configurada con H2 por defecto. No requiere configuración adicional.

### MySQL (Producción)

1. Crear base de datos:

```sql
CREATE DATABASE franquiciasdb;
```

2. Configurar variables de entorno o editar `application-prod.properties`:

```properties
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
```

### Redis (Opcional)

1. Instalar Redis localmente o usar un servicio en la nube
2. Configurar en `application.properties`:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

## 🏃 Ejecutar la Aplicación

### Modo Desarrollo (H2)

```bash
mvn spring-boot:run
```

### Modo Producción (MySQL)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

La aplicación estará disponible en: `http://localhost:8080`

### Consola H2

Acceder a: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:franquiciasdb`
- Usuario: `sa`
- Password: (dejar en blanco)

## 📡 Endpoints de la API

### Franquicias

#### Crear una franquicia

```http
POST /api/franquicias
Content-Type: application/json

{
  "nombre": "Franquicia Ejemplo"
}
```

#### Obtener todas las franquicias

```http
GET /api/franquicias
```

#### Obtener franquicia por ID

```http
GET /api/franquicias/{id}
```

#### Obtener productos con máximo stock por sucursal

```http
GET /api/franquicias/{id}/productos-max-stock
```

### Sucursales

#### Crear una sucursal

```http
POST /api/sucursales
Content-Type: application/json

{
  "nombre": "Sucursal Centro",
  "franquiciaId": 1
}
```

#### Obtener sucursales por franquicia

```http
GET /api/sucursales/franquicia/{franquiciaId}
```

#### Obtener sucursal por ID

```http
GET /api/sucursales/{id}
```

### Productos

#### Crear un producto

```http
POST /api/productos
Content-Type: application/json

{
  "nombre": "Producto A",
  "stock": 100,
  "sucursalId": 1
}
```

#### Eliminar un producto

```http
DELETE /api/productos/{productoId}/sucursal/{sucursalId}
```

#### Actualizar stock de un producto

```http
PATCH /api/productos/{id}/stock
Content-Type: application/json

{
  "nuevoStock": 150
}
```

#### Obtener productos por sucursal

```http
GET /api/productos/sucursal/{sucursalId}
```

#### Obtener producto por ID

```http
GET /api/productos/{id}
```

## 📊 Modelo de Datos

### Franquicia

- `id`: Long (PK)
- `nombre`: String
- `sucursales`: List<Sucursal>

### Sucursal

- `id`: Long (PK)
- `nombre`: String
- `franquicia`: Franquicia (FK)
- `productos`: List<Producto>

### Producto

- `id`: Long (PK)
- `nombre`: String
- `stock`: Integer
- `sucursal`: Sucursal (FK)

## 🧪 Ejemplos de Uso

### Flujo Completo

1. **Crear una franquicia**:

```bash
curl -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Mi Franquicia"}'
```

2. **Agregar una sucursal**:

```bash
curl -X POST http://localhost:8080/api/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Sucursal Norte","franquiciaId":1}'
```

3. **Agregar productos**:

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop","stock":50,"sucursalId":1}'
```

4. **Ver productos con mayor stock por sucursal**:

```bash
curl http://localhost:8080/api/franquicias/1/productos-max-stock
```

## 🔧 Compilar el Proyecto

```bash
# Limpiar y compilar
mvn clean install

# Crear JAR ejecutable
mvn package

# Ejecutar el JAR
java -jar target/franquicias-1.0.0.jar
```

## 🐳 Docker (Opcional)

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/franquicias-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Construir y ejecutar

```bash
mvn clean package
docker build -t franquicias-api .
docker run -p 8080:8080 franquicias-api
```

## 📝 Notas

- El caché de Redis se invalida automáticamente en operaciones de escritura
- Las validaciones están implementadas con Bean Validation
- El manejo de errores está centralizado en `GlobalExceptionHandler`
- Las relaciones bidireccionales están manejadas con `@JsonManagedReference` y `@JsonBackReference` para evitar loops infinitos

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/NuevaCaracteristica`)
3. Commit tus cambios (`git commit -m 'Agregar nueva característica'`)
4. Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. Abre un Pull Request

## 📄 Licencia

ISC License

# 🚀 API de Gestión de Franquicias - Guía Rápida

## ✅ Proyecto Completado

Se ha creado exitosamente una **API REST con Spring Boot** para gestionar franquicias, sucursales y productos.

---

## 📂 Estructura del Proyecto

```
franquicias/
├── src/
│   └── main/
│       ├── java/com/api/franquicias/
│       │   ├── FranquiciasApplication.java     # Clase principal
│       │   ├── model/                          # Entidades JPA
│       │   │   ├── Franquicia.java
│       │   │   ├── Sucursal.java
│       │   │   └── Producto.java
│       │   ├── repository/                     # Repositorios
│       │   │   ├── FranquiciaRepository.java
│       │   │   ├── SucursalRepository.java
│       │   │   └── ProductoRepository.java
│       │   ├── service/                        # Lógica de negocio
│       │   │   ├── FranquiciaService.java
│       │   │   ├── SucursalService.java
│       │   │   └── ProductoService.java
│       │   ├── controller/                     # Controladores REST
│       │   │   ├── FranquiciaController.java
│       │   │   ├── SucursalController.java
│       │   │   └── ProductoController.java
│       │   ├── dto/                            # DTOs
│       │   ├── config/                         # Configuraciones
│       │   │   └── RedisConfig.java
│       │   └── exception/                      # Manejo de errores
│       │       ├── GlobalExceptionHandler.java
│       │       └── ErrorResponse.java
│       └── resources/
│           ├── application.properties          # Configuración desarrollo (H2)
│           └── application-prod.properties     # Configuración producción (MySQL)
├── pom.xml
├── README.md
├── API_EJEMPLOS.md
└── .gitignore
```

---

## 🎯 Funcionalidades Implementadas

### ✅ 1. Agregar nueva franquicia

**POST** `/api/franquicias`

```json
{
  "nombre": "Franquicia Colombia"
}
```

### ✅ 2. Agregar sucursal a franquicia

**POST** `/api/sucursales`

```json
{
  "nombre": "Sucursal Norte",
  "franquiciaId": 1
}
```

### ✅ 3. Agregar producto a sucursal

**POST** `/api/productos`

```json
{
  "nombre": "Laptop Dell",
  "stock": 150,
  "sucursalId": 1
}
```

### ✅ 4. Eliminar producto de sucursal

**DELETE** `/api/productos/{productoId}/sucursal/{sucursalId}`

### ✅ 5. Modificar stock de producto

**PATCH** `/api/productos/{id}/stock`

```json
{
  "nuevoStock": 200
}
```

### ✅ 6. Obtener productos con mayor stock por sucursal

**GET** `/api/franquicias/{id}/productos-max-stock`

Retorna el producto con más stock de cada sucursal de una franquicia.

---

## 🛠️ Tecnologías Utilizadas

- ☕ **Java 17**
- 🍃 **Spring Boot 3.2.0**
- 🗄️ **Spring Data JPA** - Persistencia
- 🔴 **Redis** - Caché (opcional)
- 🐬 **MySQL** - Base de datos producción
- 💾 **H2 Database** - Base de datos desarrollo
- 🏗️ **Maven** - Gestión de dependencias
- 📦 **Lombok** - Reducir código boilerplate

---

## ⚡ Cómo Ejecutar

### 1️⃣ Ejecutar en modo desarrollo (H2)

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

### 2️⃣ Ejecutar en modo producción (MySQL)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 3️⃣ Compilar JAR

```bash
mvn clean package
java -jar target/franquicias-1.0.0.jar
```

---

## 🗄️ Base de Datos

### H2 Console (Desarrollo)

- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:franquiciasdb`
- **Usuario**: `sa`
- **Password**: _(dejar en blanco)_

### MySQL (Producción)

Crear la base de datos:

```sql
CREATE DATABASE franquiciasdb;
```

Configurar variables de entorno:

```bash
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
```

---

## 📡 Endpoints Disponibles

| Método | Endpoint                                    | Descripción                            |
| ------ | ------------------------------------------- | -------------------------------------- |
| POST   | `/api/franquicias`                          | Crear franquicia                       |
| GET    | `/api/franquicias`                          | Listar todas las franquicias           |
| GET    | `/api/franquicias/{id}`                     | Obtener franquicia por ID              |
| GET    | `/api/franquicias/{id}/productos-max-stock` | Productos con mayor stock por sucursal |
| POST   | `/api/sucursales`                           | Crear sucursal                         |
| GET    | `/api/sucursales/franquicia/{id}`           | Listar sucursales de franquicia        |
| GET    | `/api/sucursales/{id}`                      | Obtener sucursal por ID                |
| POST   | `/api/productos`                            | Crear producto                         |
| DELETE | `/api/productos/{id}/sucursal/{sucursalId}` | Eliminar producto                      |
| PATCH  | `/api/productos/{id}/stock`                 | Actualizar stock                       |
| GET    | `/api/productos/sucursal/{id}`              | Listar productos de sucursal           |
| GET    | `/api/productos/{id}`                       | Obtener producto por ID                |

---

## 🧪 Probar la API

### Opción 1: cURL (Windows PowerShell)

```powershell
# Crear franquicia
curl -X POST http://localhost:8080/api/franquicias `
  -H "Content-Type: application/json" `
  -d '{\"nombre\":\"Mi Franquicia\"}'

# Crear sucursal
curl -X POST http://localhost:8080/api/sucursales `
  -H "Content-Type: application/json" `
  -d '{\"nombre\":\"Sucursal Norte\",\"franquiciaId\":1}'

# Crear producto
curl -X POST http://localhost:8080/api/productos `
  -H "Content-Type: application/json" `
  -d '{\"nombre\":\"Laptop\",\"stock\":50,\"sucursalId\":1}'

# Ver productos con mayor stock
curl http://localhost:8080/api/franquicias/1/productos-max-stock
```

### Opción 2: Postman

Importar los ejemplos del archivo **API_EJEMPLOS.md**

---

## 🔴 Redis (Opcional)

El proyecto incluye soporte para caché con Redis. Para usarlo:

1. **Instalar Redis** (local o usar servicio en la nube)
2. **Configurar** en `application.properties`:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

El caché se aplica automáticamente en:

- Listado de franquicias
- Consultas de productos con mayor stock

---

## 📝 Modelo de Datos

### Franquicia

- `id` (Long)
- `nombre` (String) - Único
- `sucursales` (List<Sucursal>)

### Sucursal

- `id` (Long)
- `nombre` (String)
- `franquicia` (Franquicia)
- `productos` (List<Producto>)

### Producto

- `id` (Long)
- `nombre` (String)
- `stock` (Integer)
- `sucursal` (Sucursal)

---

## ✨ Características Adicionales

✅ **Validaciones** con Bean Validation  
✅ **Manejo centralizado de errores**  
✅ **Caché con Redis**  
✅ **Hot reload** con DevTools  
✅ **Logs detallados** de SQL  
✅ **Consola H2** para desarrollo  
✅ **Arquitectura por capas** (Controller → Service → Repository)  
✅ **DTOs** para separar API de modelo  
✅ **Transacciones** con @Transactional

---

## 🚀 Próximos Pasos

1. **Ejecutar la aplicación**: `mvn spring-boot:run`
2. **Probar los endpoints** con Postman o cURL
3. **Ver la consola H2**: http://localhost:8080/h2-console
4. **Leer API_EJEMPLOS.md** para más detalles

---

## 📞 Soporte

Para cualquier duda, revisar:

- **README.md** - Documentación completa
- **API_EJEMPLOS.md** - Ejemplos de uso de la API

---

**¡La API está lista para usar! 🎉**

### Colección de Postman para API de Franquicias

#### Variables de entorno

```
base_url = http://localhost:8080
```

---

### 1. Crear Franquicia

**POST** `{{base_url}}/api/franquicias`

Body (JSON):

```json
{
  "nombre": "Franquicia Colombia"
}
```

---

### 2. Obtener todas las Franquicias

**GET** `{{base_url}}/api/franquicias`

---

### 3. Obtener Franquicia por ID

**GET** `{{base_url}}/api/franquicias/1`

---

### 4. Agregar Sucursal a Franquicia

**POST** `{{base_url}}/api/sucursales`

Body (JSON):

```json
{
  "nombre": "Sucursal Norte",
  "franquiciaId": 1
}
```

---

### 5. Obtener Sucursales de una Franquicia

**GET** `{{base_url}}/api/sucursales/franquicia/1`

---

### 6. Agregar Producto a Sucursal

**POST** `{{base_url}}/api/productos`

Body (JSON):

```json
{
  "nombre": "Laptop Dell",
  "stock": 150,
  "sucursalId": 1
}
```

---

### 7. Eliminar Producto de Sucursal

**DELETE** `{{base_url}}/api/productos/{productoId}/sucursal/{sucursalId}`

Ejemplo: **DELETE** `{{base_url}}/api/productos/1/sucursal/1`

---

### 8. Modificar Stock de Producto

**PATCH** `{{base_url}}/api/productos/1/stock`

Body (JSON):

```json
{
  "nuevoStock": 200
}
```

---

### 9. Obtener Productos con Mayor Stock por Sucursal

**GET** `{{base_url}}/api/franquicias/1/productos-max-stock`

Retorna un listado con el producto que más stock tiene en cada sucursal de la franquicia.

---

### 10. Actualizar Nombre de Franquicia

**PATCH** `{{base_url}}/api/franquicias/1/nombre`

Body (JSON):

```json
{
  "nuevoNombre": "Franquicia Premium"
}
```

---

### 11. Actualizar Nombre de Sucursal

**PATCH** `{{base_url}}/api/sucursales/1/nombre`

Body (JSON):

```json
{
  "nuevoNombre": "Sucursal Centro Comercial"
}
```

---

### 12. Actualizar Nombre de Producto

**PATCH** `{{base_url}}/api/productos/1/nombre`

Body (JSON):

```json
{
  "nuevoNombre": "Laptop HP Premium"
}
```

---

### Ejemplo de Flujo Completo

```bash
# 1. Crear franquicia
curl -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Mi Franquicia"}'

# 2. Crear sucursales
curl -X POST http://localhost:8080/api/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Sucursal Norte","franquiciaId":1}'

curl -X POST http://localhost:8080/api/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Sucursal Sur","franquiciaId":1}'

# 3. Agregar productos a la Sucursal Norte
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop","stock":50,"sucursalId":1}'

curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Mouse","stock":100,"sucursalId":1}'

# 4. Agregar productos a la Sucursal Sur
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Teclado","stock":75,"sucursalId":2}'

curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Monitor","stock":120,"sucursalId":2}'

# 5. Ver productos con mayor stock por sucursal
curl http://localhost:8080/api/franquicias/1/productos-max-stock

# 6. Modificar stock de un producto
curl -X PATCH http://localhost:8080/api/productos/1/stock \
  -H "Content-Type: application/json" \
  -d '{"nuevoStock":200}'

# 7. Eliminar un producto
curl -X DELETE http://localhost:8080/api/productos/2/sucursal/1

# 8. Actualizar nombre de franquicia
curl -X PATCH http://localhost:8080/api/franquicias/1/nombre \
  -H "Content-Type: application/json" \
  -d '{"nuevoNombre":"Franquicia Premium"}'

# 9. Actualizar nombre de sucursal
curl -X PATCH http://localhost:8080/api/sucursales/1/nombre \
  -H "Content-Type: application/json" \
  -d '{"nuevoNombre":"Sucursal Centro"}'

# 10. Actualizar nombre de producto
curl -X PATCH http://localhost:8080/api/productos/1/nombre \
  -H "Content-Type: application/json" \
  -d '{"nuevoNombre":"Laptop HP"}'
```

---

### Respuestas de Ejemplo

#### Crear Franquicia (Respuesta)

```json
{
  "id": 1,
  "nombre": "Franquicia Colombia",
  "sucursales": []
}
```

#### Productos con Mayor Stock (Respuesta)

```json
[
  {
    "productoId": 2,
    "productoNombre": "Mouse",
    "stock": 100,
    "sucursalId": 1,
    "sucursalNombre": "Sucursal Norte"
  },
  {
    "productoId": 4,
    "productoNombre": "Monitor",
    "stock": 120,
    "sucursalId": 2,
    "sucursalNombre": "Sucursal Sur"
  }
]
```

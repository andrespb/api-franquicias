# 🌐 Guía de Despliegue en la Nube

Esta guía explica cómo desplegar la API de Franquicias usando bases de datos en la nube.

---

## 📋 Proveedores de Nube Soportados

### 1️⃣ **AWS (Amazon Web Services)**

- **RDS MySQL**: Base de datos relacional gestionada
- **DynamoDB**: Base de datos NoSQL (requiere configuración adicional)
- **DocumentDB**: Compatible con MongoDB

### 2️⃣ **Azure (Microsoft)**

- **Azure Database for MySQL**: Base de datos MySQL gestionada
- **Cosmos DB**: Base de datos NoSQL multi-modelo
- **App Service**: Para desplegar la aplicación

### 3️⃣ **Google Cloud Platform (GCP)**

- **Cloud SQL**: MySQL/PostgreSQL gestionado
- **Firestore/Datastore**: Base de datos NoSQL

### 4️⃣ **MongoDB Atlas**

- **MongoDB Atlas**: MongoDB gestionado en la nube

---

## 🚀 Configuración por Proveedor

### ☁️ AWS RDS MySQL

#### 1. Crear instancia RDS MySQL

```bash
# Via AWS Console o CLI
aws rds create-db-instance \
  --db-instance-identifier franquicias-db \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --master-username admin \
  --master-user-password YourPassword123! \
  --allocated-storage 20
```

#### 2. Configurar variables de entorno

```bash
export DATABASE_URL="jdbc:mysql://myinstance.abc123.us-east-1.rds.amazonaws.com:3306/franquiciasdb?useSSL=true"
export DATABASE_USERNAME="admin"
export DATABASE_PASSWORD="YourPassword123!"
```

#### 3. Ejecutar aplicación

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

---

### 🔷 Azure Database for MySQL

#### 1. Crear servidor MySQL en Azure

```bash
# Via Azure Portal o CLI
az mysql server create \
  --resource-group myResourceGroup \
  --name franquicias-mysql-server \
  --location eastus \
  --admin-user myadmin \
  --admin-password YourPassword123! \
  --sku-name B_Gen5_1
```

#### 2. Crear base de datos

```bash
az mysql db create \
  --resource-group myResourceGroup \
  --server-name franquicias-mysql-server \
  --name franquiciasdb
```

#### 3. Configurar firewall (permitir Azure Services)

```bash
az mysql server firewall-rule create \
  --resource-group myResourceGroup \
  --server franquicias-mysql-server \
  --name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

#### 4. Configurar variables de entorno

```bash
export DATABASE_URL="jdbc:mysql://franquicias-mysql-server.mysql.database.azure.com:3306/franquiciasdb?useSSL=true&requireSSL=true"
export DATABASE_USERNAME="myadmin@franquicias-mysql-server"
export DATABASE_PASSWORD="YourPassword123!"
```

#### 5. Ejecutar aplicación

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

---

### 🔵 Google Cloud SQL

#### 1. Crear instancia Cloud SQL

```bash
gcloud sql instances create franquicias-db \
  --database-version=MYSQL_8_0 \
  --tier=db-f1-micro \
  --region=us-central1
```

#### 2. Crear base de datos

```bash
gcloud sql databases create franquiciasdb \
  --instance=franquicias-db
```

#### 3. Crear usuario

```bash
gcloud sql users create myuser \
  --instance=franquicias-db \
  --password=YourPassword123!
```

#### 4. Configurar variables de entorno

```bash
export DATABASE_URL="jdbc:mysql://35.xxx.xxx.xxx:3306/franquiciasdb?useSSL=true"
export DATABASE_USERNAME="myuser"
export DATABASE_PASSWORD="YourPassword123!"
```

---

### 🍃 MongoDB Atlas

#### 1. Crear cluster en MongoDB Atlas

- Ir a https://www.mongodb.com/cloud/atlas
- Crear cluster gratuito (M0)
- Configurar usuario y contraseña
- Permitir acceso desde cualquier IP (0.0.0.0/0) para desarrollo

#### 2. Obtener Connection String

```
mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/franquiciasdb?retryWrites=true&w=majority
```

#### 3. Agregar dependencia MongoDB en pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

#### 4. Configurar variable de entorno

```bash
export MONGODB_URI="mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/franquiciasdb?retryWrites=true&w=majority"
```

#### 5. Ejecutar con perfil MongoDB

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mongodb
```

---

## 🔐 Seguridad y Mejores Prácticas

### 1. **Nunca hardcodear credenciales**

```properties
# ❌ MAL
spring.datasource.password=miPasswordSecreto

# ✅ BIEN
spring.datasource.password=${DATABASE_PASSWORD}
```

### 2. **Usar SSL/TLS para conexiones**

```properties
spring.datasource.url=jdbc:mysql://host:3306/db?useSSL=true
```

### 3. **Configurar Connection Pooling**

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

### 4. **Backup automático**

- AWS RDS: Habilitar automated backups
- Azure: Configurar backup retention
- MongoDB Atlas: Configurar continuous backup

### 5. **Monitoreo**

- AWS CloudWatch
- Azure Monitor
- Google Cloud Monitoring

---

## 🐳 Despliegue con Docker

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/franquicias-1.0.0.jar app.jar
EXPOSE 8080

# Variables de entorno se pasan en runtime
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "app.jar"]
```

### Docker Compose con MySQL

```yaml
version: "3.8"
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DATABASE_URL=jdbc:mysql://mysql:3306/franquiciasdb
      - DATABASE_USERNAME=root
      - DATABASE_PASSWORD=rootpass
    depends_on:
      - mysql

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=rootpass
      - MYSQL_DATABASE=franquiciasdb
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  mysql-data:
```

---

## 🌍 Variables de Entorno por Proveedor

### Desarrollo Local (H2)

```bash
export SPRING_PROFILES_ACTIVE=development
```

### Producción (MySQL en la nube)

```bash
export SPRING_PROFILES_ACTIVE=production
export DATABASE_URL="jdbc:mysql://host:3306/franquiciasdb?useSSL=true"
export DATABASE_USERNAME="admin"
export DATABASE_PASSWORD="password"
```

### MongoDB

```bash
export SPRING_PROFILES_ACTIVE=mongodb
export MONGODB_URI="mongodb+srv://user:pass@cluster.mongodb.net/db"
```

---

## 📊 Comparación de Proveedores

| Proveedor         | Servicio           | Costo Inicial      | Escalabilidad | Facilidad  |
| ----------------- | ------------------ | ------------------ | ------------- | ---------- |
| **AWS**           | RDS MySQL          | Free tier 12 meses | ⭐⭐⭐⭐⭐    | ⭐⭐⭐⭐   |
| **Azure**         | Database for MySQL | $13/mes (B1ms)     | ⭐⭐⭐⭐⭐    | ⭐⭐⭐⭐⭐ |
| **GCP**           | Cloud SQL          | Free tier limitado | ⭐⭐⭐⭐      | ⭐⭐⭐⭐   |
| **MongoDB Atlas** | MongoDB M0         | Gratis permanente  | ⭐⭐⭐⭐      | ⭐⭐⭐⭐⭐ |

---

## ✅ Checklist de Despliegue

- [ ] Crear instancia de base de datos en la nube
- [ ] Configurar reglas de firewall/seguridad
- [ ] Crear base de datos `franquiciasdb`
- [ ] Configurar usuario y contraseña
- [ ] Obtener connection string
- [ ] Configurar variables de entorno
- [ ] Probar conexión local con perfil production
- [ ] Compilar JAR: `mvn clean package`
- [ ] Desplegar aplicación
- [ ] Verificar endpoints funcionando
- [ ] Configurar backup automático
- [ ] Configurar monitoreo y alertas

---

## 🔧 Troubleshooting

### Error: "Access denied for user"

```bash
# Verificar credenciales
mysql -h hostname -u username -p

# Verificar permisos
GRANT ALL PRIVILEGES ON franquiciasdb.* TO 'username'@'%';
FLUSH PRIVILEGES;
```

### Error: "Connection timeout"

```bash
# Verificar firewall rules
# AWS: Security Groups
# Azure: Firewall Rules
# GCP: Authorized Networks
```

### Error: "SSL connection error"

```bash
# Agregar SSL=false solo para testing
jdbc:mysql://host:3306/db?useSSL=false

# En producción, descargar certificado SSL del proveedor
```

---

## 📞 Soporte

Para más información, consultar:

- [AWS RDS Documentation](https://docs.aws.amazon.com/rds/)
- [Azure Database for MySQL](https://docs.microsoft.com/azure/mysql/)
- [Google Cloud SQL](https://cloud.google.com/sql/docs)
- [MongoDB Atlas](https://docs.atlas.mongodb.com/)

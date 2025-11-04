# 🐳 Guía de Docker para API Franquicias

Esta guía explica cómo construir, ejecutar y desplegar la aplicación usando Docker.

---

## 📋 Requisitos Previos

- [Docker](https://www.docker.com/get-started) instalado (versión 20.10+)
- [Docker Compose](https://docs.docker.com/compose/install/) instalado (versión 2.0+)
- 4GB de RAM disponible
- Puertos disponibles: 8080 (API), 3306 (MySQL), 6379 (Redis)

---

## 🚀 Inicio Rápido

### Opción 1: Todo en Docker (Recomendado para Producción)

```bash
# Construir y levantar todos los servicios
docker-compose up -d

# Ver logs
docker-compose logs -f app

# La API estará disponible en: http://localhost:8080
```

### Opción 2: Solo Infraestructura (Desarrollo Local)

```bash
# Levantar solo MySQL y Redis
docker-compose -f docker-compose.dev.yml up -d

# Ejecutar la aplicación con Maven
mvn spring-boot:run
```

---

## 🏗️ Construcción de la Imagen

### Construir imagen manualmente

```bash
# Construir imagen
docker build -t franquicias-api:latest .

# Ver imágenes
docker images | grep franquicias
```

### Construcción multi-plataforma

```bash
# Para ARM64 (Apple Silicon, Raspberry Pi)
docker buildx build --platform linux/arm64 -t franquicias-api:arm64 .

# Para AMD64 (Intel/AMD)
docker buildx build --platform linux/amd64 -t franquicias-api:amd64 .

# Multi-plataforma
docker buildx build --platform linux/amd64,linux/arm64 -t franquicias-api:latest .
```

---

## 🎮 Comandos Docker Compose

### Iniciar servicios

```bash
# Iniciar en background
docker-compose up -d

# Iniciar con logs en tiempo real
docker-compose up

# Iniciar solo un servicio específico
docker-compose up -d mysql
docker-compose up -d redis
docker-compose up -d app
```

### Detener servicios

```bash
# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes (¡CUIDADO! Borra datos)
docker-compose down -v

# Detener sin eliminar contenedores
docker-compose stop
```

### Ver logs

```bash
# Logs de todos los servicios
docker-compose logs

# Logs en tiempo real
docker-compose logs -f

# Logs de un servicio específico
docker-compose logs -f app
docker-compose logs -f mysql
docker-compose logs -f redis

# Últimas 100 líneas
docker-compose logs --tail=100
```

### Estado de servicios

```bash
# Ver servicios en ejecución
docker-compose ps

# Ver estadísticas de recursos
docker stats

# Información detallada
docker-compose ps -a
```

---

## 🔧 Configuración con Variables de Entorno

### Crear archivo .env

```bash
# Crear .env en la raíz del proyecto
cat > .env << 'EOF'
# Base de datos
MYSQL_ROOT_PASSWORD=mi_password_seguro
MYSQL_DATABASE=franquiciasdb
MYSQL_USER=franquicias
MYSQL_PASSWORD=franquicias_pass

# Aplicación
SPRING_PROFILES_ACTIVE=production
JAVA_OPTS=-Xmx1024m -Xms512m

# Redis
REDIS_PASSWORD=redis_password
EOF
```

### Usar variables en docker-compose

```yaml
environment:
  - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
  - DATABASE_USERNAME=${MYSQL_USER}
  - DATABASE_PASSWORD=${MYSQL_PASSWORD}
```

---

## 🏥 Health Checks

### Verificar estado de los servicios

```bash
# Health check de la aplicación
curl http://localhost:8080/actuator/health

# Health check de MySQL
docker exec franquicias-mysql mysqladmin ping -h localhost -u root -prootpassword

# Health check de Redis
docker exec franquicias-redis redis-cli ping
```

### Health check automático en Docker

Los servicios tienen health checks configurados:

- **App**: Verifica `/actuator/health` cada 30s
- **MySQL**: Ping cada 10s
- **Redis**: Ping cada 10s

---

## 📊 Monitoreo y Debugging

### Acceder a contenedores

```bash
# Shell interactivo en la aplicación
docker exec -it franquicias-api sh

# Shell en MySQL
docker exec -it franquicias-mysql mysql -u root -p

# Shell en Redis
docker exec -it franquicias-redis redis-cli
```

### Ver recursos utilizados

```bash
# Uso de CPU, memoria, red, disco
docker stats franquicias-api
docker stats franquicias-mysql
docker stats franquicias-redis
```

### Inspeccionar contenedores

```bash
# Información detallada del contenedor
docker inspect franquicias-api

# Ver red
docker network inspect franquicias_franquicias-network

# Ver volúmenes
docker volume ls
docker volume inspect franquicias_mysql-data
```

---

## 🗄️ Gestión de Datos

### Backup de base de datos

```bash
# Crear backup
docker exec franquicias-mysql mysqldump -u root -prootpassword franquiciasdb > backup.sql

# Con fecha
docker exec franquicias-mysql mysqldump -u root -prootpassword franquiciasdb > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restaurar backup

```bash
# Restaurar desde backup
docker exec -i franquicias-mysql mysql -u root -prootpassword franquiciasdb < backup.sql
```

### Backup de Redis

```bash
# Crear snapshot
docker exec franquicias-redis redis-cli SAVE

# Copiar RDB file
docker cp franquicias-redis:/data/dump.rdb ./redis-backup.rdb
```

---

## 🌐 Despliegue en Producción

### Configuración recomendada

```yaml
# docker-compose.prod.yml
version: "3.8"

services:
  app:
    image: franquicias-api:latest
    restart: always
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - JAVA_OPTS=-Xmx2048m -Xms1024m
    deploy:
      resources:
        limits:
          cpus: "2"
          memory: 2G
        reservations:
          cpus: "1"
          memory: 1G
```

### Ejecutar en producción

```bash
# Usar archivo de producción
docker-compose -f docker-compose.prod.yml up -d

# Escalar servicio (si es necesario)
docker-compose up -d --scale app=3
```

---

## 🔐 Seguridad

### Mejores prácticas

1. **No usar contraseñas por defecto**

```bash
# Generar contraseña segura
openssl rand -base64 32
```

2. **Usar secrets en producción**

```yaml
secrets:
  db_password:
    file: ./secrets/db_password.txt
```

3. **Limitar recursos**

```yaml
deploy:
  resources:
    limits:
      cpus: "2"
      memory: 2G
```

4. **Usuario no-root**

```dockerfile
USER spring:spring
```

5. **Escanear vulnerabilidades**

```bash
docker scan franquicias-api:latest
```

---

## 🧹 Limpieza

### Limpiar recursos no utilizados

```bash
# Limpiar contenedores detenidos
docker container prune

# Limpiar imágenes no utilizadas
docker image prune

# Limpiar volúmenes no utilizados
docker volume prune

# Limpiar todo (¡CUIDADO!)
docker system prune -a --volumes
```

### Eliminar proyecto completo

```bash
# Detener y eliminar todo
docker-compose down -v --rmi all
```

---

## 📝 Estructura de Archivos Docker

```
franquicias/
├── Dockerfile                    # Imagen de la aplicación
├── .dockerignore                # Archivos a ignorar
├── docker-compose.yml           # Composición completa (producción)
├── docker-compose.dev.yml       # Solo infraestructura (desarrollo)
├── docker-compose.prod.yml      # Configuración de producción (opcional)
└── .env                         # Variables de entorno (no commitear)
```

---

## 🐛 Troubleshooting

### La aplicación no inicia

```bash
# Ver logs detallados
docker-compose logs -f app

# Verificar que MySQL esté listo
docker-compose logs mysql | grep "ready for connections"

# Reiniciar servicio
docker-compose restart app
```

### Error de conexión a MySQL

```bash
# Verificar que MySQL esté corriendo
docker-compose ps mysql

# Probar conexión
docker exec franquicias-mysql mysql -u root -p -e "SHOW DATABASES;"

# Verificar red
docker network inspect franquicias_franquicias-network
```

### Puerto ya en uso

```bash
# Windows - Ver qué proceso usa el puerto 8080
netstat -ano | findstr :8080

# Matar proceso (reemplazar PID)
taskkill /PID <PID> /F

# O cambiar puerto en docker-compose.yml
ports:
  - "8081:8080"
```

### Memoria insuficiente

```bash
# Aumentar memoria para Docker Desktop
# Settings > Resources > Memory > 4GB o más

# Reducir memoria de la JVM
environment:
  - JAVA_OPTS=-Xmx512m -Xms256m
```

---

## 📚 Comandos Útiles Resumidos

```bash
# Inicio rápido
docker-compose up -d                    # Levantar todo
docker-compose logs -f app              # Ver logs
docker-compose down                     # Detener todo

# Desarrollo
docker-compose -f docker-compose.dev.yml up -d   # Solo DB y Redis
mvn spring-boot:run                              # Ejecutar app local

# Mantenimiento
docker-compose restart app              # Reiniciar app
docker-compose exec app sh              # Acceder a contenedor
docker-compose ps                       # Estado de servicios

# Limpieza
docker-compose down -v                  # Detener y borrar volúmenes
docker system prune -a                  # Limpiar todo Docker
```

---

## 🎯 Próximos Pasos

1. ✅ Construir imagen Docker
2. ✅ Ejecutar con Docker Compose
3. ⬜ Configurar CI/CD para builds automáticos
4. ⬜ Desplegar en Kubernetes
5. ⬜ Configurar monitoreo con Prometheus/Grafana
6. ⬜ Implementar logs centralizados (ELK Stack)

---

## 📞 Soporte

Para más información sobre Docker:

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)

# Documentación de Arquitectura

## Sistema de Gestión de Reportes Regulatorios

### Versión 1.0

---

## 1. Descripción General

Sistema backend desarrollado en Java 17 con Spring Boot 3.5.7 para la gestión integral de reportes regulatorios. Permite a las empresas registrar, monitorear y consolidar reportes requeridos por entidades de control.

## 2. Arquitectura de Alto Nivel

### 2.1 Patrón Arquitectónico

El sistema implementa una **arquitectura en capas (Layered Architecture)** con las siguientes capas:

```
┌─────────────────────────────────────────┐
│         Capa de Presentación            │
│      (Controllers REST / OpenAPI)       │
├─────────────────────────────────────────┤
│         Capa de Aplicación              │
│    (Services / Business Logic)          │
├─────────────────────────────────────────┤
│         Capa de Persistencia            │
│    (Repositories / Spring Data JPA)     │
├─────────────────────────────────────────┤
│         Capa de Datos                   │
│         (PostgreSQL Database)           │
└─────────────────────────────────────────┘
```

### 2.2 Componentes Principales

#### Controllers (Capa de Presentación)
- **AuthController**: Autenticación y registro de usuarios
- **UsuarioController**: Gestión de usuarios
- **EntidadController**: Gestión de entidades de control
- **ReporteController**: CRUD de reportes
- **EvidenciaController**: Subida/descarga de archivos
- **DashboardController**: Métricas y estadísticas

#### Services (Capa de Negocio)
- **AuthService**: Lógica de autenticación y JWT
- **UsuarioService**: Gestión de usuarios
- **EntidadService**: Gestión de entidades
- **ReporteService**: Lógica de reportes
- **EvidenciaService**: Gestión de archivos
- **DashboardService**: Cálculo de métricas
- **AlertaService**: Generación y envío de alertas
- **EmailService**: Envío de notificaciones

## 3. Modelo de Datos

### 3.1 Entidades Principales

**Usuario**
- Representa usuarios del sistema
- Relación N:M con Rol
- Relación 1:N con Reporte (como responsable)

**Rol**
- Define permisos (ROLE_USER, ROLE_ADMIN)

**Entidad**
- Representa entidades de control regulatorias
- Relación 1:N con Reporte

**Reporte**
- Representa reportes regulatorios
- Estados: PENDIENTE, EN_PROGRESO, ENVIADO
- Frecuencias: MENSUAL, TRIMESTRAL, SEMESTRAL, ANUAL

**Evidencia**
- Almacena archivos relacionados con reportes

**Alerta**
- Notificaciones automáticas de vencimientos

## 4. Seguridad

### 4.1 Autenticación

- **Método**: JSON Web Tokens (JWT)
- **Algoritmo**: HMAC-SHA256
- **Expiración**: 24 horas (configurable)

### 4.2 Autorización

- **Control de Acceso Basado en Roles (RBAC)**
- Roles: `ROLE_USER`, `ROLE_ADMIN`

### 4.3 Protecciones

- Contraseñas hasheadas con BCrypt
- Validación de entrada con Bean Validation
- CORS configurado
- Prevención SQL Injection mediante JPA

## 5. Tecnologías

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 3.5.7 | Framework principal |
| Spring Security | 6.x | Seguridad |
| Spring Data JPA | 3.x | Persistencia |
| PostgreSQL | 15+ | Base de datos |
| JWT (jjwt) | 0.12.3 | Autenticación |
| Lombok | 1.18.30 | Reducción de código |
| MapStruct | 1.5.5 | Mapeo DTOs |
| springdoc-openapi | 2.3.0 | Documentación API |

## 6. Configuración

### 6.1 Perfiles

- **dev**: Desarrollo local
- **prod**: Producción
- **test**: Testing

### 6.2 Variables de Entorno

```bash
DATABASE_URL=jdbc:postgresql://host:5432/db
DATABASE_USER=usuario
DATABASE_PASSWORD=contraseña
JWT_SECRET=clave-secreta-segura
MAIL_HOST=smtp.gmail.com
```

## 7. Despliegue

### Con Docker Compose

```bash
docker-compose up -d
```

### Manual

```bash
mvn clean package
java -jar target/llanogas-0.0.1-SNAPSHOT.jar
```

---

**Versión**: 1.0  
**Fecha**: Octubre 2025

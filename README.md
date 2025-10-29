# Sistema de Gestión de Reportes Regulatorios

## Descripción

Sistema backend REST API para gestionar reportes regulatorios de una empresa, permitiendo el registro, seguimiento y consolidación de reportes requeridos por entidades de control.

## Características Principales

- 🔐 **Autenticación JWT** con roles de usuario (USER, ADMIN)
- 📊 **Gestión completa de reportes** con estados y fechas de vencimiento
- 🏢 **Administración de entidades** de control
- 📁 **Repositorio de evidencias** (carga y descarga de archivos PDF, Excel)
- 📅 **Sistema de alertas** automáticas por vencimientos
- 📈 **Dashboard** con métricas y tasas de cumplimiento
- 📧 **Notificaciones por email** automáticas
- 📝 **Documentación OpenAPI/Swagger**
- 🐳 **Dockerizado** con PostgreSQL

## Tecnologías

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring Security** con JWT
- **PostgreSQL 15**
- **MapStruct** para mapeo de DTOs
- **Lombok** para reducir código boilerplate
- **springdoc-openapi** para documentación
- **Maven** como gestor de dependencias
- **Docker & Docker Compose**

## Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- PostgreSQL 15+ (o usar Docker Compose)
- Docker y Docker Compose (opcional)

## Instalación y Ejecución

### Opción 1: Con Docker Compose (Recomendado)

```bash
# Clonar el repositorio
git clone <repository-url>
cd backend

# Levantar servicios con Docker Compose
docker-compose up -d

# La API estará disponible en http://localhost:8080
# Swagger UI en http://localhost:8080/swagger-ui.html
```

### Opción 2: Ejecución Local

```bash
# 1. Configurar PostgreSQL local
createdb reportes_db

# 2. Compilar el proyecto
mvn clean package -DskipTests

# 3. Ejecutar la aplicación
mvn spring-boot:run

# O ejecutar el JAR
java -jar target/llanogas-0.0.1-SNAPSHOT.jar
```

### Opción 3: Desarrollo con perfil dev

```bash
# Ejecutar con perfil de desarrollo
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Configuración

### Variables de Entorno

```bash
# Base de datos
DATABASE_URL=jdbc:postgresql://localhost:5432/reportes_db
DATABASE_USER=postgres
DATABASE_PASSWORD=postgres

# JWT
JWT_SECRET=mySecretKeyForJWTTokenGenerationPleaseChangeInProduction

# Email (Producción)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@example.com
MAIL_PASSWORD=your-password
```

### Perfiles

- **dev**: Desarrollo local, SQL visible, servidor de email local
- **prod**: Producción, configuración desde variables de entorno

## Uso de la API

### Documentación Interactiva

Acceder a Swagger UI: `http://localhost:8080/swagger-ui.html`

### Ejemplos de Endpoints

#### 1. Registro de Usuario

```bash
curl -X POST http://localhost:8080/api/auth/registro \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "nombre": "Admin",
    "apellido": "Sistema",
    "password": "admin123",
    "roles": ["admin"]
  }'
```

#### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "admin123"
  }'
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tipo": "Bearer",
  "id": 1,
  "email": "admin@example.com",
  "nombre": "Admin",
  "apellido": "Sistema",
  "roles": ["ROLE_ADMIN"]
}
```

#### 3. Crear Entidad (requiere token)

```bash
curl -X POST http://localhost:8080/api/entidades \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Superintendencia Financiera",
    "codigo": "SUPFIN",
    "descripcion": "Entidad de control financiero",
    "activo": true
  }'
```

#### 4. Crear Reporte

```bash
curl -X POST http://localhost:8080/api/reportes \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Reporte Mensual de Liquidez",
    "descripcion": "Reporte mensual de indicadores de liquidez",
    "entidadId": 1,
    "frecuencia": "MENSUAL",
    "formato": "EXCEL",
    "resolucion": "Resolución 001/2024",
    "responsableId": 1,
    "fechaVencimiento": "2024-12-31",
    "estado": "PENDIENTE"
  }'
```

#### 5. Subir Evidencia

```bash
curl -X POST http://localhost:8080/api/evidencias/reporte/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/ruta/al/archivo.pdf"
```

#### 6. Obtener Dashboard

```bash
curl -X GET http://localhost:8080/api/dashboard/estadisticas \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 7. Listar Reportes por Estado

```bash
curl -X GET "http://localhost:8080/api/reportes/estado/PENDIENTE?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/reto/spring/llanogas/
│   │   ├── config/          # Configuraciones (Security, OpenAPI, CORS)
│   │   ├── controller/      # Controladores REST
│   │   ├── dto/             # Objetos de transferencia de datos
│   │   ├── entity/          # Entidades JPA
│   │   ├── exception/       # Manejo de excepciones
│   │   ├── repository/      # Repositorios Spring Data
│   │   ├── security/        # Seguridad y JWT
│   │   └── service/         # Lógica de negocio
│   └── resources/
│       └── application.yml  # Configuración de la aplicación
└── test/                    # Tests unitarios e integración
```

## Modelo de Datos

### Entidades Principales

- **Usuario**: Usuarios del sistema con roles
- **Rol**: Roles de autorización (USER, ADMIN)
- **Entidad**: Entidades de control regulatorias
- **Reporte**: Reportes con estados y vencimientos
- **Evidencia**: Archivos adjuntos a reportes
- **Alerta**: Alertas automáticas de vencimientos

### Estados de Reporte

- `PENDIENTE`: Reporte pendiente de iniciar
- `EN_PROGRESO`: Reporte en desarrollo
- `ENVIADO`: Reporte completado y enviado

### Frecuencias de Reporte

- `MENSUAL`
- `TRIMESTRAL`
- `SEMESTRAL`
- `ANUAL`

## Testing

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con reporte de cobertura
mvn test jacoco:report
```

## Seguridad

- Autenticación basada en JWT
- Contraseñas encriptadas con BCrypt
- Control de acceso basado en roles (RBAC)
- Validación de entrada con Bean Validation
- Protección CSRF deshabilitada para API REST
- CORS configurado para desarrollo

## Tareas Programadas

- **Alertas automáticas**: Diarias a las 8:00 AM (configurable)
- **Envío de emails**: Cada 5 minutos para alertas pendientes

## Contribuir

1. Fork el proyecto
2. Crear rama de feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -m 'feat: agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## Formato de Commits

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` Nueva funcionalidad
- `fix:` Corrección de bugs
- `docs:` Cambios en documentación
- `refactor:` Refactorización de código
- `test:` Agregar o modificar tests
- `chore:` Tareas de mantenimiento

## Licencia

Este proyecto está bajo la Licencia Apache 2.0.

## Soporte

Para reportar problemas o solicitar funcionalidades, crear un issue en el repositorio.

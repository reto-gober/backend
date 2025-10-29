# Ejemplos de Uso de la API

Este documento contiene ejemplos prácticos de uso de los endpoints de la API.

## Configuración Base

**URL Base**: `http://localhost:8080`  
**Swagger UI**: `http://localhost:8080/swagger-ui.html`

## 1. Autenticación

### 1.1 Registrar Usuario Administrador

```bash
curl -X POST http://localhost:8080/api/auth/registro \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@empresa.com",
    "nombre": "Administrador",
    "apellido": "Sistema",
    "password": "Admin123!",
    "roles": ["admin"]
  }'
```

**Respuesta**:
```json
{
  "mensaje": "Usuario registrado exitosamente"
}
```

### 1.2 Registrar Usuario Estándar

```bash
curl -X POST http://localhost:8080/api/auth/registro \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@empresa.com",
    "nombre": "Juan",
    "apellido": "Pérez",
    "password": "User123!"
  }'
```

### 1.3 Iniciar Sesión

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@empresa.com",
    "password": "Admin123!"
  }'
```

**Respuesta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbXByZXNhLmNvbSIsImlhdCI6MTYzOTU4...",
  "tipo": "Bearer",
  "id": 1,
  "email": "admin@empresa.com",
  "nombre": "Administrador",
  "apellido": "Sistema",
  "roles": ["ROLE_ADMIN", "ROLE_USER"]
}
```

**Guardar el token para usar en los siguientes requests**:
```bash
export TOKEN="eyJhbGciOiJIUzI1NiJ9..."
```

## 2. Gestión de Entidades

### 2.1 Crear Entidad

```bash
curl -X POST http://localhost:8080/api/entidades \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Superintendencia Financiera",
    "codigo": "SUPFIN",
    "descripcion": "Entidad de supervisión y control del sistema financiero",
    "activo": true
  }'
```

### 2.2 Listar Entidades

```bash
curl -X GET "http://localhost:8080/api/entidades?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

### 2.3 Obtener Entidad por ID

```bash
curl -X GET http://localhost:8080/api/entidades/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 2.4 Actualizar Entidad

```bash
curl -X PUT http://localhost:8080/api/entidades/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Superintendencia Financiera de Colombia",
    "codigo": "SFC",
    "descripcion": "Entidad de supervisión del sistema financiero colombiano",
    "activo": true
  }'
```

## 3. Gestión de Reportes

### 3.1 Crear Reporte

```bash
curl -X POST http://localhost:8080/api/reportes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Reporte Mensual de Liquidez - Enero 2025",
    "descripcion": "Reporte de indicadores de liquidez correspondiente al mes de enero",
    "entidadId": 1,
    "frecuencia": "MENSUAL",
    "formato": "EXCEL",
    "resolucion": "Resolución Externa 034 de 2022",
    "responsableId": 1,
    "fechaVencimiento": "2025-02-10",
    "estado": "PENDIENTE"
  }'
```

### 3.2 Listar Todos los Reportes

```bash
curl -X GET "http://localhost:8080/api/reportes?page=0&size=10&sort=fechaVencimiento,asc" \
  -H "Authorization: Bearer $TOKEN"
```

### 3.3 Filtrar Reportes por Estado

```bash
# Reportes pendientes
curl -X GET "http://localhost:8080/api/reportes/estado/PENDIENTE?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"

# Reportes en progreso
curl -X GET "http://localhost:8080/api/reportes/estado/EN_PROGRESO?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"

# Reportes enviados
curl -X GET "http://localhost:8080/api/reportes/estado/ENVIADO?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

### 3.4 Listar Reportes Vencidos

```bash
curl -X GET http://localhost:8080/api/reportes/vencidos \
  -H "Authorization: Bearer $TOKEN"
```

### 3.5 Cambiar Estado de Reporte

```bash
# Cambiar a EN_PROGRESO
curl -X PATCH "http://localhost:8080/api/reportes/1/estado?estado=EN_PROGRESO" \
  -H "Authorization: Bearer $TOKEN"

# Cambiar a ENVIADO
curl -X PATCH "http://localhost:8080/api/reportes/1/estado?estado=ENVIADO" \
  -H "Authorization: Bearer $TOKEN"
```

### 3.6 Actualizar Reporte

```bash
curl -X PUT http://localhost:8080/api/reportes/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Reporte Mensual de Liquidez - Enero 2025 (Actualizado)",
    "descripcion": "Versión actualizada del reporte de liquidez",
    "entidadId": 1,
    "frecuencia": "MENSUAL",
    "formato": "EXCEL",
    "resolucion": "Resolución Externa 034 de 2022",
    "responsableId": 1,
    "fechaVencimiento": "2025-02-15",
    "estado": "EN_PROGRESO"
  }'
```

## 4. Gestión de Evidencias

### 4.1 Subir Evidencia (Archivo)

```bash
curl -X POST http://localhost:8080/api/evidencias/reporte/1 \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/ruta/al/archivo/reporte_liquidez_enero_2025.xlsx"
```

### 4.2 Listar Evidencias de un Reporte

```bash
curl -X GET http://localhost:8080/api/evidencias/reporte/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 4.3 Descargar Evidencia

```bash
curl -X GET http://localhost:8080/api/evidencias/1/descargar \
  -H "Authorization: Bearer $TOKEN" \
  -O -J
```

### 4.4 Eliminar Evidencia

```bash
curl -X DELETE http://localhost:8080/api/evidencias/1 \
  -H "Authorization: Bearer $TOKEN"
```

## 5. Dashboard y Métricas

### 5.1 Obtener Estadísticas Generales

```bash
curl -X GET http://localhost:8080/api/dashboard/estadisticas \
  -H "Authorization: Bearer $TOKEN"
```

**Respuesta**:
```json
{
  "totalReportes": 45,
  "reportesPendientes": 12,
  "reportesEnProgreso": 8,
  "reportesEnviados": 25,
  "reportesVencidos": 3,
  "tasaCumplimiento": 85.5
}
```

### 5.2 Obtener Tasa de Cumplimiento Mensual

```bash
curl -X GET http://localhost:8080/api/dashboard/cumplimiento \
  -H "Authorization: Bearer $TOKEN"
```

**Respuesta**:
```json
85.5
```

## 6. Gestión de Usuarios (Solo Admin)

### 6.1 Listar Usuarios

```bash
curl -X GET "http://localhost:8080/api/usuarios?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

### 6.2 Obtener Usuario por ID

```bash
curl -X GET http://localhost:8080/api/usuarios/1 \
  -H "Authorization: Bearer $TOKEN"
```

## 7. Escenarios Completos

### 7.1 Flujo Completo: Crear y Completar Reporte

```bash
# 1. Login
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@empresa.com","password":"Admin123!"}' \
  | jq -r '.token')

# 2. Crear entidad
curl -X POST http://localhost:8080/api/entidades \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Banco de la República","codigo":"BANREP","activo":true}'

# 3. Crear reporte
REPORTE_ID=$(curl -X POST http://localhost:8080/api/reportes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo":"Reporte Inflación Q1 2025",
    "entidadId":1,
    "frecuencia":"TRIMESTRAL",
    "formato":"PDF",
    "responsableId":1,
    "fechaVencimiento":"2025-04-15"
  }' | jq -r '.id')

# 4. Cambiar a EN_PROGRESO
curl -X PATCH "http://localhost:8080/api/reportes/$REPORTE_ID/estado?estado=EN_PROGRESO" \
  -H "Authorization: Bearer $TOKEN"

# 5. Subir evidencia
curl -X POST http://localhost:8080/api/evidencias/reporte/$REPORTE_ID \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@reporte_inflacion.pdf"

# 6. Marcar como ENVIADO
curl -X PATCH "http://localhost:8080/api/reportes/$REPORTE_ID/estado?estado=ENVIADO" \
  -H "Authorization: Bearer $TOKEN"

# 7. Verificar estadísticas
curl -X GET http://localhost:8080/api/dashboard/estadisticas \
  -H "Authorization: Bearer $TOKEN"
```

## 8. Manejo de Errores

### 8.1 Error de Autenticación (401)

```bash
curl -X GET http://localhost:8080/api/reportes \
  -H "Authorization: Bearer token_invalido"
```

**Respuesta**:
```json
{
  "error": "No autorizado",
  "mensaje": "Token JWT inválido"
}
```

### 8.2 Error de Validación (400)

```bash
curl -X POST http://localhost:8080/api/auth/registro \
  -H "Content-Type: application/json" \
  -d '{
    "email": "email-invalido",
    "nombre": "Test",
    "password": "123"
  }'
```

**Respuesta**:
```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 400,
  "error": "Validation Error",
  "mensaje": "Error en la validación de datos",
  "path": "/api/auth/registro",
  "detalles": [
    "El email debe ser válido",
    "La contraseña debe tener al menos 6 caracteres",
    "El apellido es obligatorio"
  ]
}
```

### 8.3 Recurso No Encontrado (404)

```bash
curl -X GET http://localhost:8080/api/reportes/999 \
  -H "Authorization: Bearer $TOKEN"
```

**Respuesta**:
```json
{
  "timestamp": "2025-01-15T10:35:00",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Reporte no encontrado con id: 999",
  "path": "/api/reportes/999"
}
```

---

**Nota**: Todos estos ejemplos están disponibles y probados en Swagger UI: `http://localhost:8080/swagger-ui.html`

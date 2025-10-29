Backend (plan)

Sugerido: Spring Boot 3 (Java 17), Spring Data JPA, PostgreSQL, Spring Security (JWT), envío de correos.

Módulos esperados

- `api`: API REST para reportes, usuarios, evidencias y alertas
- Migraciones: Flyway/Liquibase
- Tests: unitarios e integración (Testcontainers)

Pasos para iniciar (opción A: Spring Boot)

1) Crear proyecto (Spring Initializr)
   - Dependencies: Web, JPA, Validation, Security, Mail, PostgreSQL, Flyway
2) Configurar `application.yml` (DB, JWT, mail, storage)
3) Implementar entidades y repositorios según modelo SQL
4) Exponer endpoints: auth, reportes, evidencias, calendario, dashboard, alertas

Pasos alternos (opción B: Node/Express) si se prefiere JS/TS.

Infra

- DB: PostgreSQL 15+
- Storage: S3/MinIO para evidencias (URLs firmadas)
- Jobs: scheduler diario para alertas


# backend
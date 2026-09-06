# bovine-link-api

Backend (Java 17 + Spring Boot 4) para la publicación y venta de ganado bovino.

## Requisitos

- JDK 17 o superior (`JAVA_HOME` configurado)
- PostgreSQL con una base llamada `bovine_db`
- No hace falta instalar Maven: se usa el wrapper (`mvnw` / `mvnw.cmd`)

## Configuración de la base de datos

La conexión se define en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bovine_db
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:changeme}
```

La contraseña **no se versiona**. Cada quien la provee por variable de entorno.
Elegí una opción:

### Opción A — variable de entorno persistente (recomendada, se hace una sola vez)

Windows / PowerShell:

```powershell
[Environment]::SetEnvironmentVariable("DB_PASSWORD", "TU_CLAVE", "User")
```

Cerrá y volvé a abrir la terminal. A partir de ahí `DB_PASSWORD` ya está para
siempre y solo hace falta:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS: agregá `export DB_PASSWORD=TU_CLAVE` a `~/.bashrc` o `~/.zshrc`.

### Opción B — pasarla en cada corrida

```powershell
$env:DB_PASSWORD = "TU_CLAVE"; .\mvnw.cmd spring-boot:run
```

### Opción C — archivo local ignorado por git

Creá `src/main/resources/application-local.properties` (está en `.gitignore`):

```properties
spring.datasource.password=TU_CLAVE
```

y corré con el perfil `local`:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

## Migraciones

Las maneja **Flyway** automáticamente al arrancar
(`src/main/resources/db/migration/`). No hay que ejecutar nada a mano.
Hibernate corre en modo `validate`: valida que las entidades calcen con el
esquema, no lo modifica.

`V4__datos_demo.sql` siembra datos de prueba (también automático). Usuarios demo,
todos con contraseña **`demo1234`**:

| correo | rol |
|---|---|
| `admin@bovinelink.test` | ADMIN |
| `carlos@demo.test`, `jazmin@demo.test`, `jamie@demo.test` | USUARIO |

Para resetear la base en desarrollo:

```sql
DROP SCHEMA public CASCADE; CREATE SCHEMA public;
```

y reiniciar la app.

## Compilar / probar

```powershell
.\mvnw.cmd clean verify
```

## Nota sobre secretos y GitHub

Las variables de entorno (`DB_PASSWORD`, etc.) son **locales de tu máquina** y no
se suben al repositorio. En GitHub, para pipelines de CI/CD, se configuran como
*Secrets* del repo (Settings → Secrets and variables → Actions), nunca en
archivos versionados.

# Slash Music - Plataforma de Streaming Musical
## Descripción del Proyecto
MusicStream es una aplicación de streaming de música desarrollada como proyecto universitario. Permite a los usuarios explorar, buscar y reproducir canciones y crear playlists personalizadass.

## Características Principales
- Reproducción de canciones en streaming
- Gestión de usuarios y perfiles
- Creación y edición de playlists
- Sistema de búsqueda
- API RESTful documentada
- Base de datos con migraciones controladas

## Tecnologías Utilizadas
- **Spring Boot 3.x** - Framework principal
- **Gradle** - Gestión de dependencias y build
- **Lombok** - Reducción de código boilerplate
- **Spring Data JPA** - Persistencia de datos
- **Flyway** - Migraciones de base de datos
- **Swagger/OpenAPI** - Documentación de API
- **PostgreSQL** - Base de datos
- **Docker** - Contenerización de servicios

## Prerrequisitos
Antes de ejecutar la aplicación, asegúrate de tener instalado:

- Java 17 o superior
- Docker y Docker Compose
- Gradle 7.6 o superior
- Git

## Ejecución del Proyecto
### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/music-stream-app.git
cd music-stream-app
```
### 2. Ejecutar con Docker Compose
Opción A: Ejecutar todo con Docker Compose (recomendado):

```bash
# Levantar la base de datos
docker-compose up --build

# Para ejecutar en segundo plano
docker-compose up -d --build
```

### 3. Compilar y ejecutar la aplicación con Gradle
./gradlew clean build
./gradlew bootRun

**En Windows**
gradlew.bat clean build
gradlew.bat bootRun

### 4. Verificar la ejecución
Una vez iniciada, la aplicación estará disponible en:

- **Aplicación:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **Base de datos:** localhost:5432

📁 Estructura del Proyecto
```text
src/
├── main/
│   ├── java/
│   │   ├── com/slash/music/
│   │   │   ├── config/          # Configuraciones
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Excepctiones
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositorios JPA
│   │   │   └── service/         # Lógica de negocio
│   ├── resources/
│   │   ├── db/migration/        # Scripts de Flyway
│   │   ├── static/              # Archivos estáticos
│   │   └── application.properties
docker-compose
```

## Base de Datos
La aplicación utiliza **PostgreSQL** con `Flyway` para migraciones. Las migraciones se ejecutan automáticamente al iniciar la aplicación.

### Esquema principal:

- **Tablas:** `users`, `songs`, `artists`, `albums`, `playlists`, `playlist_songs`
- Las migraciones se encuentran en src/main/resources/db/migration/

## API Documentation
La API está documentada con **Swagger/OpenAPI.** Una vez ejecutada la aplicación, accede a:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

## Troubleshooting
### Problemas comunes:
- **Puerto ya en uso:**
  ```bash
  # Cambiar puerto en application.properties o detener proceso existente
  lsof -ti:8080 | xargs kill -9
  ```
- **Error de conexión a BD:**
  Verificar que Docker esté ejecutándose

- **Problemas con Gradle:**
  ```bash
  # Limpiar y regenerar caché
  ./gradlew clean --refresh-dependencies
  
  # Forzar actualización de dependencias
  ./gradlew build --refresh-dependencies
  ```

- **Problemas con Lombok en IDE:**
  Asegurarse de tener el plugin de Lombok instalado en el IDE
  
  Habitar anotación processing en las configuraciones del IDE

## Contribución
### Ramas Principales
- **master** - Producción
- **develop** - Desarrollo principal
- **release/*** - Preparación de releases
- **hotfix/*** - Correcciones urgentes
- **feat/*** - Nuevas funcionalidades

### Conventional Commits
- **Formato:**
```text
<tipo>[ámbito opcional]: <descripción>

[cuerpo opcional]

[pie opcional]

Tipos principales:

feat: - Nueva característica
fix: - Corrección de bug
docs: - Cambios en documentación
style: - Formato, puntos y coma, etc. (sin cambio lógico)
refactor: - Refactorización de código
test: - Adición de tests
chore: - Cambios en build, dependencias, etc.
```

### Ejemplos de Uso
1. Rama Feature (nueva funcionalidad)
```bash
# Crear rama desde develop
git checkout develop
git pull origin develop
git checkout -b feat/user-authentication

# Trabajar en la feature...
git add .
git commit -m "feat(auth): implement JWT authentication system"
git commit -m "feat(auth): add user registration endpoint"
git commit -m "test(auth): add unit tests for auth service"

# Finalizar feature
git checkout develop
git merge --no-ff feat/user-authentication
git branch -d feat/user-authentication
```

2. Rama Hotfix (corrección urgente)
```bash
# Crear hotfix desde master
git checkout master
git pull origin master
git checkout -b hotfix/fix-login-bug

# Trabajar en el hotfix...
git add .
git commit -m "fix(auth): resolve null pointer in login service"
git commit -m "test(auth): add test case for login edge case"

# Finalizar hotfix
git checkout master
git merge --no-ff hotfix/fix-login-bug
git checkout develop
git merge --no-ff hotfix/fix-login-bug
git branch -d hotfix/fix-login-bug
```

3. Rama Release (preparar versión)
```bash
# Crear release desde develop
git checkout develop
git pull origin develop
git checkout -b release/v1.2.0

# Preparar release (version bump, docs, etc.)
git add .
git commit -m "chore(release): bump version to 1.2.0"
git commit -m "docs: update CHANGELOG for v1.2.0"

# Finalizar release
git checkout master
git merge --no-ff release/v1.2.0
git tag -a v1.2.0 -m "Release version 1.2.0"
git checkout develop
git merge --no-ff release/v1.2.0
git branch -d release/v1.2.0
```
### Ejemplos de Commits Convencionales
**Ejemplos BUENOS:**
```bash
# Nueva característica
git commit -m "feat(playlist): add collaborative playlist functionality"

# Corrección de bug
git commit -m "fix(player): resolve audio streaming buffer issue"

# Documentación
git commit -m "docs: update API documentation for song endpoints"

# Refactorización
git commit -m "refactor(service): optimize song search algorithm"

# Tests
git commit -m "test(repository): add integration tests for user queries"

# Configuración
git commit -m "chore: update gradle dependencies to latest versions"

# Estilo de código
git commit -m "style: format code according to Google Java style guide"
```

**Ejemplos MALOS:**
```bash
# Muy vago
git commit -m "update code"

# Sin tipo conventional
git commit -m "added new feature"

# Tipo incorrecto
git commit -m "feature: new endpoint"

# Demasiado largo en asunto
git commit -m "fix: resolved the issue with the user authentication service that was causing null pointer exceptions when handling expired tokens in the JWT validation process"
🏷️ Ejemplos con Ámbito Específico
bash
# Con ámbito claro
git commit -m "feat(api): add pagination to songs endpoint"
git commit -m "fix(db): resolve migration conflict in V3__schema"
git commit -m "test(service): add mock tests for playlist service"
git commit -m "refactor(model): simplify user entity relationships"
git commit -m "docs(api): document authentication requirements"
git commit -m "chore(deps): upgrade Spring Boot to 3.2.1"
```

## Licencia
Este proyecto fue desarrollado con fines académicos para la Universidad [UNIAJC]. Distribuido bajo la licencia MIT.

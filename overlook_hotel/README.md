# 🏨 Overlook Hotel
#### (🇫🇷 Français ci-dessous)

A secure, role-based hotel management platform. 

Hotel guests can discover and book rooms and event spaces. 
Staff and managers can perform core hotel operations.

## Key Features

⭐ Role-Based Access	Different access levels for guests, staff, and managers.

⭐ Room and Event Booking - Guests can discover and reserve accommodations and event spaces.

⭐ Core Operations - Staff can manage bookings, check-ins, and other hotel operations.

⭐ Security - Utilizes JWT for secure authentication and authorization.

## 🛠 Setup

### 1. ✅ Prerequisites

Before you begin, make sure you have the following software installed:

- Java 17 (or later)  
- Apache Maven
- PostgreSQL 12+  
- Git
- psql (PostgreSQL client) — usually installed with PostgreSQL

### 2. 📦 Clone the Repository

``` bash
git clone https://github.com/jerome-cossu/overlook_hotel.git
cd overlook_hotel
```
### 3. Environment variables (recommended)

To keep secrets out of source control, use environment variables. An example file is provided as `env.example` and `environment.properties.example` — copy one to `.env` or `environment.properties` (per your workflow) and edit values locally.

Create a local `.env` (optional, if you use a tool to load `.env`) or export variables in your shell:

`.env` (example — do NOT commit real secrets)

``` properties
DB_USER=overlook_user
DB_PASSWORD=overlook_pass
DB_NAME=overlook_hotel
DB_HOST=localhost
DB_PORT=5432

APP_JWT_SECRET=change_this_to_a_strong_secret
SPRING_PROFILES_ACTIVE=dev
```
Or export in POSIX shell:

``` bash
export DB_USER=overlook_user
export DB_PASSWORD=overlook_pass
export DB_NAME=overlook_hotel
export DB_HOST=localhost
export DB_PORT=5432
export APP_JWT_SECRET=change_this_to_a_strong_secret
export SPRING_PROFILES_ACTIVE=dev
```
Windows PowerShell:

``` powershell
$env:DB_PASSWORD = "overlook_pass"
$env:SPRING_PROFILES_ACTIVE = "dev"
```

### 4. 🗄️ Configure the Database

#### 4.1. Start PostgreSQL

MacOS (Homebrew):

``` bash
brew services start postgresql
```

Linux (systemd):

``` bash
sudo systemctl start postgresql
```

Windows:

Start “PostgreSQL” service or launch pgAdmin / SQL Shell (psql).

#### 4.2. Create a Database User & Database (automatic or manual)

**Option A** — automatic (recommended for local dev): run the included script to create the role and database idempotently:

``` bash
./run-local.sh   # interactive

# or non-interactive:
DB_USER=overlook_user DB_PASS=overlook_pass DB_NAME=overlook_hotel ./run-local.sh --no-prompt

```
The script will create the role and database if they don't exist and grant privileges.

**Option B** — manual:

Switch to the `postgres` user (Linux/macOS):

``` bash
sudo -i -u postgres
```

Then run:

``` sql
-- Create application user
CREATE ROLE overlook_user WITH LOGIN PASSWORD 'overlook_pass';

-- Create database owned by the user
CREATE DATABASE overlook_hotel OWNER overlook_user;

-- Optional: grant privileges explicitly
GRANT ALL PRIVILEGES ON DATABASE overlook_hotel TO overlook_user;
```

Exit:

``` bash
\q
exit
```

#### 4.3 Initialise Schema & (Optional) Seed Data

Two approaches:

- Let Spring create schema automatically (dev only): enable the dev profile (see YAML below) and run the app — Spring JPA will create/drop schema when SPRING_PROFILES_ACTIVE=dev and ddl-auto=create-drop.

- Apply schema manually for controlled initialization:

    ``` bash
    PGPASSWORD="${DB_PASSWORD:-overlook_pass}" psql -U ${DB_USER:-overlook_user} -d ${DB_NAME:-overlook_hotel} -f db/schema.sql
    ```
    (Optional) Load demo data:

    ```bash
    ./seed-db.sh
    # or
    PGPASSWORD="${DB_PASSWORD:-overlook_pass}" psql -U ${DB_USER:-overlook_user} -d ${DB_NAME:-overlook_hotel} -f db/seed-data.sql
    ```

### 5. ⚙️ Configure the Application (YAML)

This project includes YAML configuration. Copy and edit environment-specific values if needed.

Base config: src/main/resources/application.yml (already provided in repo). It reads DB credentials from environment variables when available.

Development profile: src/main/resources/application-dev.yml enables JPA auto DDL for local development. Activate it locally with:

```bash
export SPRING_PROFILES_ACTIVE=dev
```
**Important:**

- Do not use `create/create-drop` in production. Use `ddl-auto=validate` in production and a migration tool (Flyway/Liquibase) or CI-applied SQL migrations.
- Keep `APP_JWT_SECRET` out of source control — set it via environment variables or your secrets manager.

### 6. 🚀 Build and Run

Build: 
``` bash
mvn clean package -DskipTests
```

Run (dev — app will create schema if dev profile active):

``` bash 
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
# or
java -jar target/overlook-hotel.jar
```

Open: http://localhost:8080

### 7. 🌱 Seeded Demo Accounts

If you imported `db/seed-data.sql`, these demo credentials are created:

    Manager
        email: manager@overlook.test
        password: Password123!
    Employee
        email: employee@overlook.test
        password: Password123!
    Guests
        email: guest1@overlook.test
        mot de passe: Password123!
        email: guest2@overlook.test
        password: Password123!

Change these after first login in a real environment.

### 8. 🔍 Running Tests

Unit & integration tests:

``` bash
mvn test
```

### 9. 📬 Postman Collection

A Postman collection is included at `postman/OverlookHotel.postman_collection.json`. Import it to exercise API flows (register → login → search rooms → create reservation → cancel).

### 10. 📈 Logging & Health

- Actuator endpoints (if enabled): `/actuator/health`, `/actuator/metrics`
- Logs are written to console. Configure file logging in `application.properties` or add logback config.

### 11. 💾 Backup & Restore

Manual backup (pg_dump):

``` bash
pg_dump -U overlook_user -Fc -f overlook_hotel.dump overlook_hotel
```

Restore:

``` bash
pg_restore -U overlook_user -d overlook_hotel -c overlook_hotel.dump
```

### 12. ⚠️ Troubleshooting

- DB connection errors: verify environment variables (`DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD`), PostgreSQL service, and network/firewall.
- Port conflicts: change `server.port` in `application.yml`.
- JWT errors: ensure `APP_JWT_SECRET`is set and consistent between runs.

---

## Small files included in repo

- `run-local.sh` — idempotent script to create role & DB (use --no-prompt for unattended runs)
- `seed-db.sh` — applies `db/seed-data.sql` if present
- `env.example` & `environment.properties.example` — example files for team setup
- `application.yml` & `application-dev.yml` — base and dev profile YAML configuration


🎉 That’s it — ready for local development! If you encounter any issues or have suggestions, feel free to contribute or report them! 🎉

---
---

#  🇫🇷 FR: Overlook Hotel
#### (🇬🇧 English above)

Une plateforme de gestion hôtelière sécurisée et basée sur des rôles.

Les clients de l'hôtel peuvent découvrir et réserver des chambres et des espaces d'événements. Le personnel et les gestionnaires peuvent effectuer les opérations hôtelières essentielles.

## 🛠 Configuration

### 1. ✅ Prérequis

Avant de commencer, assurez-vous d'avoir les logiciels suivants installés :

- **Java 17 (ou version ultérieure)**
- **Apache Maven**
- **PostgreSQL 12+**
- **Git**

### 2. 📦 Cloner le Répertoire

``` bash
git clone https://github.com/jerome-cossu/overlook_hotel.git
cd overlook_hotel
```

### 3. 🗄️ Configurer la Base de Données

#### 3.1. Démarrer PostgreSQL

MacOS (Homebrew) :

``` bash
brew services start postgresql
```

Linux (systemd) :

``` bash
sudo systemctl start postgresql
```

Windows :

Démarrez le service “PostgreSQL”, ou lancez pgAdmin / SQL Shell (psql).

#### 3.2. Créer un Utilisateur et une Base de Données

Passez à l'utilisateur `postgres` (Linux/macOS) :

``` bash
sudo -i -u postgres
```

Exécutez ces commandes SQL (remplacez les noms/mots de passe si nécessaire) :

``` sql
-- Créer un utilisateur d'application
CREATE ROLE overlook_user WITH LOGIN PASSWORD 'overlook_pass';

-- Créer une base de données appartenant à l'utilisateur
CREATE DATABASE overlook_hotel OWNER overlook_user;

-- Optionnel : accorder des privilèges explicitement
GRANT ALL PRIVILEGES ON DATABASE overlook_hotel TO overlook_user;
```

Quittez :

``` bash
\q
exit
```

#### 3.3 Initialiser le Schéma & (Optionnel) Charger des Données

Maintenant que votre base de données et votre utilisateur existent, appliquez le SQL de schéma inclus dans `db/schema.sql` :

``` bash 
psql -U overlook_user -d overlook_hotel -f db/schema.sql
```
(Optionnel) Chargez des données de démonstration et un utilisateur administrateur :

``` bash
psql -U overlook_user -d overlook_hotel -f db/seed-data.sql
```

### 4. ⚙️ Configurer l'Application

Copiez le fichier de propriétés d'exemple :

``` bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```
Ouvrez `src/main/resources/application.properties` et définissez au minimum :

``` properties
# Source de données Spring
spring.datasource.url=jdbc:postgresql://localhost:5432/overlook_hotel
spring.datasource.username=overlook_user
spring.datasource.password=overlook_pass
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.show-sql=false

# JWT / Sécurité
app.jwt.secret=change_this_to_a_strong_secret
app.jwt.access-token-expiry-minutes=15
app.jwt.refresh-token-expiry-days=7

# Serveur
server.port=8080
```

**Remarques :**

- Utilisez une valeur aléatoire forte pour `app.jwt.secret` dans tout environnement non-démonstration.
- Pour le développement uniquement, vous pouvez définir `spring.jpa.hibernate.ddl-auto=create-drop` pour recréer automatiquement le schéma.

### 5. 🚀 Construire et Exécuter

Construire :
``` bash
mvn clean package -DskipTests
```

Exécuter :

``` bash 
mvn spring-boot:run
# ou
java -jar target/overlook-hotel.jar
```

Ouvrir : http://localhost:8080

### 6. 🌱 Comptes de Démonstration Préchargés

Si vous avez importé `db/seed-data.sql`, ces identifiants de démonstration sont créés :

    Gestionnaire
        email: manager@overlook.test
        mot de passe: Password123!
    Employé
        email: employee@overlook.test
        mot de passe: Password123!
    Invités
        email: guest1@overlook.test
        mot de passe: Password123!
        email: guest2@overlook.test
        mot de passe: Password123!

Changez ces identifiants après la première connexion dans un environnement réel.

### 7. 🔍 Exécution des Tests

Tests unitaires et d'intégration :

``` bash
mvn test
```

### 8. 📬 Collection Postman

Une collection Postman est incluse dans `postman/OverlookHotel.postman_collection.json`. Importez-la pour tester les flux API (enregistrer → se connecter → rechercher des chambres → créer une réservation → annuler).

### 9. 📈 Journalisation & Santé

- Points de terminaison de l'Actuator (si activés) : `/actuator/health`, `/actuator/metrics`
- Les journaux sont écrits dans la console. Configurez la journalisation dans un fichier dans `application.properties` ou ajoutez une configuration logback.


### 10. 💾 Sauvegarde & Restauration

Sauvegarde manuelle (pg_dump) :

``` bash
pg_dump -U overlook_user -Fc -f overlook_hotel.dump overlook_hotel
```

Restauration :

``` bash
pg_restore -U overlook_user -d overlook_hotel -c overlook_hotel.dump
```

### 11. ⚠️ Dépannage

Erreurs de connexion à la base de données : vérifiez les valeurs `spring.datasource.*`, le service PostgreSQL et les paramètres réseau/pare-feu.

Conflits de port : changez `server.port` dans les propriétés.

Erreurs JWT : assurez-vous que `app.jwt.secret` est défini et cohérent entre les exécutions.

---

🎉 Vous êtes prêt ! Connectez-vous avec l'administrateur préchargé (si vous avez exécuté `seed-data.sql`), et commencez à gérer votre hôtel. Si vous rencontrez des problèmes ou avez des suggestions, n'hésitez pas à contribuer ou à les signaler ! 🎉
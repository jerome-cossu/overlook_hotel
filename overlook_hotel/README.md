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

JWT_SECRET=change_this_to_a_strong_secret
SPRING_PROFILES_ACTIVE=dev
```
Or export in POSIX shell:

``` bash
export DB_USER=overlook_user
export DB_PASSWORD=overlook_pass
export DB_NAME=overlook_hotel
export DB_HOST=localhost
export DB_PORT=5432
export JWT_SECRET=change_this_to_a_strong_secret
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


**(optional) if you get "permission denied" when running the repository scripts, run:*

```bash
# Make all included scripts executable
chmod +x run-local.sh seed-db.sh

# Or make a single script executable
chmod +x run-local.sh
```

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
- Keep `JWT_SECRET` out of source control — set it via environment variables or your secrets manager.

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
- Logs are written to console. Configure file logging in `application.yml` or add logback config.

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
- JWT errors: ensure `JWT_SECRET`is set and consistent between runs.

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

## Fonctionnalités clés

⭐ Accès basé sur les rôles — différents niveaux d'accès pour les clients, le personnel et les gestionnaires.

⭐ Réservation de chambres et d'espaces — les clients peuvent rechercher et réserver des hébergements et des salles d'événements.

⭐ Opérations principales — le personnel peut gérer les réservations, les enregistrements (check-in) et autres opérations hôtelières.

⭐ Sécurité — utilise JWT pour une authentification et une autorisation sécurisées.

## 🛠 Installation & Configuration

### 1. ✅ Prérequis

Avant de commencer, assurez-vous d'avoir les logiciels suivants installés :

- **Java 17** (ou version ultérieure)
- **Apache Maven**
- **PostgreSQL 12+**
- **Git**
- **psql** (client PostgreSQL) — généralement installé avec PostgreSQL

### 2. 📦 Cloner le Répertoire

``` bash
git clone https://github.com/jerome-cossu/overlook_hotel.git
cd overlook_hotel
```
### 3. Variables d'environnement (recommandé)

Pour garder les secrets hors du contrôle de version, utilisez des variables d'environnement. Un fichier exemple est fourni sous `env.example` et `environment.properties.example` — copiez-en un vers `.env` ou `environment.properties` (selon votre workflow) et modifiez les valeurs localement.

Créez un `.env` local (optionnel, si vous utilisez un outil pour charger `.env`) ou exportez les variables dans votre shell :

`.env` (exemple — NE PAS committer de vrais secrets)

``` properties
DB_USER=overlook_user
DB_PASSWORD=overlook_pass
DB_NAME=overlook_hotel
DB_HOST=localhost
DB_PORT=5432

JWT_SECRET=change_this_to_a_strong_secret
SPRING_PROFILES_ACTIVE=dev
```


Ou exportez dans un shell POSIX :

``` bash
export DB_USER=overlook_user
export DB_PASSWORD=overlook_pass
export DB_NAME=overlook_hotel
export DB_HOST=localhost
export DB_PORT=5432
export JWT_SECRET=change_this_to_a_strong_secret
export SPRING_PROFILES_ACTIVE=dev
```
PowerShell (Windows) :

``` powershell
$env:DB_PASSWORD = "overlook_pass"
$env:SPRING_PROFILES_ACTIVE = "dev"
```

### 4. 🗄️ Configurer la Base de Données

#### 4.1. Démarrer PostgreSQL

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

#### 4.2. Créer un Utilisateur et une Base de Données (automatique ou manuel)

**Option A** — automatique (recommandé pour le développement local) : exécutez le script inclus pour créer le rôle et la base de données de manière idempotente :

``` bash
./run-local.sh   # interactif

# ou non interactif :
DB_USER=overlook_user DB_PASS=overlook_pass DB_NAME=overlook_hotel ./run-local.sh --no-prompt

```
Le script créera le rôle et la base de données s'ils n'existent pas et accordera les privilèges.

**(optionnel) Si vous obtenez "permission denied" en exécutant les scripts du dépôt, exécutez :*

```bash
# Rendre tous les scripts inclus exécutables
chmod +x run-local.sh seed-db.sh

# Ou rendre un seul script exécutable
chmod +x run-local.sh
```

**Option B** — manual:

Basculez vers l'utilisateur `postgres` (Linux/macOS) :

``` bash
sudo -i -u postgres
```

Puis exécutez :

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

#### 4.3 Initialiser le Schéma & (Optionnel) Charger des Données

Deux approches :

- Laisser Spring créer le schéma automatiquement (dev seulement) : activez le profil dev (voir YAML ci-dessous) et lancez l'application — Spring JPA créera/supprimera le schéma quand SPRING_PROFILES_ACTIVE=dev et ddl-auto=create-drop.

- Appliquer le schéma manuellement pour une initialisation contrôlée :

``` bash 
PGPASSWORD="${DB_PASSWORD:-overlook_pass}" psql -U ${DB_USER:-overlook_user} -d ${DB_NAME:-overlook_hotel} -f db/schema.sql
```
(Optionnel) Charger des données de démonstration :

``` bash
./seed-db.sh
# ou
PGPASSWORD="${DB_PASSWORD:-overlook_pass}" psql -U ${DB_USER:-overlook_user} -d ${DB_NAME:-overlook_hotel} -f db/seed-data.sql
```

### 5. ⚙️ Configurer l'Application (YAML)

Le projet inclut des configurations YAML. Copiez et modifiez les valeurs spécifiques à l'environnement si nécessaire.

Configuration de base : src/main/resources/application.yml (déjà fournie dans le dépôt). Elle lit les identifiants DB depuis les variables d'environnement lorsqu'elles sont disponibles.

Profil de développement : src/main/resources/application-dev.yml active l'auto-DDL JPA pour le développement local. Activez-le localement avec :

``` bash
export SPRING_PROFILES_ACTIVE=dev
```
**Important :**

- N'utilisez pas `create/create-drop` en production. Utilisez `ddl-auto=validate` en production et un outil de migration (Flyway/Liquibase) ou des migrations SQL appliquées par le CI.
- Gardez `JWT_SECRET` hors du contrôle de version — définissez-le via des variables d'environnement ou votre gestionnaire de secrets.

### 6. 🚀 Construire et Exécuter

Construire :
``` bash
mvn clean package -DskipTests
```

Exécuter :

``` bash 
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
# ou
java -jar target/overlook-hotel.jar
```

Ouvrir : http://localhost:8080

### 7. 🌱 Comptes de Démonstration Préchargés

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

Changez-les après la première connexion en environnement réel.

### 8. 🔍 Exécution des Tests

Tests unitaires et d'intégration :

``` bash
mvn test
```

### 9. 📬 Collection Postman

Une collection Postman est incluse dans `postman/OverlookHotel.postman_collection.json`. Importez-la pour tester les flux API (enregistrer → se connecter → rechercher des chambres → créer une réservation → annuler).

### 10. 📈 Journalisation & Santé

- Points de terminaison de l'Actuator (si activés) : `/actuator/health`, `/actuator/metrics`
- Les journaux sont écrits dans la console. Configurez la journalisation dans un fichier dans `application.yml` ou ajoutez une configuration logback.


### 11. 💾 Sauvegarde & Restauration

Sauvegarde manuelle (pg_dump) :

``` bash
pg_dump -U overlook_user -Fc -f overlook_hotel.dump overlook_hotel
```

Restauration :

``` bash
pg_restore -U overlook_user -d overlook_hotel -c overlook_hotel.dump
```

### 12. ⚠️ Dépannage

- Erreurs de connexion DB : vérifiez les variables d'environnement (`DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD`), le service PostgreSQL et le réseau/pare-feu.
- Conflits de ports : changez `server.port` dans `application.yml`.
- Erreurs JWT : assurez-vous que `JWT_SECRET` est défini et cohérent entre les exécutions.

## Petits fichiers inclus dans le dépôt

- `run-local.sh` — script idempotent pour créer le rôle et la base (utilisez --no-prompt pour exécution non interactive)
- `seed-db.sh` — applique `db/seed-data.sql` si présent
- `env.example` & `environment.properties.example` — fichiers d'exemple pour la configuration de l'équipe
- `application.yml` & `application-dev.yml` — configuration de base et profil de développement en YAML

---

🎉 C'est tout — prêt pour le développement local ! Si vous rencontrez des problèmes ou avez des suggestions, n'hésitez pas à contribuer ou à les signaler ! 🎉
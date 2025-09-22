# 🏨 Overlook Hotel
#### (🇫🇷 Français ci-dessous)

A secure, role-based hotel management platform. 

Hotel guests can discover and book rooms and event spaces. 
Staff and managers can perform core hotel operations.

## 🛠 Setup

### 1. ✅ Prerequisites

Before you begin, make sure you have the following software installed:

- Java 17 (or later)  
- Apache Maven
- PostgreSQL 12+  
- Git

### 2. 📦 Clone the Repository

``` bash
git clone https://github.com/jerome-cossu/overlook_hotel.git
cd overlook_hotel
```

### 3. 🗄️ Configure the Database

#### 3.1. Start PostgreSQL

MacOS (Homebrew):

``` bash
brew services start postgresql
```

Linux (systemd):

``` bash
sudo systemctl start postgresql
```

Windows:

Start “PostgreSQL” service, or launch pgAdmin / SQL Shell (psql).

#### 3.2. Create a Database User & Database

Switch to the `postgres` user (Linux/macOS):

``` bash
sudo -i -u postgres
```

Run these SQL commands (replace names/passwords as needed):

``` sql
-- Create application user
CREATE ROLE overlook_user WITH LOGIN PASSWORD 'overlook_pass';

-- Create database owned by the user
CREATE DATABASE overlook_db OWNER overlook_user;

-- Optional: grant privileges explicitly
GRANT ALL PRIVILEGES ON DATABASE overlook_db TO overlook_user;
```

Exit:

``` bash
\q
exit
```

#### 3.3 Initialise Schema & (Optional) Seed Data

Now that your database and user exist, apply schema SQL included in `db/schema.sql`:

``` bash 
psql -U overlook_user -d overlook_db -f db/schema.sql
```
(Optional) Load demo data and seeded admin user:

``` bash
psql -U overlook_user -d overlook_db -f db/seed-data.sql
```

### 4. ⚙️ Configure the Application

Copy the example properties file:

``` bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```
Open `src/main/resources/application.properties` and set at minimum:

``` properties
# Spring datasource
spring.datasource.url=jdbc:postgresql://localhost:5432/overlook_db
spring.datasource.username=overlook_user
spring.datasource.password=overlook_pass
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.show-sql=false

# JWT / Security
app.jwt.secret=change_this_to_a_strong_secret
app.jwt.access-token-expiry-minutes=15
app.jwt.refresh-token-expiry-days=7

# Server
server.port=8080
```

**Notes:**
- Use a strong random value for `app.jwt.secret` in any non-demo environment.
- For development only you can set `spring.jpa.hibernate.ddl-auto=create-drop` to recreate schema automatically.

### 5. 🚀 Build and Run

Build: 
``` bash
mvn clean package -DskipTests
```

Run:

``` bash 
mvn spring-boot:run
# or
java -jar target/overlook-hotel.jar
```

Open: http://localhost:8080

### 6. 🌱 Seeded Demo Accounts

If you imported `db/seed-data.sql`, these demo credentials are created:

    Manager
        email: manager@overlook.test
        password: Password123!
    Employee
        email: employee@overlook.test
        password: Password123!
    Guest
        email: guest@overlook.test
        password: Password123!

Change these after first login in a real environment.

### 7. 🔍 Running Tests

Unit & integration tests:

``` bash
mvn test
```

### 8. 📬 Postman Collection

A Postman collection is included at `postman/OverlookHotel.postman_collection.json`. Import it to exercise API flows (register → login → search rooms → create reservation → cancel).

### 9. 📈 Logging & Health

- Actuator endpoints (if enabled): `/actuator/health`, `/actuator/metrics`
- Logs are written to console. Configure file logging in `application.properties` or add logback config.

### 10. 💾 Backup & Restore

Manual backup (pg_dump):

``` bash
pg_dump -U overlook_user -Fc -f overlook_db.dump overlook_db
```

Restore:

``` bash
pg_restore -U overlook_user -d overlook_db -c overlook_db.dump
```

### 11. ⚠️ Troubleshooting

DB connection errors: verify `spring.datasource.*` values, PostgreSQL service, and network/firewall.

Port conflicts: change `server.port` in properties.

JWT errors: ensure `app.jwt.secret` is set and consistent between runs.

---

🎉 You’re all set! Log in with the seeded admin (if you ran `seed-data.sql`), and start managing your hotel. If you encounter any issues or have suggestions, feel free to contribute or report them! 🎉

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
CREATE DATABASE overlook_db OWNER overlook_user;

-- Optionnel : accorder des privilèges explicitement
GRANT ALL PRIVILEGES ON DATABASE overlook_db TO overlook_user;
```

Quittez :

``` bash
\q
exit
```

#### 3.3 Initialiser le Schéma & (Optionnel) Charger des Données

Maintenant que votre base de données et votre utilisateur existent, appliquez le SQL de schéma inclus dans `db/schema.sql` :

``` bash 
psql -U overlook_user -d overlook_db -f db/schema.sql
```
(Optionnel) Chargez des données de démonstration et un utilisateur administrateur :

``` bash
psql -U overlook_user -d overlook_db -f db/seed-data.sql
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
spring.datasource.url=jdbc:postgresql://localhost:5432/overlook_db
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
    Invité
        email: guest@overlook.test
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
pg_dump -U overlook_user -Fc -f overlook_db.dump overlook_db
```

Restauration :

``` bash
pg_restore -U overlook_user -d overlook_db -c overlook_db.dump
```

### 11. ⚠️ Dépannage

Erreurs de connexion à la base de données : vérifiez les valeurs `spring.datasource.*`, le service PostgreSQL et les paramètres réseau/pare-feu.

Conflits de port : changez `server.port` dans les propriétés.

Erreurs JWT : assurez-vous que `app.jwt.secret` est défini et cohérent entre les exécutions.

---

🎉 Vous êtes prêt ! Connectez-vous avec l'administrateur préchargé (si vous avez exécuté `seed-data.sql`), et commencez à gérer votre hôtel. Si vous rencontrez des problèmes ou avez des suggestions, n'hésitez pas à contribuer ou à les signaler ! 🎉
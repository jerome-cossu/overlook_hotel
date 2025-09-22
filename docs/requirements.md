# 🏨 Overlook Hotel
#### (🇫🇷 Français ci-dessous)
 
Last updated: 2025-09-22

---

## 1. Project Overview

Build a secure, role-based hotel management platform that let guests to discover and book rooms and event spaces, and let staff and managers perform core operations (rooms, reservations, facilities/events, feedback, loyalty, notifications, and basic reports)

**Technology stack**
- Backend: **Spring Boot**, **Spring Security**, **JPA/Hibernate**
- Frontend: Server-side **Thymeleaf**
- Database: **PostgreSQL**
- Build: **Maven**
- API testing: **Postman**

**Note:** This is an educational project for learning purposes; we are not planning to deploy on Docker or integrate payments. 

---

### 1.1 Project Goals

- Deliver a Minimum Viable Product (MVP) within 3 weeks demonstrating core booking and management flows.
- Role-based access control for: **GUEST**, **EMPLOYEE**, **MANAGER**.
- Prevent obvious double-booking and maintain data integrity.
- Provide analytics and reporting for managers.
- Include seed/demo data and a Postman collection.
- Provide README with setup and run instructions 

### 1.2 Non‑functional Requirements

- Authentication: JWT-based auth.
- Passwords: hashed (bcrypt/Argon2).
- Maintainability: keep the code modular and testable with layered architecture (controller → service → repository).
- Observability: logs and basic health check endpoint.
- Simple deployment: run locally with PostgreSQL (or H2 for tests/dev).

### 1.3 Authentication & Authorization

- **Authentication**: JWT tokens.
- **Authorization**: Role-Based Access Control (RBAC). Roles: **GUEST**, **EMPLOYEE**, **MANAGER**.
- Secure password management (bcrypt or Argon2, salt).
- *Email verification (optional).*
- Password reset using time-limited token (simple implementation OK).

---

## 2. Actors & Capabilities

Summary of roles:
- Hotel Guest — browsing, bookings, loyalty, feedback.
- Hotel Employee — operational tasks, room status, check-in/out, inventory.
- Hotel Manager/Administrator — full management: users, rooms, events, reports, notifications.

---

## 3. Hotel Guest (User stories & details)

### Account & Authentication
- Register, sign in/out, password reset, email verification.
- Manage notification preferences (email/SMS/push).

### Profile Management
- View/update personal info: first name, surname, email, phone, address, DOB.
- Optional: Upload profile photo (with size limits).

### Search & Availability
- Search rooms with filters: date range, capacity, room type, price range, amenities, accessibility features.
- Real-time availability check.
- Room detail page: photos, amenities, description, bed types, floor, policies, cancellation policy, occupancy limit, price.

### Events & Facilities
- Calendar view of events and facility availability: start/end, capacity, price, event type, registered attendees count.
- Filter by facility/event type (spa, gym, fitness class, conference room, function room for weddings and special occasions etc.).
- Book facilities/events by time slot (capacity-managed).

### Reservation Management
- Create reservation with guest info, optional add-ons (breakfast, late checkout).
- Modify/cancel per cancellation policy (policy attached to rate type/reservation).
- View reservation history and upcoming stays.
- Confirmation notifications (email/SMS)

### Loyalty Points
- View points, earn rules & expiry, redemption during booking, transaction history.

### Feedback & Ratings
- Rate stay 1–5 and leave comments after checkout time.
- Guests can view hotel responses to feedback.

---

## 4. Hotel Employee (User stories & details)

### Authentication & Profile
- Sign in/out, reset password, update profile.
- Role-scoped permissions (e.g., front-desk vs housekeeping vs maintenance).

### Shifts & Scheduling
- View personal schedule and upcoming shifts.
- Request time off / swap shifts (requests route to manager).
- Track hours worked (basic timesheet).

### Training & Resources
- Access hotel's standard operating procedures, training docs, certifications, and knowledge base.

### Room Operations
- Search rooms by room number, type, status (clean, dirty, occupied, maintenance).
- Update room status in real time and add shift notes (cleaning comments, maintenance issues).
- Assign rooms to housekeeping shifts.

### Check-in / Check-out
- Perform check-in (verify ID, confirm payment, assign keys.)
- Perform check-out (final charges, release room).

### Inventory Management
- Track inventory (linens, consumables), record usage, alerts for low stock, reorder triggers (manual or automatic).

### Reservations View
- Search and view reservations by guest name/ID/date (read-only unless permitted).
- Flag problematic reservations.

### Messages & Notifications
- Read notifications from managers, send short replies, confirm tasks (e.g., room cleaned).

---

## 5. Hotel Manager / Administrator (User stories & details)

### Account & Security
- Management login with elevated permissions.
- Manage user accounts: create/edit/delete, assign roles, reset passwords.

### Rooms & Rate Management
- CRUD for rooms (number, type, capacity, price, amenities, status)
- *Optionally manage rates and rate plans (base price, seasonal adjustments, promotions). If time permits.*

### Events & Facilities
- CRUD events/facilities; schedule events with capacity and booking windows.
- Configure facility-specific rules (cancellation, setup time, resources).

### Reservation Administration
- Full view of reservations; create/modify/cancel on behalf of guests.
- Resolve conflicts, force release rooms when appropriate.

### Customer & Employee Management
- CRUD operations for customer and employee accounts.
- Employee scheduling: create shifts, assign employees, approve/deny shift/leave requests.
- Manage training and performance notes.

### Notifications & Communication
- Configure notification templates (email/SMS) and channels.
- Broadcast messages/announcements to employees.
- Respond to guest feedback.

### Reporting & Analytics
- Dashboard KPIs: **occupancy rate**, **number of reservations**, **average rating**, **revenue**, **customer retention /loyalty uptake**.
- Filterable time ranges: day/week/month/year.
- Export reports (CSV, PDF) and scheduled report generation (daily/weekly) **optional, if time*.

### Audit & Logs
- View audit trails for critical operations (who changed reservation, who adjusted rates).

---

## Local Setup

- Requirements: Java 17+, Maven, PostgreSQL
- Steps
  1. Install and run PostgreSQL locally.
  2. Create DB and configure application.properties with DB credentials.
  3. Run schema SQL in repo.
  4. (optionally) run seed-data SQL for demo data.
  5. `mvn spring-boot:run`
  6. Use Postman or browser to exercise endpoints.


---

## Data model / Entities (ERD)

--- 

## API Design

---

## Security

--- 

## Data Integrity & Concurrency

---

## Validation & error handling

---

## Testing plan

- Unit tests for services and core booking logic.
- Integration tests using a test Postgres instance or H2.
- Manual testing documented via Postman collection &example flows (create user → search → book → cancel)..
- Basic test cases: successful booking, prevent double-booking, cancel booking, room status updates.

---

## CI / Build

- Simple CI: GitHub Actions that runs mvn test and builds the JAR.

## Seed data & demo flows

Seed script with:

Roles: GUEST, EMPLOYEE, MANAGER

Users:
    manager@overlook.test / Password123! (MANAGER)
    employee@overlook.test / Password123! (EMPLOYEE)
    guest1@overlook.test / Password123! (GUEST)
    guest2@overlook.test / Password123! (GUEST)

Rooms: 101 (Single, cap 1, 80), 102 (Double, cap 2, 120), 201 (Suite, cap 4, 300)

Reservations: one sample reservation in the past for rating flow

Provide these credentials in README and Postman environment.

---

## Thymeleaf pages

- **Public:** landing, rooms search, room details, register/login
- **Guest:** dashboard (reservations), reservation detail, profile
- **Employee:** reservations list, room status update
- **Manager:** admin dashboard, room CRUD, reports

---

## Key Workflows

---

## Reporting & CSV export


---

## Observability & logging

- Log format (JSON for easier parsing), log levels per package.
- Health endpoints: /actuator/health, /actuator/metrics (Spring Actuator).


## Documentation

- README with prerequisites, setup, run, and how to run tests.
- Postman collection + environment file.

---

## Project Management / Roadmap

- **Short term:** core booking (search, create, cancel), room CRUD, authentication, basic dashboards, seed data, Postman collection, README, core tests.

- **Medium term:** events/facilities, loyalty, basic reporting, employee scheduling, inventory.

---

## Optional Extensions (if time permits)

- waiting list for events 
---
---

# 🏨 Hôtel Overlook
#### (🇬🇧 English above)

Dernière mise à jour : 2025-09-22

---

## 1. Vue d'ensemble du projet

Construire une plateforme de gestion hôtelière sécurisée et basée sur des rôles qui permet aux clients de découvrir et de réserver des chambres et des espaces d'événements, et permet au personnel et aux gestionnaires d'effectuer des opérations essentielles (chambres, réservations, installations/événements, retours, fidélité, notifications et rapports de base).

**Technologies utilisées**
- Backend : **Spring Boot**, **Spring Security**, **JPA/Hibernate**
- Frontend : **Thymeleaf** côté serveur
- Base de données : **PostgreSQL**
- Build : **Maven**
- Test d'API : **Postman**

**Remarque :** Il s'agit d'un projet éducatif à des fins d'apprentissage ; nous ne prévoyons pas de déployer sur Docker ou d'intégrer des paiements.

---

### 1.1 Objectifs du projet

- Livrer un Produit Minimum Viable (MVP) dans les 3 semaines démontrant les flux de réservation et de gestion essentiels.
- Contrôle d'accès basé sur les rôles pour : **CLIENT**, **EMPLOYÉ**, **GESTIONNAIRE**.
- Prévenir les doubles réservations évidentes et maintenir l'intégrité des données.
- Fournir des analyses et des rapports pour les gestionnaires.
- Inclure des données de démonstration et une collection Postman.
- Fournir un README avec des instructions de configuration et d'exécution.

### 1.2 Exigences non fonctionnelles

- Authentification : authentification basée sur JWT.
- Mots de passe : hachés (bcrypt/Argon2).
- Maintenabilité : garder le code modulaire et testable avec une architecture en couches (contrôleur → service → référentiel).
- Observabilité : journaux et point de contrôle de santé de base.
- Déploiement simple : exécuter localement avec PostgreSQL (ou H2 pour les tests/développement).

### 1.3 Authentification et autorisation

- **Authentification** : jetons JWT.
- **Autorisation** : Contrôle d'Accès Basé sur les Rôles (RBAC). Rôles : **CLIENT**, **EMPLOYÉ**, **GESTIONNAIRE**.
- Gestion sécurisée des mots de passe (bcrypt ou Argon2, sel).
- *Vérification par e-mail (optionnel).*
- Réinitialisation du mot de passe à l'aide d'un jeton à durée limitée (implémentation simple OK).

---

## 2. Acteurs et capacités

Résumé des rôles :
- Client d'hôtel — navigation, réservations, fidélité, retours.
- Employé d'hôtel — tâches opérationnelles, état des chambres, enregistrement/départ, inventaire.
- Gestionnaire/Administrateur d'hôtel — gestion complète : utilisateurs, chambres, événements, rapports, notifications.

---

## 3. Client d'hôtel (Histoires d'utilisateur et détails)

### Compte et authentification
- Inscription, connexion/déconnexion, réinitialisation de mot de passe, vérification par e-mail.
- Gérer les préférences de notification (e-mail/SMS/push).

### Gestion de profil
- Voir/mettre à jour les informations personnelles : prénom, nom, e-mail, téléphone, adresse, date de naissance.
- Optionnel : Télécharger une photo de profil (avec des limites de taille).

### Recherche et disponibilité
- Rechercher des chambres avec des filtres : plage de dates, capacité, type de chambre, fourchette de prix, équipements, caractéristiques d'accessibilité.
- Vérification de la disponibilité en temps réel.
- Page de détails de la chambre : photos, équipements, description, types de lits, étage, politiques, politique d'annulation, limite d'occupation, prix.

### Événements et installations
- Vue calendrier des événements et de la disponibilité des installations : début/fin, capacité, prix, type d'événement, nombre de participants inscrits.
- Filtrer par type d'installation/événement (spa, salle de sport, cours de fitness, salle de conférence, salle de fonction pour mariages et occasions spéciales, etc.).
- Réserver des installations/événements par créneau horaire (capacité gérée).

### Gestion des réservations
- Créer une réservation avec les informations du client, options supplémentaires (petit-déjeuner, départ tardif).
- Modifier/annuler selon la politique d'annulation (politique attachée au type de tarif/réservation).
- Voir l'historique des réservations et les séjours à venir.
- Notifications de confirmation (e-mail/SMS).

### Points de fidélité
- Voir les points, règles d'acquisition et d'expiration, échange lors de la réservation, historique des transactions.

### Retours et évaluations
- Évaluer le séjour de 1 à 5 et laisser des commentaires après l'heure de départ.
- Les clients peuvent voir les réponses de l'hôtel aux retours.

---

## 4. Employé d'hôtel (Histoires d'utilisateur et détails)

### Authentification et profil
- Connexion/déconnexion, réinitialisation de mot de passe, mise à jour du profil.
- Permissions basées sur les rôles (par exemple, réception vs ménage vs maintenance).

### Horaires et planification
- Voir le planning personnel et les prochains quarts de travail.
- Demander des congés / échanger des quarts (les demandes sont envoyées au gestionnaire).
- Suivre les heures travaillées (feuille de temps de base).

### Formation et ressources
- Accéder aux procédures opérationnelles standard de l'hôtel, documents de formation, certifications et base de connaissances.

### Opérations de chambre
- Rechercher des chambres par numéro de chambre, type, état (propre, sale, occupée, maintenance).
- Mettre à jour l'état des chambres en temps réel et ajouter des notes de quart (commentaires de nettoyage, problèmes de maintenance).
- Assigner des chambres aux quarts de ménage.

### Enregistrement / Départ
- Effectuer l'enregistrement (vérifier l'identité, confirmer le paiement, attribuer des clés).
- Effectuer le départ (frais finaux, libérer la chambre).

### Gestion des stocks
- Suivre l'inventaire (linge, consommables), enregistrer l'utilisation, alertes pour faible stock, déclencheurs de réapprovisionnement (manuel ou automatique).

### Vue des réservations
- Rechercher et voir les réservations par nom/ID/date du client (lecture seule sauf autorisation).
- Signaler les réservations problématiques.

### Messages et notifications
- Lire les notifications des gestionnaires, envoyer de courtes réponses, confirmer des tâches (par exemple, chambre nettoyée).

---

## 5. Gestionnaire / Administrateur d'hôtel (Histoires d'utilisateur et détails)

### Compte et sécurité
- Connexion de gestion avec permissions élevées.
- Gérer les comptes utilisateurs : créer/modifier/supprimer, attribuer des rôles, réinitialiser les mots de passe.

### Gestion des chambres et des tarifs
- CRUD pour les chambres (numéro, type, capacité, prix, équipements, état).
- *Gérer éventuellement les tarifs et les plans tarifaires (prix de base, ajustements saisonniers, promotions). Si le temps le permet.*

### Événements et installations
- CRUD pour les événements/installations ; planifier des événements avec capacité et fenêtres de réservation.
- Configurer des règles spécifiques aux installations (annulation, temps de préparation, ressources).

### Administration des réservations
- Vue complète des réservations ; créer/modifier/annuler au nom des clients.
- Résoudre les conflits, libérer les chambres lorsque cela est approprié.

### Gestion des clients et des employés
- Opérations CRUD pour les comptes clients et employés.
- Planification des employés : créer des quarts, attribuer des employés, approuver/refuser les demandes de quart/congé.
- Gérer les notes de formation et de performance.

### Notifications et communication
- Configurer des modèles de notification (e-mail/SMS) et des canaux.
- Diffuser des messages/annonces aux employés.
- Répondre aux retours des clients.

### Reporting et analyses
- Tableau de bord des KPI : **taux d'occupation**, **nombre de réservations**, **note moyenne**, **revenu**, **rétention des clients / adoption de la fidélité**.
- Plages de temps filtrables : jour/semaine/mois/année.
- Exporter des rapports (CSV, PDF) et génération de rapports programmée (quotidienne/hebdomadaire) **optionnelle, si le temps le permet**.

### Audit et journaux
- Voir les pistes d'audit pour les opérations critiques (qui a changé la réservation, qui a ajusté les tarifs).

---

## Configuration locale

- Exigences : Java 17+, Maven, PostgreSQL
- Étapes
  1. Installer et exécuter PostgreSQL localement.
  2. Créer la base de données et configurer `application.properties` avec les identifiants de la base de données.
  3. Exécuter le SQL de schéma dans le dépôt. 
  4. (optionnel) exécuter le 'seed-data' SQL pour les données de démonstration.
  5. `mvn spring-boot:run`
  6. Utiliser Postman ou un navigateur pour tester les points de terminaison

---

## Modèle de données / Entités (ERD)

---

## Conception de l'API

---

## Sécurité

---

## Intégrité des données et concurrence

---

## Validation et gestion des erreurs

---

## Plan de test

- Tests unitaires pour les services et la logique de réservation principale.
- Tests d'intégration utilisant une instance de test PostgreSQL ou H2.
- Tests manuels documentés via la collection Postman et des flux d'exemple (créer un utilisateur → rechercher → réserver → annuler).
- Cas de test de base : réservation réussie, prévention des doubles réservations, annulation de réservation, mises à jour de l'état des chambres.

---

## CI / Build

- CI simple : GitHub Actions qui exécute `mvn test` et construit le JAR.

## Données de semence et flux de démonstration

Seed script with:

Roles: GUEST, EMPLOYEE, MANAGER

Users:
    manager@overlook.test / Password123! (MANAGER)
    employee@overlook.test / Password123! (EMPLOYEE)
    guest1@overlook.test / Password123! (GUEST)
    guest2@overlook.test / Password123! (GUEST)

Rooms: 101 (Single, cap 1, 80), 102 (Double, cap 2, 120), 201 (Suite, cap 4, 300)

Reservations: one sample reservation in the past for rating flow

Provide these credentials in README and Postman environment.

---

## Pages Thymeleaf

- **Public :** page d'accueil, recherche de chambres, détails de la chambre, inscription/connexion
- **Client :** tableau de bord (réservations), détails de la réservation, profil
- **Employé :** liste des réservations, mise à jour de l'état des chambres
- **Gestionnaire :** tableau de bord administrateur, CRUD des chambres, rapports

---

## Flux de travail clés

---

## Reporting et exportation CSV

---

## Observabilité et journalisation

- Format des journaux (JSON pour un meilleur parsing), niveaux de journalisation par paquet.
- Points de contrôle de santé : `/actuator/health`, `/actuator/metrics` (Spring Actuator).

## Documentation

- README avec les prérequis, la configuration, l'exécution et comment exécuter des tests.
- Collection Postman + fichier d'environnement.

---

## Gestion de projet / Feuille de route

- **Court terme :** réservation de base (recherche, création, annulation), CRUD des chambres, authentification, tableaux de bord de base, données de semence, collection Postman, README, tests de base.

- **Moyen terme :** événements/installations, fidélité, reporting de base, planification des employés, inventaire.

---

## Extensions optionnelles (si le temps le permet)



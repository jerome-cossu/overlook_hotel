# EN: Sprint plan (3 weeks, 3 devs) 
### (français ci-dessous)

## Week 1 — Core backend, auth, DB, basic frontend scaffolding
- Tasks (parallel):
    - Jerome & Emmanuelle: Project setup, Maven, Spring Boot, PostgreSQL config, entities & repositories.
    - Emmanuelle & Leila : Spring Security — JWT or session-based auth, role model (GUEST, EMPLOYEE, MANAGER), registration/login endpoints for customers.
    - Jerome & Leila: Thymeleaf skeleton, login/registration pages (customer + manager), basic navigation, shared CSS.
- Deliverables: DB schema implemented, registration/login flows working, basic Thymeleaf pages wired to backend.
## Week 2 — Rooms, bookings, customers, feedback, loyalty
- Tasks:
    - Dev A: Room CRUD API + manager UI for add/edit/delete, client UI to browse rooms.
    - Dev B: Reservation API (create/view/modify/cancel), booking UI for clients, manager reservation overview.
    - Dev C: Customer & employee CRUD APIs + manager UI; Feedback/rating API + client submit form; Loyalty points system (earned on completed stays).
- Deliverables: Room + booking workflows functional, feedback submission stored, loyalty points increment on completed bookings (simulate completed stays initially).

## Week 3 — Events/facilities, scheduling, analytics, polish, tests
- Tasks:
    - Dev A: Events & facilities APIs + manager UI to schedule; client view & event booking.
    - Dev B: Employee schedules/shifts API + HR UI; notifications (email or in-app stub).
    - Dev C: Analytics endpoints (occupancy, revenue, avg rating) + dashboard pages; QA, integration tests, Postman collection, finalize roles/permissions.
- Deliverables: All major features present, basic analytics dashboard, automated notification stub, documentation and Postman collection.

# FR : Plan Sprint (3 semaines, 3 dévs) 

## Semaine 1 — Backend principal, authentification, base de données, structure frontend de base

- Tâches (en parallèle) :
    - Jerome & Emmanuelle : configuration du projet, Maven, Spring Boot, configuration PostgreSQL, entités & répositories.
    - Emmanuelle & Leila : Spring Security — Authentification JWT ou basée sur les sessions, modèle de rôles (GUEST, EMPLOYEE, MANAGER), points de terminaison d'inscription/de connexion pour les clients.
    - Jerome & Leila : squelette Thymeleaf, pages de connexion/d'inscription (client + gestionnaire), navigation de base, CSS partagé.
- Livrables : schéma de base de données implémenté, flux d'inscription/de connexion fonctionnels, pages Thymeleaf de base connectées au backend.

## Semaine 2 — Chambres, réservations, clients, commentaires, fidélité

- Tâches :
    - Dév A : API CRUD pour les chambres + interface utilisateur pour le gestionnaire pour ajouter/modifier/supprimer, interface utilisateur pour le client pour parcourir les chambres.
    - Dév B : API de réservation (créer/afficher/modifier/annuler), interface utilisateur de réservation pour les clients, aperçu des réservations pour le gestionnaire.
    - Dév C : API CRUD client et employé + interface utilisateur gestionnaire ; API commentaires/évaluations + formulaire de soumission client ; système de points de fidélité (gagnés lors des séjours effectués).
- Livrables : workflows chambre + réservation fonctionnels, soumission des commentaires enregistrée, augmentation des points de fidélité lors des réservations effectuées (simulation initiale des séjours effectués).

## Semaine 3 — Événements/installations, planification, analyses, finitions, tests

- Tâches :
    - Dév A : API pour les événements et les installations + interface utilisateur pour les responsables pour la planification ; affichage client et réservation d'événements.
    - Dév B : API pour les horaires/équipes des employés + interface utilisateur RH ; notifications (e-mail ou stub dans l'application).
    - Dév C : points de terminaison d'analyse (occupation, revenus, note moyenne) + pages du tableau de bord ; assurance qualité, tests d'intégration, collection Postman, finalisation des rôles/permissions.
- Livrables : toutes les fonctionnalités principales sont présentes, tableau de bord analytique de base, stub de notification automatisé, documentation et collection Postman.
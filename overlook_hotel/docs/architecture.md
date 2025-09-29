# 🏨 Overlook Hotel — Architecture 
#### (🇫🇷 Français ci-dessous)

Last updated: 2025-09-22

This document describes the repository architecture, package responsibilities, key entities, concurrency rules, APIs, testing, and the recommended developer split for the Overlook Hotel MVP. 

## Repository layout
    
    .github/
        workflows/
            ci.yml

    db/
        schema.sql
        seed-data.sql

    postman/
        OverlookHotel.postman_collection.json
        OverlookHotel.postman_environment.json

    scripts/
        run-local.sh
        seed-db.sh

    src/
        main/
            java/
                com/example/overlook_hotel/
                config/
                    SecurityBeansConfig.java
                    SecurityConfig.java
                    JwtFilter.java
                    JwtUtils.java
                    CustomUserPrinciple.java
                    WebMvcConfig.java
                controller/
                    auth/
                        AuthController.java
                        PasswordResetController.java
                    api/
                        RoomController.java
                        FeatureController.java
                        ReservationController.java
                        ReportController.java
                        UserController.java
                    ui/
                        HomeController.java
                        GuestController.java
                        EmployeeController.java
                        ManagerController.java
                dto/
                    auth/
                        LoginRequest.java
                        AuthResponse.java
                        RegisterRequest.java
                        PasswordResetRequest.java
                    room/
                        RoomDto.java
                        RoomSearchRequest.java
                    reservation/
                        CreateReservationRequest.java
                        ReservationDto.java
                        CancelReservationRequest.java
                    user/
                        UserProfileDto.java
                    report/
                        OccupancyReportDto.java
                        RevenueReportDto.java
                exception/
                    ApiException.java
                    NotFoundException.java
                    ConflictException.java
                    GlobalExceptionHandler.java
                model/
                    enums/
                        RoleName.java
                        RoomStatus.java
                        ReservationStatus.java
                    entity/
                        User.java
                        Role.java
                        Room.java
                        Feature.java
                        RoomFeature.java
                        Reservation.java
                repository/
                    auth/
                        PasswordResetTokenRepository.java
                        RoleRepository.java
                        UserRepository.java
                    hotel/
                        RoomRepository.java
                        FeatureRepository.java
                        RoomFeatureRepository.java
                        ReservationRepository.java
                service/
                    auth/
                        AuthService.java
                        JwtService.java
                        UserDetailsServiceImpl.java
                        PasswordResetService.java
                        DataLoader.java <-- seeds demo data on dev profile
                    hotel/
                        UserService.java
                        RoomService.java
                        FeatureService.java
                        ReservationService.java
                        ReportService.java
                util/
                    Mapper.java
                    DateUtils.java
                    ValidationUtils.java
        resources/
            templates/
                index.html
                rooms.html
                room-detail.html
                login.html
                register.html
                guest/
                    dashboard.html
                    reservation.html
                employee/
                    reservations.html
                    room-status.html
                manager/
                    admin-dashboard.html
                    rooms-crud.html
                    reports.html
            static/
                css/
                js/
                img/
            application-dev.yml
            application.yml

    test/
        java/
            com.overlook.app/
                service/
                    ReservationServiceUnitTest.java
                    ReservationConcurrencyIntegrationTest.java
                controller/
                    AuthControllerTest.java
                repository/
                    ReservationRepositoryTest.java

    docs/
        requirements.md
        architecture.md
        API.md
        demo_flow.md
        diagrams/
            ER_MCD.png
            ER_MLD.png
            ER_MPD.png

    .gitignore
    environment.properties.example
    env.example
    mvnw
    mvnw.cmd
    pom.xml
    README.md

## Package responsibilities

### config

    /SecurityBeansConfig.java
    /SecurityConfig.java — Spring Security configuration (stateless JWT).
    /JwtFilter.java — validates JWT, injects Authentication.
    /JwtUtils.java — token creation/validation.
    /WebMvcConfig.java — interceptors for authentication and authorization, logging and monitoring, view resolvers, static resources

### controller

    auth/
        AuthController.java — register, login.
        PasswordResetController.java — simple token-based reset.
    api/
        RoomController.java — JSON REST endpoints for rooms.
        ReservationController.java — reservation CRUD endpoints.
        ReportController.java — manager reports.
        UserController.java — user/profile endpoints.
    ui/
        HomeController.java — public pages.
        GuestController.java — guest pages.
        EmployeeController.java — employee pages.
        ManagerController.java — manager pages.

*Controllers validate requests, call services, map DTOs.

### dto

- Data transport objects for requests/responses.
- Use javax.validation annotations for input validation.
- Examples: LoginRequest, RegisterRequest, RoomDto, CreateReservationRequest, ReservationDto, OccupancyReportDto.

### exception

    ApiException: base for application errors.
    NotFoundException, ConflictException, UnauthorizedException.
    GlobalExceptionHandler: converts exceptions to HTTP responses (400/401/403/404/409/500).

### model (JPA entities) 

The JPA model reflects core domain objects as entities with clear ownership and relationships: User (with Role), Room, Feature, RoomFeature (join), and Reservation. 

Entities use JPA annotations for primary keys, relationships, and optimistic locking via @Version; auditing fields (createdAt, updatedAt) are mapped and updated by listeners or DB triggers. Design choices:

- User: stores credentials (passwordHash), contact/profile fields, role relation, and audit fields. Expose via DTOs to avoid leaking sensitive fields.
- Role: simple entity used for RBAC; referenced by User.
- Room: captures roomNumber, type, capacity, basePrice, status, accessibility, and a features collection (ManyToMany via RoomFeature). Index frequently queried attributes (roomNumber, status).
- Feature / RoomFeature: Feature is reusable capability/amenity; RoomFeature is an explicit join entity to allow metadata (addedAt, source) and a composite unique constraint.
- Reservation: references User and Room, enforces date constraints (checkIn/checkOut), stores guestsCount, totalPrice and status. Use @Check/validation and database constraints plus indexed room-date columns to optimize availability queries.
    
- Concurrency & Integrity: use @Version for optimistic locking, DB CHECK constraints for date validity, and repository-level availability checks (select for update or versioned updates) to prevent double-booking.
- DTOs & Mapping: map entities to slim DTOs for controller responses; avoid returning passwordHash and internal version/timestamps unless needed.
- Extensibility: keep entities modular to add Events, Facilities, Loyalty and Audit entities later without breaking core booking flows.


    User: id, email (unique), passwordHash, firstName, lastName, phone, roles, enabled, createdAt, updatedAt.
    Role: id, name (GUEST, EMPLOYEE, MANAGER).
    Room: id, number (unique), type, capacity, pricePerNight, amenities (text), status (ENUM: AVAILABLE, OCCUPIED, CLEANING, MAINTENANCE).
    Reservation: id, guest (User), room (Room), startDate (LocalDate), endDate (LocalDate), status (BOOKED, CANCELLED, CHECKED_IN, CHECKED_OUT), totalPrice, createdAt, version (optimistic lock).
    RoomStatusLog: id, room, status, note, updatedBy, timestamp.
    AuditLog: id, userId, operation, targetType, targetId, timestamp.

### Entity design 

Entity design focuses on clear, normalized domain models that reflect real hotel concepts (User, Role, Room, Feature, Reservation, etc.), enforce data integrity with constraints and optimistic locking, and keep relationships explicit (FKs and join tables for many-to-many like room_features). Each entity maps to a layered service and repository, uses immutable auditing fields (created_at/updated_at/version) for concurrency control, and stores minimal denormalized fields (e.g., reservation total_price, lead_guest_name) to optimize read paths. Design decisions prioritize preventing double-booking (date checks, indexed room-date queries, and version-based updates), extensibility for features/events/loyalty, and security/privacy by keeping sensitive data hashed or tokenized and limiting direct exposure through DTOs.

### service

    AuthService: register, authenticate, token generation.
    UserService: user CRUD and profile updates.
    RoomService: room CRUD, search filters.
    RoomAvailabilityService: availability logic for searches.
    ReservationService: booking, modify, cancel — core transactional logic.
    ReportService: occupancy and revenue calculations.
    DataLoader: seeds roles, demo users, rooms, sample reservations when running with dev profile (or when seed script is used).

Services contain business rules and transactions. Controllers do not hold transactional logic.

### util

    /Mapper: entity ⇄ dto mapping helpers.
    /DateUtils: helper date validations (start < end).
    /ValidationUtils: shared validation helpers.

### Key design decisions

- One implementation per concern. No optional modules.
- Authentication: JWT-based stateless security.
- Password hashing: BCryptPasswordEncoder (strength 12).
- Database: PostgreSQL for dev; H2 for tests.
- No external integrations (email provider, payment gateways).
- Seed data provided in db/seed-data.sql and loaded by DataLoader in dev profile or via scripts/seed-db.sh.

### Database & indexes

- Use schema.sql as canonical schema.
    Indexes:
        users: email unique index
        rooms: number unique index
        reservations: index on (room_id, start_date, end_date)
    Reservation entity includes @Version (optimistic locking).

Developer setup & DB ownership (local dev)
    When running in the `dev` profile the application may use `spring.jpa.hibernate.ddl-auto: create-drop` (see `application-dev.yml`) so Hibernate can create/drop the schema automatically.

    If you encounter startup errors such as "ERROR: must be owner of table ..." it means the configured DB user does not own existing tables. Options to resolve locally:

    - Recreate the database and set the owner to the application user (recommended for local development):

        dropdb overlook_hotel
        createdb -O overlook_user overlook_hotel

    - Change ownership of existing schema/tables (requires superuser access):

        psql -d overlook_hotel -c "ALTER SCHEMA public OWNER TO overlook_user;"
        # or for individual tables:
        psql -d overlook_hotel -c "ALTER TABLE room_features OWNER TO overlook_user;"

    - Safer alternative: switch `ddl-auto` to `validate` and apply `db/schema.sql` + `db/seed-data.sql` (or use Flyway/Liquibase) to manage schema and seed data.

    The `scripts/seed-db.sh` helper can be used to load `db/seed-data.sql` into a fresh database.

### Preventing double-booking (concrete flow)

- ReservationRepository.existsConflictingReservation(roomId, startDate, endDate)
    ```SQL
    SQL: SELECT 1 FROM reservation r WHERE r.room_id = :roomId AND r.status IN ('BOOKED','CHECKED_IN') AND NOT (r.end_date <= :startDate OR r.start_date >= :endDate) LIMIT 1;
    ```

- ReservationService.createReservation(...) annotated with @Transactional:
    - Validate DTO (dates, guest).
    - Call existsConflictingReservation(...). If true, throw ConflictException (HTTP 409).
    - Calculate totalPrice = nights * room.pricePerNight.
    - Save reservation (reservationRepo.save).
    - Return reservation DTO.

- Concurrency safety:
    -  Rely on transactional repository check + optimistic locking on writes.
    - Integration test simulates concurrent requests; expected behavior: one succeeds, others fail with ConflictException. 

### API endpoints

    Auth
        POST /api/auth/register
        POST /api/auth/login -> { accessToken }
        POST /api/auth/password-reset
    Rooms
        GET /api/rooms?startDate=&endDate=&capacity=&type=&minPrice=&maxPrice=
        GET /api/rooms/{id}
        POST /api/rooms (MANAGER)
        PUT /api/rooms/{id} (MANAGER)
        PATCH /api/rooms/{id}/status (EMPLOYEE or MANAGER)
    Reservations
        POST /api/reservations (GUEST)
        GET /api/reservations (GUEST: own; EMPLOYEE/MANAGER: all)
        GET /api/reservations/{id}
        POST /api/reservations/{id}/cancel
    Reports
        GET /api/reports/occupancy?from=&to= (MANAGER)
        GET /api/reports/revenue?from=&to= (MANAGER)
    Health
        GET /actuator/health

### Security 

uses method-level annotations (@PreAuthorize) and URL protection in SecurityConfig.
Error handling & validation

    Use javax.validation on DTOs.
    GlobalExceptionHandler maps exceptions:
        400 Bad Request — validation errors.
        401 Unauthorized — authentication failures.
        403 Forbidden — authorization failures.
        404 Not Found — missing resources.
        409 Conflict — booking conflicts / business rule violations.
        500 Internal Server Error — unexpected errors.

### Testing plan

- Unit tests:
    - ReservationServiceUnitTest — booking rules, cancellation rules, price calc, date validation.
- Integration tests:
    - ReservationConcurrencyIntegrationTest — two threads attempt same booking window — assert one success, one ConflictException.
    - Controller tests with MockMvc for AuthController and RoomController.
- Test DB: H2 configured for tests; tests should be repeatable.

### CI

    GitHub Actions (.github/workflows/ci.yml)
        Steps: checkout, set up JDK 17, mvn -B test, build jar.
        Fail on failed tests.

### Seed & demo

    db/seed-data.sql creates roles, demo usersSeed (manager@overlook.test, employee@overlook.test, guest1/guest2@overlook.test), sample rooms (delux - occupancy 2, price 150euros, suite - occupancy 4, price 250euros , standard - occupancy 2, price 100euros), and at least one past reservation for feedback flows.
    scripts/seed-db.sh wraps psql call to load seed-data.sql.

### Responsibilities split

- Leila (Auth & infra)
     - SecurityConfig, JwtFilter, JwtUtils, AuthController, PasswordReset, User entity/repo/service, DataLoader seeds users, CI config, README.
- Jerome (Rooms & UI)
    - Room entity/repo/service/controllers, room templates, room status updates, RoomAvailabilityService, RoomController tests.
- Emanuelle (Reservations & tests)
    - Reservation entity/repo/service/controllers, booking concurrency logic and tests, Postman collection, docs (API.md, demo_flow.md). 

### Scripts

- scripts/run-local.sh — starts app with dev profile (sets SPRING_PROFILES=dev).
- scripts/seed-db.sh — psql command to load db/seed-data.sql.

### Docs to keep in repo

- README.md — setup & run.
- docs/requirements.md — outline project requirements & user stories 
- docs/architechture.md — this file! 
- docs/API.md — endpoint request/response examples.
- docs/demo_flow.md — step-by-step demo scenario.
- docs/diagrams/ — entity diagrams.
        ER_MVC.png — entity diagram.

### Next steps

- Scaffold core classes: entities, repositories, ReservationRepository.existsConflictingReservation(), ReservationService.createReservation() with @Transactional.

- Implement one end-to-end flow quickly: register → login → search → create reservation → see reservation list → cancel.

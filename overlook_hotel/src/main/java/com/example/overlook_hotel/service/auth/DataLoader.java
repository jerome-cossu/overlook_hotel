package com.example.overlook_hotel.service.auth;

import com.example.overlook_hotel.model.entity.*;
import com.example.overlook_hotel.model.enums.*;
import com.example.overlook_hotel.repository.auth.RoleRepository;
import com.example.overlook_hotel.repository.auth.UserRepository;
import com.example.overlook_hotel.repository.hotel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final RoomRepository roomRepo;
    private final FeatureRepository featureRepo;
    private final RoomFeatureRepository roomFeatureRepo;
    private final ReservationRepository reservationRepo;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepo,
                      UserRepository userRepo,
                      RoomRepository roomRepo,
                      FeatureRepository featureRepo,
                      RoomFeatureRepository roomFeatureRepo,
                      ReservationRepository reservationRepo,
                      PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
        this.featureRepo = featureRepo;
        this.roomFeatureRepo = roomFeatureRepo;
        this.reservationRepo = reservationRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Running DataLoader (dev profile)");

        createRoles();
        createUsers();
        createRoomsAndFeatures();
        createReservations();

        log.info("DataLoader finished");
    }

    private void createRoles() {
        Map<String, String> roles = Map.of(
                "GUEST", "Hotel guest / customer",
                "EMPLOYEE", "Hotel staff",
                "MANAGER", "Hotel manager / admin"
        );

        roles.forEach((name, desc) -> {
            roleRepo.findByName(name).orElseGet(() -> {
                Role r = new Role();
                r.setName(name);
                r.setDescription(desc);
                Role saved = roleRepo.save(r);
                log.info("Created role {}", saved.getName());
                return saved;
            });
        });
    }

    private void createUsers() {
        // plaintext passwords for demo accounts
        String demoPassword = "Password123!";

        createUserIfMissing("manager@overlook.test", "manager", "Manager", "Overlook", "MANAGER", demoPassword);
        createUserIfMissing("employee@overlook.test", "employee", "Employee", "Overlook", "EMPLOYEE", demoPassword);
        createUserIfMissing("guest1@overlook.test", "guest1", "Guest", "One", "GUEST", demoPassword);
        createUserIfMissing("guest2@overlook.test", "guest2", "Guest", "Two", "GUEST", demoPassword);
    }

    private void createUserIfMissing(String email, String username, String firstName, String lastName, String roleName, String plainPassword) {
        userRepo.findByEmail(email).orElseGet(() -> {
            Role role = roleRepo.findByName(roleName).orElseThrow();
            User u = new User();
            u.setEmail(email);
            u.setUsername(username);
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setPasswordHash(passwordEncoder.encode(plainPassword));
            u.setRole(role);
            User saved = userRepo.save(u);
            log.info("Created user {} with role {}", email, roleName);
            return saved;
        });
    }

    private void createRoomsAndFeatures() {
        // Features
        Map<String, String> features = Map.of(
                "wifi", "Wi‑Fi",
                "ensuite", "En-suite Bathroom",
                "tv", "Television",
                "balcony", "Balcony",
                "minibar", "Minibar",
                "sofa", "Sofa / Seating"
        );

        features.forEach((code, name) -> {
            featureRepo.findByCode(code).orElseGet(() -> {
                Feature f = new Feature();
                f.setCode(code);
                f.setName(name);
                f.setCategory("room_feature");
                Feature saved = featureRepo.save(f);
                log.info("Created feature {}", code);
                return saved;
            });
        });

        // Rooms
        createRoomIfMissing("101", "Standard", 2, new BigDecimal("100.00"), 1, false, List.of("wifi", "ensuite", "tv"));
        createRoomIfMissing("102", "Deluxe", 2, new BigDecimal("150.00"), 1, false, List.of("wifi", "ensuite", "tv", "balcony"));
        createRoomIfMissing("201", "Suite", 4, new BigDecimal("250.00"), 2, true, List.of("wifi", "ensuite", "tv", "minibar", "sofa"));
    }

    private void createRoomIfMissing(String number, String type, int capacity, BigDecimal price, int floor, boolean accessible, List<String> featureCodes) {
        roomRepo.findByRoomNumber(number).orElseGet(() -> {
            Room r = new Room();
            r.setRoomNumber(number);
            r.setRoomType(type);
            r.setCapacity(capacity);
            r.setBasePrice(price);
            r.setFloorNumber(floor);
            r.setIsAccessible(accessible);
            r.setStatus("AVAILABLE");
            Room saved = roomRepo.save(r);

            // attach features
            for (String code : featureCodes) {
                Feature f = featureRepo.findByCode(code).orElseThrow();
                // avoid duplicates
                if (roomFeatureRepo.existsByRoomIdAndFeatureId(saved.getId(), f.getId())) continue;
                RoomFeature rf = new RoomFeature();
                rf.setRoom(saved);
                rf.setFeature(f);
                roomFeatureRepo.save(rf);
            }

            log.info("Created room {}", number);
            return saved;
        });
    }

    private void createReservations() {
        // Two demo reservations with relative dates
        createReservationIfMissing("guest1@overlook.test", "101", LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), 2, new BigDecimal("300.00"));
        createReservationIfMissing("guest2@overlook.test", "201", LocalDate.now().plusDays(30), LocalDate.now().plusDays(33), 3, new BigDecimal("750.00"));
    }

    private void createReservationIfMissing(String guestEmail, String roomNumber, LocalDate checkIn, LocalDate checkOut, int guests, BigDecimal totalPrice) {
        // Simple uniqueness check: same user + room + check_in
        Optional<User> userOpt = userRepo.findByEmail(guestEmail);
        Optional<Room> roomOpt = roomRepo.findByRoomNumber(roomNumber);
        if (userOpt.isEmpty() || roomOpt.isEmpty()) {
            log.warn("Skipping reservation seed: missing user or room ({} / {})", guestEmail, roomNumber);
            return;
        }
        User user = userOpt.get();
        Room room = roomOpt.get();

        boolean exists = reservationRepo.existsByUserIdAndRoomIdAndCheckInDate(user.getId(), room.getId(), checkIn);
        if (exists) {
            log.info("Reservation for user {} room {} on {} already exists, skipping", guestEmail, roomNumber, checkIn);
            return;
        }

        Reservation r = new Reservation();
        r.setUser(user);
        r.setRoom(room);
        r.setLeadGuestName(user.getFirstName() + " " + user.getLastName());
        r.setLeadGuestPhone("555-0000");
        r.setCheckInDate(checkIn);
        r.setCheckOutDate(checkOut);
        r.setGuestsCount(guests);
        r.setTotalPrice(totalPrice);
        r.setStatus("BOOKED");
        reservationRepo.save(r);
        log.info("Created reservation for {} in room {} from {} to {}", guestEmail, roomNumber, checkIn, checkOut);
    }
}

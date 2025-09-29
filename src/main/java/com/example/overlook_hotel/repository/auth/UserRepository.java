package com.example.overlook_hotel.repository.auth;

import com.example.overlook_hotel.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // return list because names are not unique
    List<User> findAllByLastName(String lastName);
    List<User> findAllByFirstName(String firstName);

    // case insensitive search
    Optional<User> findByEmailIgnoreCase(String email);
}

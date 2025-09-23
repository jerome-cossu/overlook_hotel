package com.example.overlook_hotel.repository;
import com.example.overlook_hotel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByLastName(String lastName);
    User findByFirstName(String firstName);
}

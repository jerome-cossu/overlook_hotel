package com.example.overlook_hotel.repository;
import com.example.overlook_hotel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByLastName(String lastName);
    User findByFirstName(String firstName);
}

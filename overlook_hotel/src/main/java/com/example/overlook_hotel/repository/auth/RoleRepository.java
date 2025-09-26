package com.example.overlook_hotel.repository.auth;

import com.example.overlook_hotel.model.entity.Role;
import com.example.overlook_hotel.model.enums.RoleName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}

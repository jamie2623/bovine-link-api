package com.bovinelink.bovinelinkapi.repository;

import com.bovinelink.bovinelinkapi.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByName(String name);
}

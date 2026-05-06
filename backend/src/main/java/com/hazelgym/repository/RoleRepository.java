package com.hazelgym.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.Role;
import com.hazelgym.model.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
